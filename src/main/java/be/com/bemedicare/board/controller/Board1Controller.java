package be.com.bemedicare.board.controller;

import be.com.bemedicare.board.entity.Board1;
import be.com.bemedicare.board.service.Board1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/board1")
@CrossOrigin(origins = "http://localhost:3000")
public class Board1Controller {

    @Autowired
    private Board1Service board1Service;

    @PostMapping("/write")
    public ResponseEntity<String> boardWritePro(@RequestParam("title") String title,
                                                @RequestParam("content") String content,
                                                @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Board1 board = new Board1();
            board.setTitle(title);
            board.setContent(content);
            board1Service.write(board, file);
            return new ResponseEntity<>("글 작성이 완료되었습니다", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("글 작성 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Board1>> boardList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                  @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Page<Board1> list;
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            list = board1Service.boardList(pageable);
        } else {
            list = board1Service.boardSearchList(searchKeyword, pageable);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Board1> boardView(@PathVariable("id") Long id) {
        Board1 board = board1Service.boardView(id);
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> boardDelete(@PathVariable("id") Long id) {
        try {
            board1Service.boardDelete(id);
            return new ResponseEntity<>("글이 삭제되었습니다", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("글 삭제 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> boardModify(@PathVariable("id") Long id,
                                              @RequestParam("title") String title,
                                              @RequestParam("content") String content,
                                              @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Board1 board = board1Service.boardView(id);
            if (board != null) {
                board.setTitle(title);
                board.setContent(content);
                board1Service.write(board, file);
                return new ResponseEntity<>("글 수정이 완료되었습니다", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 글을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("글 수정 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/firstpage")
    public ResponseEntity<String> mainPage() {
        return new ResponseEntity<>("This is the first page", HttpStatus.OK);
    }

    @GetMapping("/ai")
    public ResponseEntity<String> subPage() {
        return new ResponseEntity<>("This is the AI subpage", HttpStatus.OK);
    }
}
