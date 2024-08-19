package be.com.bemedicare.controller;

import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ReactController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public Page<Board> boardList(@PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return boardService.boardList(pageable);
    }

    @GetMapping("/bestList")
    public List<Board> boardList(){
        return boardService.bestList();
    }
}
