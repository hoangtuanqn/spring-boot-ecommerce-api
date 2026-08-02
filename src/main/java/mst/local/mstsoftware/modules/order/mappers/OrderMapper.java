package mst.local.mstsoftware.modules.order.mappers;

import mst.local.mstsoftware.mappers.CreateMapper;
import mst.local.mstsoftware.mappers.ReadMapper;
import mst.local.mstsoftware.modules.order.entities.Order;
import mst.local.mstsoftware.modules.order.requests.CreateOrderRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper extends CreateMapper<Order, CreateOrderRequest>, ReadMapper<Order, OrderResource> {
}
