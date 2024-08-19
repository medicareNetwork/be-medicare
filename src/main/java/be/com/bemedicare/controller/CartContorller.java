package be.com.bemedicare.controller;

import be.com.bemedicare.cart.entity.Cart;
import be.com.bemedicare.cart.entity.CartItem;
import be.com.bemedicare.cart.service.CartService;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes
public class CartContorller {

    @Autowired
    private CartService cartService;
    @Autowired
    private BoardRepository boardRepository;

    private static final String CART_SESSION_KEY = "cartItems";

//    @GetMapping("/cart/add")
//    public String addToCart() {
//        return "redirect:/board/list";
//    }


    @PostMapping("/cart/add")
    public String addCart(@RequestParam("boardId") Long boardId,
                          @RequestParam("amount") int amount,
                          HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        Long cartItemId = (Long) session.getAttribute("cartItemId");
        cartService.addItemToCart(boardId,amount,session);
        return "redirect:/board/list";
    }

    @GetMapping("/cart/list")
    public String showCart(Model model, HttpSession session) {
        Map<Long, Integer> cartItems = cartService.getCartItems(session);
        int totalAmount = cartService.getTotalAmountAtCart(session);

        List<Board> boards = boardRepository.findAllById(cartItems.keySet());
        Map<Long, Board> boardMap = boards.stream().collect(Collectors.toMap(Board::getId, board -> board));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("boardMap", boardMap);
        model.addAttribute("totalAmount", totalAmount);
        return "cartlist";
    }

    @PostMapping("/order/complete")
    public String completeOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        try{
            cartService.completeOrder(session);
            redirectAttributes.addFlashAttribute("message", "Order completed");
        } catch(IllegalStateException e){
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/cart/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/order/complete";
    }
    @GetMapping("/order/complete")
    public String showOrderConfirmation() {
        return "confirm"; // 이것은 templates/order/confirm.html 파일을 렌더링합니다.
    }


    @PostMapping("/cart/remove")
    public String removeItemFromCart(@RequestParam("boardId") Long boardId, HttpSession session, RedirectAttributes redirectAttributes) {
        Map<Long, Integer> cartItems = (Map<Long, Integer>) session.getAttribute(CART_SESSION_KEY);

        if (cartItems != null) {
            cartItems.remove(boardId);
//            session.removeAttribute(CART_SESSION_KEY);
            session.setAttribute(CART_SESSION_KEY, cartItems);
        }

        redirectAttributes.addFlashAttribute("message", "상품이 장바구니에서 제거되었습니다.");

        // 장바구니 페이지로 리다이렉트
        return "redirect:/cart/list";
    }


}
