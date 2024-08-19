package be.com.bemedicare.board.controller;

import be.com.bemedicare.board.entity.Board2;
import be.com.bemedicare.board.service.Board2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class Board2Controller {

    @Autowired
    private Board2Service board2Service;

    @GetMapping("/board2/write")
    public String board2WriteForm() {
        return "boardwrite1";
    }

    @PostMapping("/board2/writepro")
    public String board2WritePro(Board2 board2, Model model, @RequestParam("file") MultipartFile file) throws Exception {
        board2Service.write(board2, file);
        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board2/list");
        return "message";
    }

    @GetMapping("/board2/list")
    public String board2List(Model model,
                             @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(name="searchKeyword", required = false) String searchKeyword) {

        Page<Board2> list;

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            list = board2Service.boardList(pageable);
        } else {
            list = board2Service.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int totalPages = list.getTotalPages();
        int startPage;
        int endPage;

        if (totalPages <= 10) {
            startPage = 1;
            endPage = totalPages;
        } else {
            if (nowPage <= 6) {
                startPage = 1;
                endPage = 10;
            } else {
                startPage = nowPage - 5;
                endPage = nowPage + 4;
                if (endPage > totalPages) {
                    endPage = totalPages;
                    startPage = totalPages - 9;
                }
            }
        }

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchKeyword", searchKeyword);

        return "boardlist1";
    }

    @GetMapping("/board2/view")
    public String boardView(Model model, @RequestParam(name="id") Integer id) {
        model.addAttribute("board", board2Service.boardView(id));
        return "boardview1"; // board1view.html 템플릿 파일이 있어야 합니다.
    }

    @GetMapping("/board2/delete")
    public String board2Delete(@RequestParam(name="id") Integer id) {
        board2Service.boardDelete(id);
        return "redirect:/board2/list";
    }

    @GetMapping("/board2/modify/{id}")
    public String board2Modify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", board2Service.boardView(id));
        return "boardmodify1";
    }

    @PostMapping("/board2/update/{id}")
    public String board2Update(@PathVariable("id") Integer id, Board2 board2, Model model, @RequestParam("file") MultipartFile file) throws Exception{
        try {
            Board2 board2Temp = board2Service.boardView(id);
            board2Temp.setTitle(board2.getTitle());
            board2Temp.setContent(board2.getContent());
            board2Service.write(board2Temp, file);
            model.addAttribute("message", "글 수정이 완료되었습니다");
            model.addAttribute("searchUrl", "/board2/list");
        } catch (Exception e) {
            model.addAttribute("message", "글 수정 중 오류가 발생했습니다.");
            model.addAttribute("searchUrl", "/board2/list");
            return "message";
        }
        return "message";
    }
}
