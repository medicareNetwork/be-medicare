package be.com.bemedicare.controller;

import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.service.MemberService;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.entity.Cart;
import be.com.bemedicare.xodlq.service.BoardService;
import be.com.bemedicare.xodlq.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "index";
    }


    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CartService cartService;

    @GetMapping("/board/write")
    public String boardWrite(){
        return "boardwrite";
    }


    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, @RequestParam(name="file") MultipartFile file, HttpSession session) throws IOException {
        boardService.write(board,file, (MemberDTO) session.getAttribute("member"));

        return "redirect:/board/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "searchKeyword", required = false, defaultValue = "") String searchKeyword,
                            @RequestParam(name = "categoryKeyword", required = false, defaultValue = "") String categoryKeyword){

        Page<Board> list = null;

        if(searchKeyword.isEmpty() && categoryKeyword.isEmpty()){
            list = boardService.boardList(pageable);
        }else if(categoryKeyword.isEmpty() && !searchKeyword.isEmpty()){
            list = boardService.boardSearchList(searchKeyword, pageable);
        }else if(!categoryKeyword.isEmpty() && searchKeyword.isEmpty()){
            list = boardService.boardCategoryList(categoryKeyword, pageable);
        }else if(!categoryKeyword.isEmpty() && !searchKeyword.isEmpty()){
            list = boardService.boardCategorySearchList(categoryKeyword, searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list",list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam(name="id") Long id){

        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam(name="id") Long id){

        Board board = boardService.boardView(id);

        boardService.boardDelete(board);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model) throws IOException{
        Board board = boardService.boardView(id);

        if(board.getFilename()!=null){
            String oriName = board.getFilename().split("_")[1];
            model.addAttribute("oriName", oriName);
        }

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, Board board, @RequestParam(name="file") MultipartFile file, HttpSession session) throws IOException{

        Board boardTemp = boardService.boardView(id);

        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());
        boardTemp.setCategory(board.getCategory());

        boardService.modify(boardTemp, file, (MemberDTO) session.getAttribute("member"));

        return "redirect:/board/list";
    }

//    @GetMapping("/member2/create")
//    public String member(){
//
//        return "membercreate";
//    }
//
//    @PostMapping("/member2/create")
//    public String memberCreate(@ModelAttribute MemberDTO member){
//
//        System.out.println(member);
//
//        memberService.(member);
//
//        return "redirect:/board/list";
//    }
//
//    @GetMapping("/member2/login")
//    public String memberLogin(){
//        return "memberlogin";
//    }

//    @PostMapping("/member2/login")
//    public String checkLogin(@ModelAttribute Member member, HttpSession session){
//        Optional<Member> loginResult = memberService.loginMember(member);
//
//        if(loginResult.isPresent()){
//            session.setAttribute("member", loginResult.get());
//            return "redirect:/board/list";
//        }else{
//            return "memberlogin";
//        }
//    }
//
//    @GetMapping("/member2/")
//    public String findMemberAll(){
//        List<Member> memberList = memberService.findAll();
//
//        return "index";
//    }

    @PostMapping("/cart/contain")
    public String cartContain(Cart cart,
                              @RequestParam(name = "list_back", required = false) String list_back,
                              @RequestParam(name = "cart_back", required = false) String cart_back){

        cartService.save(cart);

        String direct = "";

        if(list_back!=null&&cart_back==null){
            direct="redirect:/board/list";
        }else if(cart_back!=null&&list_back==null){
            direct="redirect:/cart/list";
        }

        return direct;
    }

}
