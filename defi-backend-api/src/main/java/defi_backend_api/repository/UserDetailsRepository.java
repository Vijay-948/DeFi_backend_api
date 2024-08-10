package defi_backend_api.repository;

import defi_backend_api.entity.UserDetailsBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetailsBO, Integer> {

    Optional<UserDetailsBO> findByEmail(String email);
}
