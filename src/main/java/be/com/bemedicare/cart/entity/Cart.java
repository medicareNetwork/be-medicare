package be.com.bemedicare.cart.entity;


import be.com.bemedicare.member.entity.MemberEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id")
    private MemberEntity member; //멤버테이블과 연결(참조하기)

    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    //장바구니 (진짜 장바구니처럼) 번호 만들기
    private List<CartItem> cartItems = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문일자

    @Enumerated(EnumType.STRING)
    private CartStatus status; // 주문상태 [order,cancel]

    //cart에서 setter메소드를 쓸 때는 이 값을 넣어라
    public void setMember(MemberEntity member) {
        this.member = member;
        member.getCart().add(this);
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setCart(this);
    }

    //주문 생성하기
    //cartItem은 배열로 해서 여러가지 상품을 담을 수 있다.
    public static Cart createCart(MemberEntity member, Delivery delivery, CartItem... cartItems) {
        Cart cart = new Cart();
        cart.setMember(member);
        cart.setDelivery(delivery);
        for(CartItem cartItem : cartItems) {
            cart.addCartItem(cartItem);
        }
        cart.setStatus(CartStatus.ORDER); //주문상태 주문
        cart.setOrderDate(LocalDateTime.now()); //주문시점 생성
        return cart;
    }

    //주문 취소하기
    public void orderCancel() {
        if(delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalArgumentException("해당 상품은 이미 배송중입니다.");
        }
        this.setStatus(CartStatus.CANCEL);
        for(CartItem cartItem : cartItems) {
            cartItem.cancel(); // 주문 취소 시 재고 반환을 위한 메서드
        }
    }
    //장바구니 주문 가격
    public int getTotalPirce() {
        int totalPrice = 0;
        for(CartItem cartItem : cartItems) {
            totalPrice += cartItem.getTotalPrice();
        }
        return totalPrice;
    }

}
