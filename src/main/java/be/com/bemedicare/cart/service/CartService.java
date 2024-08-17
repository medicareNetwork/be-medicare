package be.com.bemedicare.cart.service;

import be.com.bemedicare.cart.entity.*;
import be.com.bemedicare.cart.repository.CartRepository;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.repository.MemberRepository;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private static final String CART_SESSION_KEY = "cartItems";

    @Transactional
    public void addItemToCart(Long boardId,int amount, HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

        if(cartItems == null) {
            cartItems = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }
        //이미 장바구니에 있는 boardId면 수량을 더하게함)
        cartItems.put(boardId, cartItems.getOrDefault(boardId, 0) + amount);
    }
    @Transactional
    public Map<Long, Integer> getCartItems(HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if(cartItems == null) {
            cartItems = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }
        return cartItems;
    }

    @Transactional
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    @Transactional
    public int getTotalAmountAtCart(HttpSession session) {
        Map<Long, Integer> cartItems = getCartItems(session); //세션을 통해서 등록된장바구니 가져오고
        int totalAmount = 0; //총액값 초기화하고

       for(Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
           Long boardId = entry.getKey();
           int amount = entry.getValue();

           Board board = boardRepository.findById(boardId).orElse(null);
           if(board != null) {
               totalAmount += board.getPrice() * amount;
           }
       }
       return totalAmount;
    }



//    @Transactional
//    public Long order(Long memberId, Long boardId, int count) {
//
//
//        //엔티티 조회
//        MemberEntity member = memberRepository.findById(memberId).orElse(null);
//        Board board = boardRepository.findAllById(boardId);
//
//        //배송정보 생성
//        Delivery delivery = new Delivery();
//        delivery.setAddress(member.getMemberAddress());//배송지 가져오기
//        delivery.setDeliveryStatus(DeliveryStatus.READY);
//
//        //주문 상품 생성
//        CartItem cartItem = CartItem.createCartItem(board, board.getPrice(), count);
//
//        //주문 생성 여기서 리턴값 : cart
//        Cart cart = Cart.createCart(member,delivery,cartItem);
//
//        //주문 저장
//        cartRepository.save(cart);
//
//        return cartItem.getId();
//    }
//
//    @Transactional
//    public void cancelOrder(Long cartId) {
//        //주문 엔티티 조회
//        Cart cart = cartRepository.findOne(cartId);
//        //주문 취소
//        cart.orderCancel();
//    }

}
