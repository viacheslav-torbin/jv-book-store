package org.bookstore.service;

import java.util.List;
import org.bookstore.dto.order.CreateOrderRequestDto;
import org.bookstore.dto.order.OrderDto;
import org.bookstore.dto.order.OrderItemDto;
import org.bookstore.dto.order.UpdateOrderDto;
import org.bookstore.dto.order.UpdateOrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto orderDto);

    List<OrderDto> getAllOrders(Long userId, Pageable pageable);

    UpdateOrderResponseDto updateStatus(Long orderId, UpdateOrderDto orderDto);

    List<OrderItemDto> findAllItemsByOrder(Long userId, Long orderId);

    OrderItemDto findOrderItemById(Long orderId, Long orderItemId, Long userId);
}
