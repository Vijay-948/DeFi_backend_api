package defi_backend_api.repository;

import defi_backend_api.entity.OtpBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OtpRepository extends CrudRepository<OtpBO, String> {

    Optional<OtpBO> findByEmail(String email);
}
