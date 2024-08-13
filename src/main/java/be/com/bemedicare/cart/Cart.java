package be.com.bemedicare.cart;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id; // 유저아이디
    private Long product_id; // 상품아이디
    private int amount; // 수량
    private int price; // 갯수

}
