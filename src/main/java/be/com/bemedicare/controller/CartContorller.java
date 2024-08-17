package be.com.bemedicare.controller;

import be.com.bemedicare.cart.service.CartService;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CartContorller {

    @Autowired
    private CartService cartService;
    @Autowired
    private BoardRepository boardRepository;


    @GetMapping("/cart/add")
    public String addToCart() {
        return "redirect:/board/list";
    }


    @PostMapping("/cart/add")
    public String addCart(@RequestParam("boardId") Long boardId,
                          @RequestParam("amount") int amount,
                          HttpSession session) {
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
    @PostMapping("/cart/remove")
    public String remove1Cart(HttpSession session) {
        cartService.clearCart(session);
        return "cartlist";
    }
    @GetMapping("/cart/remove")
    public String removeCart(HttpSession session) {
        cartService.clearCart(session);
        return "redirect:/cart/list";
    }


}
