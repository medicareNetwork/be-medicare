package be.com.bemedicare.cart.query;

import be.com.bemedicare.cart.entity.CartStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    private Long cartId;
    private String name; //주문자 이름
    private LocalDateTime orderDate;
    private String address;
    private CartStatus cartStatus;

    private String itemName;
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    public OrderFlatDto(Long cartId, String name, LocalDateTime orderDate, String address, CartStatus cartStatus, String itemName, int orderPrice, int count) {
        this.cartId = cartId;
        this.name = name;
        this.orderDate = orderDate;
        this.address = address;
        this.cartStatus = cartStatus;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
