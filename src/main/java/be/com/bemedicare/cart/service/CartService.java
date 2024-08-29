package be.com.bemedicare.cart.service;

import be.com.bemedicare.cart.entity.*;
import be.com.bemedicare.cart.repository.CartItemRepository;
import be.com.bemedicare.cart.repository.CartRepository;
import be.com.bemedicare.cart.repository.DeliveryRepositrory;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.repository.MemberRepository;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private static final String CART_SESSION_KEY = "cartItems";
    private final DeliveryRepositrory deliveryRepositrory;


    @Transactional
    public void addItemToCart(Long boardId,int amount, HttpSession session) {
        MemberEntity member = (MemberEntity) session.getAttribute("member");

        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

        if(cartItems == null) {
            cartItems = new HashMap<>();
//            session.setAttribute(MEMBER_SESSION_KEY, member);
            session.setAttribute(CART_SESSION_KEY, cartItems);
                    //엔티티 조회
        }
        //이미 장바구니에 있는 boardId면 수량을 더하게함
        cartItems.put(boardId, cartItems.getOrDefault(boardId, 0) + amount);
    }
    @Transactional
    public Map<Long, Integer> getCartItems(HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        if(cartItems == null) {
            cartItems = new HashMap<>();
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("cartItems: " + cartItems);
        return cartItems;
    }

    @Transactional
    public void completeOrder(HttpSession session) {
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

        System.out.println("member = " + member);
        System.out.println("cartItems = " + cartItems);

        if(member==null || cartItems==null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }

        List<CartItem> cartItemList = new ArrayList<>();
        for(Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long boardId = entry.getKey();
            int amount = entry.getValue();

            Board board = boardRepository.findById(boardId).orElse(null);
            if(board == null) {
                throw new IllegalArgumentException("유효하지 않은 상품  ID : " + boardId);
            }
            CartItem cartItem = CartItem.createCartItem(board, board.getPrice(), amount);
            cartItemList.add(cartItem);
        }

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getMemberAddress());
        delivery.setDeliveryStatus(DeliveryStatus.READY);

        MemberEntity memberEntity = memberRepository.findById(member.getId()).orElseThrow(
                ()-> new IllegalArgumentException(("유효하지 않은 회원 id"+member.getId())));
        Cart cart = Cart.createCart(memberEntity, delivery, cartItemList.toArray(new CartItem[0]));


        if(cart == null) {
            throw new IllegalArgumentException("카트가 텅 비어있습니다.");
        }else {

            cartRepository.saveAndFlush(cart);
        }
        cartRepository.saveAndFlush(cart);
        session.removeAttribute(CART_SESSION_KEY);


    }

    @Transactional
    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    @Transactional
    public int getTotalAmountAtCart(HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY); //세션을 통해서 등록된장바구니 가져오고
        if (cartItems == null) {
            return 0;
        }
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

    @Transactional
    public void shipDelivery(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        Delivery delivery = cart.getDelivery();
        if(delivery !=null){
            delivery.setDeliveryStatus(DeliveryStatus.SHIP);
            deliveryRepositrory.save(delivery);
        } else {
            throw new IllegalArgumentException("배달상태로 상태변경 실패");
        }
    }

    @Transactional
    public void completeDelivery(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();
        Delivery delivery = cart.getDelivery();

        if (delivery != null) {
            delivery.setDeliveryStatus(DeliveryStatus.COMP);
            deliveryRepositrory.save(delivery);
        } else {
            throw new IllegalArgumentException("배달완료로 상태변경 실패");
        }
    }

    @Transactional
    public void cancelOrder(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow();

        if(cart != null) {
            cart.setStatus(CartStatus.CANCEL);
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("주문취소로 상태변경 실패");
        }
    }



}
