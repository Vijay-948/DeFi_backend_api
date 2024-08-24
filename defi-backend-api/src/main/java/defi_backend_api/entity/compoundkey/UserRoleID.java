package defi_backend_api.entity.compoundkey;

import java.io.Serializable;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleID implements Serializable {
    private Integer user;
    private Integer role;
}
