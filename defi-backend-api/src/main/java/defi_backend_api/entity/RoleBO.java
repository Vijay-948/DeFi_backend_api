package defi_backend_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles_lookup")
public class RoleBO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_roles_lookup_id_generator")
    @SequenceGenerator(
            name = "user_roles_lookup_id_generator",
            sequenceName = "user_roles_lookup_id_seq",
            allocationSize = 1)
    private Integer id;

    @Column(name = "name")
    private String name;
}

