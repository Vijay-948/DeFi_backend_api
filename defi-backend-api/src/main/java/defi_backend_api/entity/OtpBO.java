package defi_backend_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otp_auth", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class OtpBO {

    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "one_time_password")
    private String verificationCode;

    @Column(name = "otp_expiry_time")
    private Date otpExpiryTime;
}
