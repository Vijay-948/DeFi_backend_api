package defi_backend_api.service.impl;

import defi_backend_api.constants.Role;
import defi_backend_api.dto.AuthResponseToken;
import defi_backend_api.dto.LoginDto;
import defi_backend_api.dto.OtpDto;
import defi_backend_api.dto.UserDetailsDto;
import defi_backend_api.entity.OtpBO;
import defi_backend_api.entity.UserDetailsBO;
import defi_backend_api.exception.InvalidCredentialsException;
import defi_backend_api.repository.OtpRepository;
import defi_backend_api.repository.UserDetailsRepository;
import defi_backend_api.service.AuthenticationUserService;
import defi_backend_api.service.EmailService;
import defi_backend_api.service.OtpAuthService;
import defi_backend_api.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationUserServiceImpl implements AuthenticationUserService, OtpAuthService {

    @Autowired
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final OtpRepository otpRepository;

    @Autowired
    private final EmailService emailService;

    @Override
    public void register(UserDetailsDto request) {
//        UserDetailsBO userDetailsBO = new UserDetailsBO();
//
//        userDetailsRepository.findByEmailIgnoreCase(request.getEmail())
//                .ifPresent(user -> {
//                    throw new IllegalArgumentException("User with the email " + userDetailsBO.getEmail() + " already Exist.");
//                });

        Optional<UserDetailsBO> email = userDetailsRepository.findByEmailIgnoreCase(request.getEmail());

        if(email.isPresent()){
            UserDetailsBO existingEmail = email.get();
            if(!existingEmail.getActive()){
                sendVerificationCode(existingEmail.getEmail());
            }else{
                throw new IllegalArgumentException("User with the email " + existingEmail.getEmail() + " already Exist.");
            }
        }else{
            UserDetailsBO newUser = UserDetailsBO.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .active(false)
                    .createdOn(new Date())
                    .role(Role.USER)
                    .modifiedOn(new Date())
                    .build();

            userDetailsRepository.save(newUser);

            sendVerificationCode(request.getEmail());
        }

    }


    @Override
    public void sendVerificationCode(String email) {
        UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found with Email " + email));

        String subject = "One Time Password for Signup";
        String fileName = "otp_verification.html";

        String otp = CommonUtil.generateOTP(6);

        try{
            String content = StreamUtils.copyToString(new ClassPathResource(fileName).getInputStream(), Charset.defaultCharset());

            content = content.replace("[OTP]", otp);
            content = content.replace("[NAME]", userDetailsBO.getFirstName() + " " + userDetailsBO.getLastName());

            emailService.sendEmail(email, subject, content);

            OtpBO otpBO = new OtpBO();
            otpBO.setEmail(email);
            otpBO.setVerificationCode(otp);

            LocalDateTime presentTime = LocalDateTime.now().plusMinutes(10);
            Date expiryTime = Date.from(presentTime.atZone(ZoneId.systemDefault()).toInstant());
            otpBO.setOtpExpiryTime(expiryTime);
            otpRepository.save(otpBO);
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }

    @Override
    public AuthResponseToken verifyOtp(OtpDto otpDto) {
        OtpBO otpBO = otpRepository.findByEmailIgnoreCase(otpDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid OTP"));


        if(!otpBO.getVerificationCode().equals(otpDto.getVerificationCode())){
            throw new InvalidCredentialsException("Invalid OTP");
        }

        if(!isWithinTimeDiff(otpBO.getOtpExpiryTime())){
            throw new InvalidCredentialsException("OTP Expired");
        }

        UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(otpDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email not Found"));

        userDetailsBO.setActive(true);
        userDetailsRepository.save(userDetailsBO);

        otpRepository.delete(otpBO);

        String generateJwtToken = jwtService.generateToken(userDetailsBO);

        return AuthResponseToken.builder()
                .token(generateJwtToken)
                .build();

    }


    @Override
    public AuthResponseToken login(LoginDto request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

            if(!userDetailsBO.getActive()){
                throw new InvalidCredentialsException(userDetailsBO.getFirstName() + " " + userDetailsBO.getLastName() + "Your account is currently inactive. Please verify your email or contact support for assistance.");
            }

            String generateToken = jwtService.generateToken(userDetailsBO);

//            if(userDetailsBO.getRole() == Role.ADMIN){
//                List<UserDetailsBO> users =  userDetailsRepository.findAll();
//
//                List<UserDetailsDto> userDetailsDtoList = users.
//
//            }

            return AuthResponseToken.builder()
                    .token(generateToken)
                    .build();
        }catch (AuthenticationException e){
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @Override
    public UserDetailsDto getFirstNameAndLastName(String token) {
        String email = jwtService.extractUserName(token);
        UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new InvalidCredentialsException("user not found"));

        return UserDetailsDto.builder()
                .firstName(userDetailsBO.getFirstName())
                .lastName(userDetailsBO.getLastName())
                .email(userDetailsBO.getEmail())
                .build();
    }

    private boolean isWithinTimeDiff(Date otpTimeStamp){
        if(otpTimeStamp == null) return false;

        LocalDateTime storedTime = otpTimeStamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();

        return currentTime.isBefore(storedTime) || currentTime.equals(storedTime);
    }
}
