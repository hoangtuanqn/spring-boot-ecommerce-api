package mst.local.mstsoftware.modules.order.services.interfaces;

import mst.local.mstsoftware.modules.order.requests.CreateOrderRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;

public interface OrderServiceInterface {
    public OrderResource store(Long userId, CreateOrderRequest request);
}
