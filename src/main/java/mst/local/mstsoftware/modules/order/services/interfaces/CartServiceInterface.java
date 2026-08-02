package mst.local.mstsoftware.modules.order.services.interfaces;

import mst.local.mstsoftware.modules.order.requests.AddCartItemRequest;
import mst.local.mstsoftware.modules.order.requests.CartResource;
import mst.local.mstsoftware.modules.order.requests.UpdateCartItemRequest;

import java.util.List;

public interface CartServiceInterface {
    CartResource getCart(Long userId);

    CartResource addItem(Long userId, AddCartItemRequest request);

    CartResource updateItem(Long userId, Long productId, UpdateCartItemRequest request);

    void removeItem(Long userId, Long productId);

    void removeItemMany(Long userId, List<Long> productIds);
}
