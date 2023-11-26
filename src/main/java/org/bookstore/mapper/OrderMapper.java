package org.bookstore.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.bookstore.dto.order.OrderDto;
import org.bookstore.dto.order.UpdateOrderResponseDto;
import org.bookstore.model.CartItem;
import org.bookstore.model.Order;
import org.bookstore.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring",
        uses = OrderItemMapper.class,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    UpdateOrderResponseDto toUpdateDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total", source = "cart.cartItems", qualifiedByName = "total")
    @Mapping(target = "orderItems", source = "cart.cartItems")
    Order cartToOrder(ShoppingCart cart, String shippingAddress);

    @Mapping(target = "orderDate", dateFormat = "yyyy-MM-dd HH:mm")
    @Mapping(target = "userId", source = "user.id")
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtoList(List<Order> orders);

    @AfterMapping
    default void updateOrder(@MappingTarget Order order) {
        order.getOrderItems().forEach(oi -> oi.setOrder(order));
    }

    @Named("total")
    default BigDecimal getTotal(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
