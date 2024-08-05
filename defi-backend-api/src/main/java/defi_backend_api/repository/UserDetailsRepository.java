package defi_backend_api.repository;

import defi_backend_api.entity.UserDetailsBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetailsBO, Integer> {

    Optional<UserDetailsBO> findByEmail(String email);
}
