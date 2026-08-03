package mst.local.mstsoftware.modules.user.entities;

import jakarta.persistence.*;
import lombok.*;
import mst.local.mstsoftware.modules.user.enums.RoleType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType name = RoleType.USER;

    private String description;

    @Column(name = "is_system", nullable = false)
    @Builder.Default
    private boolean isSystem = false;
}
