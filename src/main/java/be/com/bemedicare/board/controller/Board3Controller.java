package be.com.bemedicare.board.controller;

import be.com.bemedicare.board.entity.Board3;
import be.com.bemedicare.board.service.Board3Service;
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
@RequestMapping("/api/board3")
@CrossOrigin(origins = "http://localhost:3000")
public class Board3Controller {

    @Autowired
    private Board3Service board3Service;

    @PostMapping("/write")
    public ResponseEntity<String> boardWritePro(@RequestParam("title") String title,
                                                @RequestParam("content") String content,
                                                @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Board3 board = new Board3();
            board.setTitle(title);
            board.setContent(content);
            board3Service.write(board, file);
            return new ResponseEntity<>("글 작성이 완료되었습니다", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("글 작성 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Board3>> boardList(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                                  @RequestParam(value = "searchKeyword", required = false) String searchKeyword) {
        Page<Board3> list;
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            list = board3Service.boardList(pageable);
        } else {
            list = board3Service.boardSearchList(searchKeyword, pageable);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Board3> boardView(@PathVariable("id") Long id) {
        Board3 board = board3Service.boardView(id);
        if (board != null) {
            return new ResponseEntity<>(board, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> boardDelete(@PathVariable("id") Long id) {
        try {
            board3Service.boardDelete(id);
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
            Board3 board = board3Service.boardView(id);
            if (board != null) {
                board.setTitle(title);
                board.setContent(content);
                board3Service.write(board, file);
                return new ResponseEntity<>("글 수정이 완료되었습니다", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("해당 글을 찾을 수 없습니다", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("글 수정 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
