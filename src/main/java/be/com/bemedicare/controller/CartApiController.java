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
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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
        System.out.println("요청이 잘 왔당께요 ");

        System.out.println(session.getAttribute("member"));
        MemberEntity member = (MemberEntity) session.getAttribute("member");

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AddCartResponse("MemberId가 없습니다, 로그인해주세요",false));
        } else {
            cartService.addItemToCart(addCartRequest.getBoardId(), addCartRequest.getAmount(), session);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AddCartResponse("장바구니에 담겼습니다",true));
        }

    }
    @Data
    static class AddCartRequest {
        private Long boardId;
        private int amount;
    }
    @Data
    static class AddCartResponse {
        private String message;
        private boolean success;

        public AddCartResponse(String message, boolean success) {
            this.message = message;
            this.success = success;
        }
    }
        //장바구니 열어보기
    @PostMapping("/cart/list")
    public ResponseEntity<ShowCartList> showCart(HttpSession session) {

        //세션에서 카트 아이템 가져오고
        Map<Long, Integer> cartItems = cartService.getCartItems(session);
        System.out.println("세션에 저장된 cartItems: " + cartItems);


        // 총 금액 계산하는 메서드 실행
        int totalAmount = cartService.getTotalAmountAtCart(session);


        //카트에 담긴 아이템 조회
        List<Board> boards = boardRepository.findAllById(cartItems.keySet());
        Map<Long, Board> boardMap = boards.stream().collect(Collectors.toMap(Board::getId, board -> board));

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
    public ResponseEntity<AddCartResponse> completeOrder(HttpSession session) {
        Boolean success = false;
           try{
            cartService.completeOrder(session);
             success = true;
            System.out.println("order 성공~");
            return ResponseEntity.status(HttpStatus.OK).body(new AddCartResponse("주문이 성공적으로 처리되었습니다",success));
        } catch(IllegalStateException e){
            System.out.println("오더실패 ~ 1번");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AddCartResponse("주문실패",success));
        } catch (Exception e) {
            System.out.println("오더실패 ~ 2번");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AddCartResponse("주문실패",success));
        }
    }

    //장바구니에서 item 한개씩 지우기
    @PostMapping("/cart/remove")
    public ResponseEntity<RemoveItemResponse> removeItemFromCart(@RequestBody RemoveItemRequest request, HttpSession session) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);
        Board board = new Board();
        board.setId(request.boardId);
        Board boardFind = boardRepository.findById(request.boardId).orElse(null);

        String itemName = boardFind.getTitle();
        boolean success = false;

        System.out.println("서버에서 확인용로그(지우는 아이템은? : "+itemName);

        if (cartItems != null) {
            cartItems.remove(request.boardId);
            session.setAttribute(CART_SESSION_KEY, cartItems);
            success = true;

        }
        return ResponseEntity.status(HttpStatus.OK).
                body(new RemoveItemResponse(
                        success,itemName," 상품이 장바구니에서 제거되었습니다."
                ));
    }
    @Data
    static class RemoveItemRequest {
        private Long boardId;
    }
    @Data
    static class RemoveItemResponse {
        private String message;
        private boolean success;
        private String itemName;

        public RemoveItemResponse(boolean success, String itemName,String message) {
            this.message = message;
            this.success = success;
            this.itemName = itemName;
        }
    }

    //장바구니 전부 비우기
    @PostMapping("/cart/clear")
    public ResponseEntity<?> clearCart(HttpSession session) {
        cartService.clearCart(session);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니가 비워졌습니다.");
    }

    //db에있는 모든 주문내역 출력 ( admin 전용 )
    @PostMapping("/order/list")
    public ResponseEntity<List<OrderQueryDto>> orderList() {


        List<OrderQueryDto> allOrderList = orderQueryRepository.findAllByDto();
        return ResponseEntity.status(HttpStatus.OK).body(allOrderList);

    }

    //개인 주문내역 확인
    @PostMapping("/order/list2")
    public ResponseEntity<List<OrderQueryDto>> orderList(HttpSession session) {
        MemberEntity member = (MemberEntity) session.getAttribute("member");

        List<OrderQueryDto> orderList = orderQueryRepository.findOneByDto(member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(orderList);

    }

    //postman전송할때 편하게 하려고 만든 테스트용
    @PostMapping("/order/list3")
    public ResponseEntity<List<OrderQueryDto>> orderList2(@RequestBody MemberEntity member) {

        List<OrderQueryDto> orderList = orderQueryRepository.findOneByDto(member.getId());

        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @PostMapping("/delivery/ship")
    public String shipOrder(@RequestBody Map<String, Long> request) {

        Long cartId = request.get("cartId");
        if(cartId != null) {
            cartService.shipDelivery(cartId);
        }
        return "배달이 시작됐씀당 CartId : " +cartId;
    }
    @PostMapping("/delivery/comp")
    public String compDelivery(@RequestBody Map<String, Long> request) {

        Long cartId = request.get("cartId");
        if(cartId != null) {
            cartService.completeDelivery(cartId);
        }
        return "배달이 완료되었습니다 ~ CartId : " + cartId;
    }


    @PostMapping("/order/cancel")
    public String cancelOrder(@RequestBody Map<String, Long> request) {

        Long cartId = request.get("cartId");
        if(cartId != null) {
            cartService.cancelOrder(cartId);
        }
        return "주문이 취소되었습니다. CartId : " +cartId;
    }

}
