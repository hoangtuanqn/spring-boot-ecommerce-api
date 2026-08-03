package mst.local.mstsoftware.modules.order.services.impls;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mst.local.mstsoftware.helpers.QuerySpecBuilder;
import mst.local.mstsoftware.modules.order.entities.Order;
import mst.local.mstsoftware.modules.order.entities.OrderItem;
import mst.local.mstsoftware.modules.order.enums.OrderStatus;
import mst.local.mstsoftware.modules.order.mappers.OrderMapper;
import mst.local.mstsoftware.modules.order.repositories.OrderRepository;
import mst.local.mstsoftware.modules.order.requests.CancelOrderRequest;
import mst.local.mstsoftware.modules.order.requests.CartItemResource;
import mst.local.mstsoftware.modules.order.requests.CheckoutRequest;
import mst.local.mstsoftware.modules.order.resources.CartResource;
import mst.local.mstsoftware.modules.order.resources.OrderResource;
import mst.local.mstsoftware.modules.order.resources.OrderSummaryResource;
import mst.local.mstsoftware.modules.order.services.interfaces.CartServiceInterface;
import mst.local.mstsoftware.modules.order.services.interfaces.OrderServiceInterface;
import mst.local.mstsoftware.modules.product.entities.Product;
import mst.local.mstsoftware.modules.product.repositories.ProductRepository;
import mst.local.mstsoftware.modules.user.entities.User;
import mst.local.mstsoftware.modules.user.repositories.UserRepository;
import mst.local.mstsoftware.services.impl.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class OrderService extends BaseService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final CartServiceInterface cartService;
    private final QuerySpecBuilder specBuilder;

    @Override
    @Transactional
    public OrderResource checkout(Long userId, CheckoutRequest request) {
        CartResource cart = cartService.getCart(userId);
        User user = findOrThrow(userRepository.findById(userId), "Người dùng không tồn tại!");
        if (cart.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng trống!");
        }
        List<CartItemResource> selectedItems = cart.items().stream()
                .filter(item -> request.productIds().contains(item.productId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm đã chọn trong giỏ hàng!");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .note(request.note())
                .items(new ArrayList<>())
                .build();

        for (var cartItem : selectedItems) {
            Product product = findOrThrow(productRepository.findById(cartItem.productId()), "Không tìm thấy sản phẩm ID: " + cartItem.productId());
            if (product.getQuantity() < cartItem.quantity()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sản phẩm " + product.getTitle() + " không đủ số lượng!");
            }
            product.setQuantity(product.getQuantity() - cartItem.quantity());
            productRepository.save(product);

            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.quantity())
                    .unitPrice(product.getPrice())
                    .build();

            order.getItems().add(item);
        }

        BigDecimal total = order.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);
        request.productIds().forEach(productId -> cartService.removeItem(user.getId(), productId));
        return orderMapper.toResource(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResource cancel(Long userId, Long orderId, CancelOrderRequest note) {
        Order order = findOrThrow(orderRepository.findByIdAndUserId(orderId, userId), "Đơn hàng này của bạn không tồn tại!");
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ có thể huỷ đơn hàng ở trạng thái chờ xử lý!");
        }
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        });
        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResource(orderRepository.save(order));
    }

    @Override
    public Page<OrderSummaryResource> paginate(Long userId, Map<String, String[]> parameters) {
        Specification<Order> specs = specBuilder.buildSpecification(parameters);
        Pageable pageable = specBuilder.buildPageable(parameters);
        return orderRepository.findAll(specs, pageable).map(orderMapper::toSummary);
    }
}
