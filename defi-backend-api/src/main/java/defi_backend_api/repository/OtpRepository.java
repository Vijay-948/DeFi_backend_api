package defi_backend_api.repository;

import defi_backend_api.entity.OtpBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpBO, String> {

    Optional<OtpBO> findByEmail(String email);
}
