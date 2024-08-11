package defi_backend_api.controller;

import defi_backend_api.dto.AppResponseDto;
import defi_backend_api.dto.ForgotPasswordRequestDto;
import defi_backend_api.dto.ResetPasswordDto;
import defi_backend_api.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reset")
public class ForgotPasswordController {

    @Autowired
    ForgotPasswordService service;

    public AppResponseDto<String> forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto){
        service.sendOtpToEmail(forgotPasswordRequestDto.getEmail());
        return new AppResponseDto<>(null, "OTP sent Successfully");
    }


    public AppResponseDto<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
        service.resetPassword(resetPasswordDto);
        return new AppResponseDto<>(null, "Password Updated Successfully");
    }
}
