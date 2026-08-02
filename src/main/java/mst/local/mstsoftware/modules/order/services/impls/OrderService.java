package mst.local.mstsoftware.modules.order.services.impls;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.modules.order.entities.Order;
import mst.local.mstsoftware.modules.order.entities.OrderItem;
import mst.local.mstsoftware.modules.order.enums.OrderStatus;
import mst.local.mstsoftware.modules.order.mappers.OrderMapper;
import mst.local.mstsoftware.modules.order.repositories.OrderRepository;
import mst.local.mstsoftware.modules.order.requests.CreateOrderRequest;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.services.interfaces.OrderServiceInterface;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class OrderService extends BaseService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResource store(Long userId, CreateOrderRequest request) {
        var user = findOrThrow(userRepository.findById(userId), "Người dùng không tồn tại trong hệ thống!");
        List<OrderItem> orderItems = request.items().stream().map(item -> {
            Product product = findOrThrow(productRepository.findById(item.productId()), "Không tìm thấy sản phẩm có id: " + item.productId());
            if (product.getQuantity() < item.quantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Sản phẩm " + product.getTitle() + " không đủ số lượng!");
            }

            product.setQuantity(product.getQuantity() - item.quantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .product(product)
                    .quantity(item.quantity())
                    .unitPrice(product.getPrice())
                    .build();
        }).toList();
        BigDecimal total = orderItems.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalPrice(total)
                .note(request.note())
                .items(orderItems)
                .build();
        return orderMapper.toResource(orderRepository.save(order));
    }
}
