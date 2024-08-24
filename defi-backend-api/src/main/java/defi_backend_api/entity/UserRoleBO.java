package defi_backend_api.entity;

import defi_backend_api.entity.compoundkey.UserRoleID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_role_mapping")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserRoleID.class)
public class UserRoleBO {

    @Id
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserDetailsBO user;

    @Id
    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    RoleBO role;
}

