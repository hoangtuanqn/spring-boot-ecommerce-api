package mst.local.mstsoftware.modules.user.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_catalogues_id")
    private Long userCataloguesId;

//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    @JoinColumn(name = "role_id", nullable = false,
//            foreignKey = @ForeignKey(name = "fk_role_id"))
//    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRole> userRoles = new HashSet<>();

    private String name;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Column(unique = true)
    private String phone;
    private String image;
    private String address;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public User(String name, String email, String password, Long userCataloguesId, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userCataloguesId = userCataloguesId;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreated() {
        createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdated() {
        updatedAt = Instant.now();
    }

}
