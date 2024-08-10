package defi_backend_api.service.impl;

import defi_backend_api.dto.AuthResponseToken;
import defi_backend_api.dto.LoginDto;
import defi_backend_api.dto.OtpDto;
import defi_backend_api.dto.UserDetailsDto;
import defi_backend_api.service.AuthenticationUserService;
import defi_backend_api.service.OtpAuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationUserServiceImpl implements AuthenticationUserService, OtpAuthService {


    @Override
    public void sendVerificationCode(String email) {

    }

    @Override
    public AuthResponseToken verifyOtp(OtpDto otpDto) {
        return null;
    }

    @Override
    public void register(UserDetailsDto request) {

    }

    @Override
    public AuthResponseToken login(LoginDto request) {
        return null;
    }

    @Override
    public UserDetailsDto getFirstNameAndLastName(String token) {
        return null;
    }
}
