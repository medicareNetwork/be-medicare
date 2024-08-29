package be.com.bemedicare.cart.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long cartId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long cartId, String itemName, int orderPrice, int count) {
        this.cartId = cartId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }


}
