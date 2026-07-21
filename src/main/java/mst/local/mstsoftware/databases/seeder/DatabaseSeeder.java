package mst.local.mstsoftware.databases.seeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.users.entities.User;
import mst.local.mstsoftware.modules.users.entities.UserCatalogue;
import mst.local.mstsoftware.modules.users.repositories.UserCatalogueRepository;
import mst.local.mstsoftware.modules.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserCatalogueRepository userCatalogueRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (isUserTableEmpty() || isUserCatalogueTableEmpty()) {
            UserCatalogue userCatalogue = UserCatalogue.builder()
                    .name("Admin")
                    .publish(true)
                    .build();
            userCatalogue = userCatalogueRepository.save(userCatalogue);

            String password = passwordEncoder.encode("password");
            User user = User.builder()
                    .name("Phạm Hoàng Tuấn")
                    .email("phamhoangtuanqn@gmail.com")
                    .password(password)
                    .userCataloguesId(userCatalogue.getId())
                    .phone("0812665001")
                    .build();
            userRepository.save(user);

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

}
