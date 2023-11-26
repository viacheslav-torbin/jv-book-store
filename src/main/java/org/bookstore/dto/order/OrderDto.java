package org.bookstore.dto.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
        Long id,
        Long userId,
        List<OrderItemDto> orderItems,
        String orderDate,
        BigDecimal total,
        String status) {
}
