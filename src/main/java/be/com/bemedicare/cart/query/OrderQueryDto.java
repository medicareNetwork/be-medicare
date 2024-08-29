package be.com.bemedicare.cart.query;

import be.com.bemedicare.cart.entity.CartStatus;
import be.com.bemedicare.cart.entity.DeliveryStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of ="cartId")
public class OrderQueryDto {

    private Long cartId;
    private String name;
    private LocalDateTime orderDate;
    private CartStatus cartStatus;
    private String address;
    private String title;
    private int count;
    private int totalPrice;
    private DeliveryStatus deliveryStatus;
    private List<OrderItemQueryDto> cartItems;

    public OrderQueryDto(Long cartId, String name, LocalDateTime orderDate, CartStatus cartStatus, String address) {
        this.cartId = cartId;
        this.name = name;
        this.orderDate = orderDate;
        this.cartStatus = cartStatus;
        this.address = address;
    }
    public OrderQueryDto(Long cartId, String name, LocalDateTime orderDate, CartStatus cartStatus, String address, List<OrderItemQueryDto> cartItems) {
        this.cartId = cartId;
        this.name = name;
        this.orderDate = orderDate;
        this.cartStatus = cartStatus;
        this.address = address;
        this.cartItems = cartItems;
    }

    public OrderQueryDto(Long cartId, String name, LocalDateTime orderDate, CartStatus cartStatus,
                         String address, String title, int count, int totalPrice, DeliveryStatus deliveryStatus) {
        this.cartId = cartId;
        this.name = name;
        this.orderDate = orderDate;
        this.cartStatus = cartStatus;
        this.address = address;
        this.title = title;
        this.count = count;
        this.totalPrice = totalPrice;
        this.deliveryStatus = deliveryStatus;
    }
}
