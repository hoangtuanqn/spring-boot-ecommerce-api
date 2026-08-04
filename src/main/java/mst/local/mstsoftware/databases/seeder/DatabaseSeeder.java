package mst.local.mstsoftware.databases.seeder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.product.entities.Category;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.CategoryRepository;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.modules.user.entities.*;
import mst.local.mstsoftware.modules.user.enums.RoleType;
import mst.local.mstsoftware.modules.user.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedPermissions();
        seedRoles();
        seedUsers();
        seedCategoryAndProduct();
    }

    // =============================================
    // PERMISSIONS
    // =============================================
    private void seedPermissions() {
        if (permissionRepository.count() > 0) return;

        List<Permission> permissions = List.of(
                // POST
                Permission.builder().resource("POST").action("READ").description("Xem bài viết").build(),
                Permission.builder().resource("POST").action("CREATE").description("Tạo bài viết").build(),
                Permission.builder().resource("POST").action("UPDATE").description("Sửa bài viết").build(),
                Permission.builder().resource("POST").action("DELETE").description("Xóa bài viết").build(),
                // USER
                Permission.builder().resource("USER").action("READ").description("Xem danh sách user").build(),
                Permission.builder().resource("USER").action("CREATE").description("Tạo user").build(),
                Permission.builder().resource("USER").action("UPDATE").description("Sửa user").build(),
                Permission.builder().resource("USER").action("DELETE").description("Xóa user").build(),
                // ROLE
                Permission.builder().resource("ROLE").action("READ").description("Xem danh sách role").build(),
                Permission.builder().resource("ROLE").action("CREATE").description("Tạo role").build(),
                Permission.builder().resource("ROLE").action("UPDATE").description("Sửa role").build(),
                Permission.builder().resource("ROLE").action("DELETE").description("Xóa role").build(),
                // PRODUCT
                Permission.builder().resource("PRODUCT").action("READ").description("Xem sản phẩm").build(),
                Permission.builder().resource("PRODUCT").action("CREATE").description("Tạo sản phẩm").build(),
                Permission.builder().resource("PRODUCT").action("UPDATE").description("Sửa sản phẩm").build(),
                Permission.builder().resource("PRODUCT").action("DELETE").description("Xóa sản phẩm").build(),
                // CATEGORY
                Permission.builder().resource("CATEGORY").action("READ").description("Xem danh mục").build(),
                Permission.builder().resource("CATEGORY").action("CREATE").description("Tạo danh mục").build(),
                Permission.builder().resource("CATEGORY").action("UPDATE").description("Sửa danh mục").build(),
                Permission.builder().resource("CATEGORY").action("DELETE").description("Xóa danh mục").build()
        );

        permissionRepository.saveAll(permissions);
    }

    // =============================================
    // ROLES + gán permissions cho từng role
    // =============================================
    private void seedRoles() {
        if (roleRepository.count() > 0) return;

        // Load tất cả permissions đã seed
        List<Permission> allPermissions = permissionRepository.findAll();

        // Helper lấy permission theo resource:action
        java.util.function.BiFunction<String, String, Permission> find =
                (resource, action) -> allPermissions.stream()
                        .filter(p -> p.getResource().equals(resource) && p.getAction().equals(action))
                        .findFirst()
                        .orElseThrow();

        // ADMIN — toàn quyền
        Role adminRole = Role.builder()
                .name(RoleType.ADMIN)
                .description("Toàn quyền hệ thống")
                .isSystem(true)
                .permissions(Set.copyOf(allPermissions)) // tất cả permissions
                .build();

        // EDITOR — quản lý nội dung, không động user/role
        Role editorRole = Role.builder()
                .name(RoleType.EDITOR)
                .description("Quản lý nội dung")
                .isSystem(false)
                .permissions(Set.of(
                        find.apply("POST", "READ"),
                        find.apply("POST", "CREATE"),
                        find.apply("POST", "UPDATE"),
                        find.apply("POST", "DELETE"),
                        find.apply("PRODUCT", "READ"),
                        find.apply("PRODUCT", "CREATE"),
                        find.apply("PRODUCT", "UPDATE"),
                        find.apply("CATEGORY", "READ"),
                        find.apply("CATEGORY", "CREATE"),
                        find.apply("CATEGORY", "UPDATE")
                ))
                .build();

        // USER — chỉ xem
        Role userRole = Role.builder()
                .name(RoleType.USER)
                .description("Người dùng thông thường")
                .isSystem(false)
                .permissions(Set.of(
                        find.apply("POST", "READ"),
                        find.apply("PRODUCT", "READ"),
                        find.apply("CATEGORY", "READ")
                ))
                .build();

        roleRepository.saveAll(List.of(adminRole, editorRole, userRole));
    }

    // =============================================
    // USERS + gán roles
    // =============================================
    private void seedUsers() {
        if (userRepository.count() > 0) return;

        // UserCatalogue
        UserCatalogue adminCatalogue = userCatalogueRepository.save(
                UserCatalogue.builder().name("Admin").publish(1).build()
        );
        UserCatalogue editorCatalogue = userCatalogueRepository.save(
                UserCatalogue.builder().name("Editor").publish(1).build()
        );
        UserCatalogue userCatalogue = userCatalogueRepository.save(
                UserCatalogue.builder().name("User").publish(1).build()
        );

        String password = passwordEncoder.encode("password");

        Role adminRole = roleRepository.findByName(RoleType.ADMIN).orElseThrow();
        Role editorRole = roleRepository.findByName(RoleType.EDITOR).orElseThrow();
        Role userRole = roleRepository.findByName(RoleType.USER).orElseThrow();

        // Admin user
        User admin = userRepository.save(
                User.builder()
                        .name("Phạm Hoàng Tuấn")
                        .email("phamhoangtuanqn@gmail.com")
                        .password(password)
                        .phone("0812665001")
                        .userCataloguesId(adminCatalogue.getId())
                        .build()
        );

        // Editor user
        User editor = userRepository.save(
                User.builder()
                        .name("Editor MST")
                        .email("editor@mst.com")
                        .password(password)
                        .phone("0900000002")
                        .userCataloguesId(editorCatalogue.getId())
                        .build()
        );

        // Normal user
        User normalUser = userRepository.save(
                User.builder()
                        .name("User MST")
                        .email("user@mst.com")
                        .password(password)
                        .phone("0900000003")
                        .userCataloguesId(userCatalogue.getId())
                        .build()
        );

        // Gán roles — granted_by NULL vì đây là seed ban đầu
        UserRole adminUserRole = UserRole.builder()
                .user(admin)
                .role(adminRole)
                .grantedBy(null)
                .expiresAt(null)
                .build();

        UserRole editorUserRole = UserRole.builder()
                .user(editor)
                .role(editorRole)
                .grantedBy(admin)
                .expiresAt(null)
                .build();

        UserRole normalUserRole = UserRole.builder()
                .user(normalUser)
                .role(userRole)
                .grantedBy(admin)
                .expiresAt(null)
                .build();

        userRoleRepository.saveAll(List.of(adminUserRole, editorUserRole, normalUserRole));
    }

    // =============================================
    // CATEGORY + PRODUCT
    // =============================================
    private void seedCategoryAndProduct() {
        if (categoryRepository.count() > 0 && productRepository.count() > 0) return;

        Category iphone = categoryRepository.save(
                Category.builder().name("iPhone").build()
        );
        Category samsung = categoryRepository.save(
                Category.builder().name("Samsung").build()
        );

        productRepository.saveAll(List.of(
                Product.builder()
                        .title("iPhone 17")
                        .description("iPhone 17 hàng chính hãng!")
                        .price(BigDecimal.valueOf(35000000))
                        .quantity(100)
                        .category(iphone)
                        .build(),
                Product.builder()
                        .title("iPhone 17 Pro Max")
                        .description("iPhone 17 Pro Max hàng chính hãng!")
                        .price(BigDecimal.valueOf(50000000))
                        .quantity(50)
                        .category(iphone)
                        .build(),
                Product.builder()
                        .title("Samsung Galaxy S25")
                        .description("Samsung Galaxy S25 hàng chính hãng!")
                        .price(BigDecimal.valueOf(28000000))
                        .quantity(80)
                        .category(samsung)
                        .build()
        ));
    }
}