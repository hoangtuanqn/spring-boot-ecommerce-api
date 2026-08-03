package mst.local.mstsoftware.databases.seeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.product.entities.Category;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.modules.user.entities.Role;
import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.entities.UserCatalogue;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.modules.user.repositories.RoleRepository;
import mst.local.mstsoftware.modules.user.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserCatalogueRepository userCatalogueRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (isRoleTableEmpty()) {
            Role adminRole = Role.builder().name(RoleType.ADMIN).build();
            Role userRole = Role.builder().name(RoleType.USER).build();
            roleRepository.saveAll(List.of(adminRole, userRole));
        }
        if (isUserTableEmpty() || isUserCatalogueTableEmpty()) {

            UserCatalogue userCatalogue = UserCatalogue.builder()
                    .name("Admin")
                    .publish(1)
                    .build();
            userCatalogue = userCatalogueRepository.save(userCatalogue);

            String password = passwordEncoder.encode("password");
            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ADMIN).build()));

            User user = User.builder()
                    .role(adminRole)
                    .name("Phạm Hoàng Tuấn")
                    .email("phamhoangtuanqn@gmail.com")
                    .password(password)
                    .userCataloguesId(userCatalogue.getId())
                    .phone("0812665001")
                    .build();
            userRepository.save(user);

        }

        if (isCategoryAndProductTableEmpty()) {
            Category category = Category.builder()
                    .name("Iphone").build();

            category = categoryRepository.save(category);

            Product product = Product.builder()
                    .title("Iphone 17")
                    .description("Iphone 17 hàng chính hãng!")
                    .price(BigDecimal.valueOf(100000))
                    .quantity(100)
                    .category(category)
                    .build();
            productRepository.save(product);
        }


    }

    private boolean isUserTableEmpty() {
//        Long count = (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
//        return count == 0;
        return userRepository.count() == 0;
    }

    private boolean isUserCatalogueTableEmpty() {
        return userCatalogueRepository.count() == 0;
    }

    private boolean isCategoryAndProductTableEmpty() {
        return categoryRepository.count() == 0 && productRepository.count() == 0;
    }

    private boolean isRoleTableEmpty() {
        return roleRepository.count() == 0;
    }

}
