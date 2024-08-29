package be.com.bemedicare.cart.query;

import be.com.bemedicare.cart.entity.CartStatus;
import be.com.bemedicare.cart.entity.Delivery;
import be.com.bemedicare.cart.entity.DeliveryStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCompleteDto {

    private Long cartItemId;
    private DeliveryStatus deliveryStatus;
    private CartStatus cartStatus;

    public OrderCompleteDto(Long cartItemId, DeliveryStatus deliveryStatus, CartStatus cartStatus) {
        this.cartItemId = cartItemId;
        this.deliveryStatus = deliveryStatus;
        this.cartStatus = cartStatus;
    }
}
