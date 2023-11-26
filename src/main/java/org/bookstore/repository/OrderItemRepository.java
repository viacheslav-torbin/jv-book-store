package org.bookstore.repository;

import java.util.Optional;
import org.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = """
            FROM OrderItem item
            JOIN FETCH item.order order
            WHERE item.id = :orderItemId AND order.id = :orderId AND order.user.id = :userId""")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("orderItemId") Long orderItemId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("userId") Long userId);
}
