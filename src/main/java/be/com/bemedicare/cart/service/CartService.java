package be.com.bemedicare.cart.service;

import be.com.bemedicare.cart.entity.*;
import be.com.bemedicare.cart.repository.CartItemRepository;
import be.com.bemedicare.cart.repository.CartRepository;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.repository.MemberRepository;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CartItemRepository cartItemRepository;
    private static final String CART_SESSION_KEY = "cartItems";
    private static final String MEMBER_SESSION_KEY = "member";


//    @GetMapping("/order")
//    public String showOrderForm(Model model, HttpSession session) {
//        MemberDTO loggedInMember = (MemberDTO) session.getAttribute("member");
//        List<ItemDTO> items = itemService.getAllItems();

//        model.addAttribute("loggedInMember", loggedInMember);
//        model.addAttribute("items", items);

    @Transactional
    public void addItemToCart(Long boardId,int amount, HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

        if(cartItems == null) {
            cartItems = new HashMap<>();
            session.setAttribute(MEMBER_SESSION_KEY, member);
            session.setAttribute(CART_SESSION_KEY, cartItems);
                    //엔티티 조회
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
    public void completeOrder(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

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
            throw new IllegalArgumentException("카트가 null이다 이새기야");
        }else {

            cartRepository.saveAndFlush(cart);
        }
//        cartRepository.save(cart);
        cartRepository.saveAndFlush(cart);


        session.removeAttribute(CART_SESSION_KEY);



    }
//    @Transactional
//    public String placeOrder(HttpSession session, RedirectAttributes redirectAttributes) {
//        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
//        Map<Long, Board> boardMap = (Map<Long, Board>) session.getAttribute("boardMap");
//        Long memberId = (Long) session.getAttribute("memberId");
//
//        if (cartItems != null && boardMap != null && memberId != null) {
//            for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
//                Long boardId = entry.getKey();
//                int quantity = entry.getValue();
//                Board board = boardMap.get(boardId);
//
//                // 새로운 Cart 엔티티를 생성하여 주문 정보를 저장합니다.
//                CartItem order = new CartItem();
//                order.setMemberId(memberId);
//                order.setBoard(boardId);
//                order.setItemName(board.getTitle());
//                order.setCount(amount);
//                order.setOrderPrice();
//
//
//                // CartRepository를 사용하여 데이터베이스에 저장합니다.
//                cartRepository.save(order);
//
//
//        }
//    }

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
