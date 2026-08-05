Rate Limiting trong Spring Boot
Các approach phổ biến
Approach Khi nào dùng
Bucket4j (in-memory)    App đơn, đơn giản, nhanh
Bucket4j + Redis Multi-instance, distributed
Resilience4j RateLimiter Đã dùng Resilience4j rồi
Spring Cloud Gateway API Gateway layer

Oke, Bucket4j Filter thuần trước — hiểu xong mới thêm Redis.

---

### 1. Dependency

```xml

<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.10.1</version>
</dependency>
```

---

### 2. Filter

```java

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1))))
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(ip, k -> newBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                        {"success": false, "message": "Quá nhiều request, thử lại sau"}
                    """);
        }
    }
}
```

Test bằng cách đổi `20` thành `2`, bắn 3 request là thấy 429 ngay.

---

### 3. Thêm Redis khi đã hiểu

Thêm dependency:

```xml
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-redis</artifactId>
    <version>8.10.1</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

Sửa `newBucket()` thành lấy từ Redis:

```java

@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, byte[]> redisTemplate;

    private Bucket resolveBucket(String ip) {
        ProxyManager<String> proxyManager = Bucket4jRedis
                .casBasedBuilder(redisTemplate)
                .build();

        BucketConfiguration config = BucketConfiguration.builder()
                .addLimit(Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1))))
                .build();

        return proxyManager.builder().build("rate_limit:" + ip, config);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                        {"success": false, "message": "Quá nhiều request, thử lại sau"}
                    """);
        }
    }
}
```

---

### Điểm khác biệt duy nhất

```
// Before — in-memory
Map<String, Bucket> buckets = new ConcurrentHashMap<>();
buckets.computeIfAbsent(ip, k -> newBucket());

// After — Redis
ProxyManager<String> proxyManager = Bucket4jRedis.casBasedBuilder(redisTemplate).build();
proxyManager.builder().build("rate_limit:" + ip, config);
```

Logic filter không đổi gì — chỉ thay chỗ lưu bucket. Đó là lý do học Filter trước cho dễ hiểu.