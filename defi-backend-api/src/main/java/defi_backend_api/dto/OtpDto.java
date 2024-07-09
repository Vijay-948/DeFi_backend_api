package defi_backend_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpDto {
    private String email;
    private String verificationCode;
    private Date otpExpiryTime;
}
