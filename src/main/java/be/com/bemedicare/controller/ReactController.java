package be.com.bemedicare.controller;

import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.service.MemberService;
import be.com.bemedicare.xodlq.DTO.BoardDTO;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ReactController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/list")
    public Page<Board> boardList(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return boardService.boardList(pageable);
    }

    @GetMapping("/bestList")
    public List<Board> boardList(HttpSession session){
        return boardService.bestList();
    }

    @PostMapping("/write")
    public ResponseEntity<String> boardWrite(@RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam("price") String price,
                                             @RequestParam("name") String name,
                                             @RequestParam("stockAmount") int stockAmount,
                                             @RequestParam(value = "file", required = false) MultipartFile file){
        Board board = new Board();
        MemberDTO memberDTO = new MemberDTO();

        board.setTitle(title);
        board.setContent(content);
        board.setPrice(Integer.parseInt(price));
        board.setStockAmount(stockAmount);

        memberDTO.setMemberName(name);

        try{
            boardService.write(board,file,memberDTO);
            System.out.println("등록성공");
            return new ResponseEntity<>("등록완료", HttpStatus.CREATED);
        }catch(Exception e){
            System.out.println("등록실패");
            return new ResponseEntity<>("등록실패" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/modify")
    public ResponseEntity<String> boardModify(@RequestParam("id") String id,
                                              @RequestParam("title") String title,
                                              @RequestParam("content") String content,
                                              @RequestParam("price") String price,
                                              @RequestParam(value = "file", required = false) MultipartFile file){
        Board board = boardService.boardView(Long.parseLong(id));

        board.setTitle(title);
        board.setContent(content);
        board.setPrice(Integer.parseInt(price));

        try{
            boardService.modify(board,file);
            return new ResponseEntity<>("수정 완료",HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>("수정 실패" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Data
    static class memberAddress{
        private String memberAddress;
    }
}
