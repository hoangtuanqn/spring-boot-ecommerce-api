package mst.local.mstsoftware.modules.order.mappers;

import mst.local.mstsoftware.mappers.CreateMapper;
import mst.local.mstsoftware.mappers.ReadMapper;
import mst.local.mstsoftware.modules.order.entities.Order;
import mst.local.mstsoftware.modules.order.requests.CheckoutRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.resources.OrderSummaryResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper extends CreateMapper<Order, CheckoutRequest>, ReadMapper<Order, OrderResource> {

    @Mapping(target = "itemCount", expression = "java(order.getItems().size())")
    OrderSummaryResource toSummary(Order order);
}
