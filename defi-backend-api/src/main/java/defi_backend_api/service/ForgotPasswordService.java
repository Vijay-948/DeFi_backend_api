package defi_backend_api.service;

import defi_backend_api.dto.ResetPasswordDto;

public interface ForgotPasswordService {

    public void sendOtpToEmail(String email);

    public void resetPassword(ResetPasswordDto resetPassword);
}
