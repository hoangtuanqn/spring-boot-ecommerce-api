**Có 2 trường phái cấu trúc chính — chọn đúng ngay từ đầu:**

```
# Trường phái 1 — Layer-based (KHÔNG nên dùng cho dự án lớn)
src/
├── controllers/
├── services/
├── repositories/
└── models/

# Trường phái 2 — Module-based / Feature-based (bạn đang dùng, ĐÚNG hướng)
src/
├── modules/
│   ├── users/
│   └── orders/
└── shared/
```

Layer-based dễ vỡ khi scale: tìm logic của `User` phải nhảy qua 4 folder khác nhau. Module-based giữ tất cả logic liên
quan 1 domain nằm cùng chỗ.

**Cấu trúc module-based chuẩn cho dự án Spring Boot production:**

```
src/main/java/mst/local/mstsoftware/
│
├── MstSoftwareApplication.java
│
├── config/                          # Cấu hình toàn hệ thống
│   ├── SecurityConfig.java
│   ├── RedisConfig.java
│   ├── AuthConfig.java
│   └── OpenApiConfig.java
│
├── shared/                          # Code dùng chung, không thuộc module nào
│   │
│   ├── resources/                   # Response wrapper
│   │   ├── ApiResource.java         # ApiResource<T>
│   │   ├── ErrorResource.java       # ErrorResource (code, message, details)
│   │   └── FieldErrorResource.java
│   │
│   ├── exceptions/                  # Custom exception dùng chung
│   │   ├── ResourceNotFoundException.java
│   │   ├── BusinessException.java
│   │   └── TokenException.java
│   │
│   ├── handlers/                    # Global handler
│   │   ├── GlobalExceptionHandler.java
│   │   └── CustomAuthenticationEntryPoint.java
│   │
│   ├── filters/                     # Servlet filter
│   │   └── JwtAuthFilter.java
│   │
│   ├── services/                    # Service infrastructure dùng chung
│   │   ├── interfaces/
│   │   │   ├── JwtServiceInterface.java
│   │   │   └── BlacklistServiceInterface.java
│   │   └── impl/
│   │       ├── JwtServiceImpl.java
│   │       └── BlacklistServiceImpl.java
│   │
│   └── utils/                       # Utility, helper thuần function
│       ├── DateUtils.java
│       └── StringUtils.java
│
└── modules/                         # Mỗi module = 1 business domain
    │
    ├── users/
    │   ├── controllers/
    │   │   └── AuthController.java
    │   │
    │   ├── services/
    │   │   ├── interfaces/
    │   │   │   ├── UserServiceInterface.java
    │   │   │   └── RefreshTokenServiceInterface.java
    │   │   └── impl/
    │   │       ├── UserServiceImpl.java
    │   │       └── RefreshTokenServiceImpl.java
    │   │
    │   ├── repositories/
    │   │   ├── UserRepository.java
    │   │   └── RefreshTokenRepository.java
    │   │
    │   ├── entities/                # JPA entity — ánh xạ 1-1 với DB table
    │   │   ├── User.java
    │   │   └── RefreshToken.java
    │   │
    │   ├── dto/                     # Trung gian truyền data giữa các layer
    │   │   ├── UserDto.java
    │   │   └── RefreshTokenDto.java
    │   │
    │   ├── requests/                # Request body từ client vào
    │   │   ├── LoginRequest.java
    │   │   ├── RegisterRequest.java
    │   │   └── UpdateProfileRequest.java
    │   │
    │   ├── resources/               # Response payload trả ra ngoài
    │   │   ├── LoginResource.java
    │   │   ├── UserResource.java
    │   │   └── AuthResult.java
    │   │
    │   └── mappers/                 # Convert giữa entity ↔ dto ↔ resource
    │       └── UserMapper.java
    │
    └── products/                    # Module khác tương tự, cấu trúc y hệt
        ├── controllers/
        ├── services/
        ├── repositories/
        ├── entities/
        ├── dto/
        ├── requests/
        ├── resources/
        └── mappers/
```

**Giải thích 3 điểm quan trọng nhất — chỗ hay bị nhầm nhất:**

**1. `entities` vs `dto` vs `requests` vs `resources` — 4 thứ khác nhau, không thay thế được nhau**

```
Client gửi lên    → requests/    (LoginRequest — chỉ nhận input, có @Valid)
Đi trong hệ thống → dto/         (UserDto — truyền giữa Service ↔ Controller, không có JPA annotation)
Lưu xuống DB      → entities/    (User — có @Entity, @Column, KHÔNG bao giờ ra ngoài Controller)
Trả ra ngoài      → resources/   (UserResource — chỉ chứa field được phép client thấy)
```

Đây chính là lý do tôi hỏi ở lượt trước: `auth.user()` của bạn trả về `entity` hay `resource` — vì nếu trả thẳng
`entity` ra ngoài thì `passwordHash` sẽ bị leak.

**2. `mappers/` — tầng quan trọng hay bị bỏ qua:**

```java
// Không có mapper → developer bắt đầu viết convert thủ công ở khắp nơi
UserResource resource = new UserResource(
                user.getId(),
                user.getName(),
                user.getEmail()   // nếu sau này thêm field, phải tìm khắp project để sửa
        );

// Có mapper (dùng MapStruct) → 1 chỗ duy nhất định nghĩa mapping rule
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResource toResource(User entity);

    UserDto toDto(User entity);

    User toEntity(RegisterRequest request);
}
```

Dùng **MapStruct** thay vì tự viết convert thủ công — nó generate code lúc compile time (không dùng reflection như
ModelMapper), performance tốt hơn, và type-safe (lỗi mapping bị bắt ở compile time, không phải runtime).

**Cấu trúc `resources/` (static files):**

```
src/main/resources/
├── application.yml              # Config chung
├── application-local.yml        # Override cho local dev
├── application-staging.yml
├── application-prod.yml
└── logback-spring.xml           # Log config — print traceId vào mọi dòng log
```

Điểm quan trọng: không commit `application-prod.yml` lên git nếu chứa thông tin nhạy cảm — dùng biến môi trường hoặc
secret manager thay thế.

Nhìn vào codebase hiện tại của bạn, việc cần làm ngay là: chuyển `BlacklistServiceInterface` ra `shared/services`, và
thêm tầng `mappers/` để ngăn entity leak ra ngoài Controller. Hai thứ này là technical debt rõ ràng nhất đang tồn tại
trong cấu trúc hiện tại.