package defi_backend_api.controller;

import defi_backend_api.dto.*;
import defi_backend_api.service.AuthenticationUserService;
import defi_backend_api.service.OtpAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    AuthenticationUserService service;

    @Autowired
    OtpAuthService otpAuthService;

    @PostMapping("/signup")
    public AppResponseDto<String> signUp(@RequestBody UserDetailsDto userDetailsDto){
        service.register(userDetailsDto);
        return new AppResponseDto<>(null, "User Created Successfully");

    }

    @PostMapping("/sent")
    public void sentOTP(@RequestBody OtpDto otpDto){
        otpAuthService.sendVerificationCode(otpDto.getEmail());
    }

    @PostMapping("/verify")
    public AuthResponseToken verifyOTP(@RequestBody OtpDto otpDto){
        return otpAuthService.verifyOtp(otpDto);
    }

    @PostMapping("/login")
    public AppResponseDto<String> login(@RequestBody LoginDto loginDto){
        service.login(loginDto);
        return new AppResponseDto<>(null, "User login Successfully");
    }




}
