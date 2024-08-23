package be.com.bemedicare.controller;

import be.com.bemedicare.cart.query.OrderFlatDto;
import be.com.bemedicare.cart.query.OrderQueryDto;
import be.com.bemedicare.cart.query.OrderQueryRepository;
import be.com.bemedicare.cart.service.CartService;
import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.MemberService;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartApiController {


    private final CartService cartService;
    private final BoardRepository boardRepository;

    private static final String CART_SESSION_KEY = "cartItems";


    private final MemberService memberService;
    private final OrderQueryRepository orderQueryRepository;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.save(memberDTO);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("오류"+e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                   HttpSession session) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(loginRequest.email);
        memberEntity.setMemberPassword(loginRequest.password);
        try {
            MemberEntity loggedin = memberService.login(memberEntity);
            // login메서드 자체가 email을 가지고 해당 entity전체를 조회하는것이기 때문에 새로 정의한 MemberEntity를 세션에 저장하는것!!!!
            session.setAttribute("loggedin", loggedin);
            System.out.println(memberEntity.getMemberEmail()+memberEntity.getMemberPassword());
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(loginRequest.email));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null)); //

        }
    }
    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }
    @Data
    static class LoginResponse {
        private String email;

        public LoginResponse(String email) {
            this.email = email;
        }
    }


    //장바구니에 담기
    @PostMapping("/cart/add")
    public ResponseEntity<AddCartResponse> addCart(@RequestBody @Valid AddCartRequest addCartRequest,
                                  HttpSession session) {

        System.out.println(session.getAttribute("loggedin"));
        MemberEntity member = (MemberEntity) session.getAttribute("loggedin");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AddCartResponse("MemberId가 없습니다, 로그인해주세요"));
        } else {
            cartService.addItemToCart(addCartRequest.getBoardId(), addCartRequest.getAmount(), session);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AddCartResponse("장바구니에 담겼습니다"));
        }

    }
    @Data
    static class AddCartRequest {
        private Long boardId;
        private int amount;
    }
    @Data
    static class AddCartResponse {
        String message;

        public AddCartResponse(String message) {
            this.message = message;
        }
    }

    //장바구니 열어보기
    @PostMapping("/cart/list")
    public ResponseEntity<ShowCartList> showCart(HttpSession session) {
        // 세션에 저장된 속성들 확인
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            System.out.println("세션 속성 이름: " + attributeName + ", 값: " + attributeValue);
        }

        //세션에서 카트 아이템 가져오고
        Map<Long, Integer> cartItems = cartService.getCartItems(session);
        System.out.println("세션에 저장된 cartItems: " + cartItems);


        // 총 금액 계산하는 메서드 실행
        int totalAmount = cartService.getTotalAmountAtCart(session);


        //카트에 담긴 아이템 조회
        List<Board> boards = boardRepository.findAllById(cartItems.keySet());
        Map<Long, Board> boardMap = boards.stream().collect(Collectors.toMap(Board::getId, board -> board));
        System.out.println("cartItems = " + cartItems);
        System.out.println("boardMap = " + boardMap);
        System.out.println("totalAmount = " + totalAmount);

        ShowCartList showCartList = new ShowCartList(cartItems,boardMap,totalAmount);
        showCartList.setCartItems(cartItems);
        showCartList.setBoardMap(boardMap);
        showCartList.setTotalAmount(totalAmount);
        System.out.println("showCartList = " + showCartList);

        return ResponseEntity.status(HttpStatus.OK).body(new ShowCartList(cartItems,boardMap,totalAmount));
    }
    @Data
    static class ShowCartList{
        private Map<Long, Integer> cartItems;
        private Map<Long, Board> boardMap;
        private int totalAmount;

        public ShowCartList(Map<Long, Integer> cartItems, Map<Long, Board> boardMap, int totalAmount) {
            this.cartItems = cartItems;
            this.boardMap = boardMap;
            this.totalAmount = totalAmount;
        }
    }

    //주문완료 버튼
    @PostMapping("/order/complete")
    public ResponseEntity<?> completeOrder(HttpSession session) {
        try{
            cartService.completeOrder(session);
            return ResponseEntity.status(HttpStatus.OK).body("주문완료");
        } catch(IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //장바구니에서 item 한개씩 지우기
    @PostMapping("/cart/remove")
    public ResponseEntity<?> removeItemFromCart(@RequestBody RemoveItemRequest request, HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        Board board = new Board();
        board.setId(request.boardId);
        Board boardFind = boardRepository.findById(request.boardId).orElse(null);
        String itemName = boardFind.getTitle();
        System.out.println(itemName);
        if (cartItems != null) {
            cartItems.remove(request.boardId);
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }
        return ResponseEntity.status(HttpStatus.OK).body(itemName+" 상품이 장바구니에서 제거되었습니다.");
    }
    @Data
    static class RemoveItemRequest {
        private Long boardId;
    }

    //장바구니 전부 비우기
    @PostMapping("/cart/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        cartService.clearCart(session);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 비워졌습니다.");
    }

    //db에있는 모든 주문내역 출력 ( admin 전용 )
    @PostMapping("/order/list")
    public List<OrderQueryDto> orderList() {
        return orderQueryRepository.findAllByDto();

    }

    //이대로는 안쓸거임, 개인 주문내역 찾기할때 쓰는거 만드는중
    @PostMapping("/order/list2")
    public List<OrderQueryDto> orderList(@RequestBody MemberEntity memberEntity) {

        return orderQueryRepository.findOneByDto(memberEntity.getId());

    }

}
