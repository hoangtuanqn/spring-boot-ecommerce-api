package mst.local.mstsoftware.databases.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (isTableEmpty()) {
            String password = passwordEncoder.encode("password");
            String sqlString = "INSERT INTO users (name, email, password, user_catalogues_id, phone) VALUES (?, ?, ?, ?, ?)";
            entityManager.createNativeQuery(sqlString)
                    .setParameter(1, "Phạm Hoàng Tuấn")
                    .setParameter(2, "phamhoangtuanqn@gmail.com")
                    .setParameter(3, password)
                    .setParameter(4, 1)
                    .setParameter(5, "0812665001")
                    .executeUpdate();

        }
    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
        return count == 0;
    }

}
