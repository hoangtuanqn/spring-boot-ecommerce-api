package mst.local.mstsoftware.modules.order.services.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.order.requests.AddCartItemRequest;
import mst.local.mstsoftware.modules.order.requests.CartItemResource;
import mst.local.mstsoftware.modules.order.requests.UpdateCartItemRequest;
import mst.local.mstsoftware.modules.order.resources.CartResource;
import mst.local.mstsoftware.modules.order.services.interfaces.CartServiceInterface;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CartService extends BaseService implements CartServiceInterface {

    private final RedisTemplate<String, Object> redis;

    private final ProductRepository productRepository;

    private static final String CART_PREFIX = "cart:";
    private static final long CART_TTL_DAYS = 7;


    @Override
    public CartResource getCart(Long userId) {
        String key = cartKey(userId);
        Map<Object, Object> entries = redis.opsForHash().entries(key);
        if (entries.isEmpty()) {
            return new CartResource(List.of(), BigDecimal.ZERO);
        }

        List<CartItemResource> items = entries.entrySet().stream().map(entry -> {
            Long productId = Long.valueOf(entry.getKey().toString());
            int quantity = Integer.parseInt(entry.getValue().toString());

            Product product = productRepository.findById(productId).orElse(null);
            if (product == null || product.getQuantity() < quantity) {
                redis.opsForHash().delete(key, entry.getKey());
                return null;
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

            return CartItemResource.builder()
                    .productId(productId)
                    .productTitle(product.getTitle())
                    .quantity(quantity)
                    .unitPrice(product.getPrice())
                    .subtotal(subtotal)
                    .build();
        }).filter(Objects::nonNull).toList();

        BigDecimal total = items.stream()
                .map(CartItemResource::subtotal).
                reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartResource.builder()
                .items(items)
                .total(total)
                .build();
    }

    @Override
    public CartResource addItem(Long userId, AddCartItemRequest request) {
        Product product = findOrThrow(productRepository.findById(request.productId()), "Không tìm thấy sản phẩm!");
        if (product.getQuantity() < request.quantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getTitle() + " đã hết hàng!");
        }
        String key = cartKey(userId);
        String field = request.productId().toString();

        Object existing = redis.opsForHash().get(key, field);
        int currentQty = existing != null ? Integer.parseInt(existing.toString()) : 0;
        int newQty = currentQty + request.quantity();

        if (newQty > product.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getTitle() + " không đủ số lượng!. Hiện tại còn lại " + (product.getQuantity() - currentQty) + " sản phẩm!");
        }
        redis.opsForHash().put(key, field, String.valueOf(newQty));
        redis.expire(key, Duration.ofDays(CART_TTL_DAYS));
        return getCart(userId);
    }

    @Override
    public CartResource updateItem(Long userId, Long productId, UpdateCartItemRequest request) {
        String key = cartKey(userId);
        String field = productId.toString();
        int quantity = request.quantity();
        Object existing = redis.opsForHash().get(key, field);
        if (existing == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Sản phẩm không có trong giỏ hàng!");
        }
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại!");
        }
        if (request.quantity() > product.getQuantity()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getTitle() + " không đủ số lượng!. Hiện tại còn lại " + (product.getQuantity()) + " sản phẩm!");
        }

        redis.opsForHash().put(key, field, String.valueOf(quantity));
        redis.expire(key, Duration.ofDays(CART_TTL_DAYS));
        return getCart(userId);
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        String key = cartKey(userId);
        Long deleted = redis.opsForHash().delete(key, productId.toString());
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không có trong giỏ hàng!");
        }
    }

    @Override
    public void removeItemMany(Long userId, List<Long> productIds) {
        String key = cartKey(userId);
        productIds.stream().forEach(product -> {
            redis.opsForHash().delete(key, product.toString());
        });
    }


    private String cartKey(Long userId) {
        return CART_PREFIX + userId;
    }
}
