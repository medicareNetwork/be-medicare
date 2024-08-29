package be.com.bemedicare.cart.entity;

import be.com.bemedicare.xodlq.entity.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cartItem_id")
    private Long id;

    private Long memberId;
//    private Long boardId;
    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="cart_id")
    private Cart cart;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
    private int totalPrice;

    //장바구니 생성하기 (board-제품 가져오고, 가격 가져오고, 갯수가져오고)
    public static CartItem createCartItem(Board board, int orderPrice, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setBoard(board);
        cartItem.setOrderPrice(orderPrice);
        cartItem.setCount(count);
        cartItem.setTotalPrice(orderPrice * count);

        board.removeStock(count);
        return cartItem;
    }

    public void cancel() {
        getBoard().addStock(count);
    }
    //주문 상품 다 합쳐서 얼마인지
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
