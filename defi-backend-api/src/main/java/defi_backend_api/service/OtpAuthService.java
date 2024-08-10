package defi_backend_api.service;

import defi_backend_api.dto.AuthResponseToken;
import defi_backend_api.dto.OtpDto;

public interface OtpAuthService {
    void sendVerificationCode(String email);

    AuthResponseToken verifyOtp(OtpDto otpDto);
}
