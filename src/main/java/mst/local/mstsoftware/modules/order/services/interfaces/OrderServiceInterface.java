package mst.local.mstsoftware.modules.order.services.interfaces;

import mst.local.mstsoftware.modules.order.requests.CheckoutRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;

public interface OrderServiceInterface {
    public OrderResource checkout(Long userId, CheckoutRequest request);
}
