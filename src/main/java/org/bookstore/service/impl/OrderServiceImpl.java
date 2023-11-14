package org.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bookstore.dto.order.CreateOrderRequestDto;
import org.bookstore.dto.order.OrderDto;
import org.bookstore.dto.order.OrderItemDto;
import org.bookstore.dto.order.UpdateOrderDto;
import org.bookstore.dto.order.UpdateOrderResponseDto;
import org.bookstore.exceptions.EntityNotFoundException;
import org.bookstore.mapper.OrderItemMapper;
import org.bookstore.mapper.OrderMapper;
import org.bookstore.model.Order;
import org.bookstore.model.OrderItem;
import org.bookstore.model.ShoppingCart;
import org.bookstore.repository.OrderItemRepository;
import org.bookstore.repository.OrderRepository;
import org.bookstore.repository.ShoppingCartRepository;
import org.bookstore.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto orderDto) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            // TODO: replace the exception
            throw new EntityNotFoundException("Cart is empty for user: " + userId);
        }
        Order order = orderMapper.cartToOrder(cart, orderDto.shippingAddress());
        cart.clear();
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId, Pageable pageable) {
        return orderMapper.toOrderDtoList(orderRepository.findAllByUserId(userId, pageable));
    }

    @Override
    public UpdateOrderResponseDto updateStatus(Long orderId, UpdateOrderDto orderDto) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with id: %d not found", orderId)
                ));
        order.setStatus(Order.Status.valueOf(orderDto.status()));
        order = orderRepository.save(order);
        return orderMapper.toUpdateDto(order);
    }

    @Override
    public List<OrderItemDto> findAllItemsByOrder(Long userId, Long orderId) {
        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order with id: %d not found for user: %d", orderId, userId)
                ));
        return orderItemMapper.toOrderItemDtoList(order.getOrderItems());
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long orderItemId, Long userId) {
        OrderItem item = orderItemRepository
                .findByIdAndOrderIdAndUserId(orderItemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Order item: %d not found in order: %d for user: %d",
                                orderItemId, orderId, userId)
                ));
        return orderItemMapper.toOrderItemDto(item);
    }
}
