package mst.local.mstsoftware.modules.order.services.impls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mst.local.mstsoftware.modules.order.requests.AddCartItemRequest;
import mst.local.mstsoftware.modules.order.requests.CartResource;
import mst.local.mstsoftware.modules.order.requests.UpdateCartItemRequest;
import mst.local.mstsoftware.modules.order.services.interfaces.CartServiceInterface;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.TimeUnit;

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
        return null;
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
                    HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getTitle() + " không đủ số lượng!");
        }
        redis.opsForHash().put(key, field, String.valueOf(newQty));
        redis.expire(key, CART_TTL_DAYS, TimeUnit.DAYS);
        return getCart(userId);
    }

    @Override
    public CartResource updateItem(Long userId, Long productId, UpdateCartItemRequest request) {
        return null;
    }

    @Override
    public void removeItem(Long userId, Long productId) {

    }

    @Override
    public void clearCart(Long userId) {
        redis.delete(cartKey(userId));
    }

    private String cartKey(Long userId) {
        return CART_PREFIX + userId;
    }
}
