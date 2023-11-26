package org.bookstore.dto.order;

public record UpdateOrderResponseDto(
        Long orderId,
        String status) {
}
