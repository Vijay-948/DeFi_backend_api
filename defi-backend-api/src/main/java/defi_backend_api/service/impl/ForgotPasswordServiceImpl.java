package defi_backend_api.service.impl;

import defi_backend_api.dto.ResetPasswordDto;
import defi_backend_api.entity.OtpBO;
import defi_backend_api.entity.UserDetailsBO;
import defi_backend_api.exception.InvalidCredentialsException;
import defi_backend_api.repository.OtpRepository;
import defi_backend_api.repository.UserDetailsRepository;
import defi_backend_api.service.EmailService;
import defi_backend_api.service.ForgotPasswordService;
import defi_backend_api.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;


import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtService jwtService;

    @Autowired
    OtpRepository otpRepository;

    @Override
    public void sendOtpToEmail(String email) {
        UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Email" + email));

        String otp = CommonUtil.generateOTP(6);

        String subject = "One Time Password for Reset Pin";
        String fileName = "otp_verification.html";

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
    public void resetPassword(ResetPasswordDto resetPassword) {
        UserDetailsBO userDetailsBO = userDetailsRepository.findByEmailIgnoreCase(resetPassword.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String newPassword = resetPassword.getNewPassword();
        String confirmPassword = resetPassword.getConfirmPassword();

        if(!newPassword.equals(confirmPassword)){
            throw new InvalidCredentialsException("New password and confirm password must match");
        }

        String encodePassword = passwordEncoder.encode(confirmPassword);
        userDetailsBO.setPassword(encodePassword);

        String newToken = jwtService.generateToken(userDetailsBO);

        userDetailsRepository.save(userDetailsBO);

    }
}
