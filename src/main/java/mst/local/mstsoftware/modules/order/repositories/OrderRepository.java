package mst.local.mstsoftware.modules.order.repositories;

import mst.local.mstsoftware.modules.order.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

    Optional<Order> findByCodeAndUserId(String code, Long userId);
}
