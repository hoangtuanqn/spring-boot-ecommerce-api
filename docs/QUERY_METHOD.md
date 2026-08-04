## Spring Data JPA — Query Method Naming Convention

### Cấu trúc tổng quát

```
[action]By[Field][Condition][Connector][Field2][Condition2]...
```

---

### 1. Action prefix

| Prefix                        | Ý nghĩa                                 |
|-------------------------------|-----------------------------------------|
| `findBy` / `getBy` / `readBy` | SELECT, trả về entity / Optional / List |
| `existsBy`                    | SELECT, trả về `boolean`                |
| `countBy`                     | SELECT COUNT                            |
| `deleteBy` / `removeBy`       | DELETE                                  |

---

### 2. Condition keywords (gắn sau field)

| Keyword                 | SQL tương đương         |
|-------------------------|-------------------------|
| *(không có)*            | `= ?`                   |
| `Not`                   | `!= ?`                  |
| `Like`                  | `LIKE ?`                |
| `NotLike`               | `NOT LIKE ?`            |
| `StartingWith`          | `LIKE '?%'`             |
| `EndingWith`            | `LIKE '%?'`             |
| `Containing`            | `LIKE '%?%'`            |
| `In`                    | `IN (?)`                |
| `NotIn`                 | `NOT IN (?)`            |
| `IsNull` / `Null`       | `IS NULL`               |
| `IsNotNull` / `NotNull` | `IS NOT NULL`           |
| `GreaterThan`           | `> ?`                   |
| `GreaterThanEqual`      | `>= ?`                  |
| `LessThan`              | `< ?`                   |
| `LessThanEqual`         | `<= ?`                  |
| `Between`               | `BETWEEN ? AND ?`       |
| `True` / `False`        | `= true` / `= false`    |
| `Before` / `After`      | dùng cho Date/Timestamp |
| `IgnoreCase`            | `LOWER(col) = LOWER(?)` |
| `OrderBy...Asc/Desc`    | ORDER BY                |

---

### 3. Connector

- `And` → `AND`
- `Or` → `OR`

---

### Ví dụ thực tế

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // filter theo 1 field
    Optional<User> findByEmail(String email);

    List<User> findByStatus(String status);

    // kết hợp nhiều field
    List<User> findByStatusAndRole(String status, String role);

    Optional<User> findByEmailAndIsActiveTrue(String email);

    // LIKE / contains
    List<User> findByUsernameContaining(String keyword);

    List<User> findByEmailStartingWith(String prefix);

    // so sánh số / date
    List<User> findByAgeGreaterThan(int age);

    List<User> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // null check
    List<User> findByDeletedAtIsNull();

    List<User> findByVerifiedAtIsNotNull();

    // IN
    List<User> findByRoleIn(List<String> roles);

    // boolean shorthand
    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();

    // exists / count
    boolean existsByEmail(String email);

    long countByStatus(String status);

    // delete
    void deleteByStatus(String status);

    // kết hợp OrderBy
    List<User> findByStatusOrderByCreatedAtDesc(String status);

    // top / first — lấy N record đầu
    List<User> findTop5ByStatusOrderByCreatedAtDesc(String status);

    Optional<User> findFirstByEmailOrderByCreatedAtDesc(String email);
}
```

---

### Lưu ý quan trọng

**Field name phải khớp chính xác với tên field trong entity** (camelCase), không phải tên column DB.

```java
// Entity
@Column(name = "is_active")
private Boolean isActive;  // ← dùng tên này

// Repository → findByIsActiveTrue() ✓
//              findByIs_Active()    ✗
```

**Nested field** (join): dùng `_` hoặc camelCase để traverse

```java
// User có field: Address address; Address có field: String city
List<User> findByAddress_City(String city);

// hoặc
List<User> findByAddressCity(String city); // JPA tự resolve nếu không ambiguous
```

**Khi query phức tạp** hơn thì chuyển sang `@Query` thay vì cố nhồi vào method name — method name dài quá là dấu hiệu
nên dùng JPQL/native query rồi.