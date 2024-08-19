package be.com.bemedicare.board.controller;


import be.com.bemedicare.board.entity.Board3;
import be.com.bemedicare.board.service.Board3Service;
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
public class Board3Controller {

    @Autowired
    private Board3Service board3Service;

    @GetMapping("/board3/write")
    public String board3WriteForm() {
        return "boardwrite2";
    }

    @PostMapping("/board3/writepro")
    public String board3WritePro(Board3 board3, Model model, @RequestParam("file") MultipartFile file) throws Exception {
        board3Service.write(board3, file);
        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board3/list");
        return "message";
    }

    @GetMapping("/board3/list")
    public String board3List(Model model,
                             @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(name="searchKeyword", required = false) String searchKeyword) {

        Page<Board3> list;

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            list = board3Service.boardList(pageable);
        } else {
            list = board3Service.boardSearchList(searchKeyword, pageable);
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

        return "boardlist2";
    }

    @GetMapping("/board3/view")
    public String boardView(Model model, @RequestParam(name="id") Integer id) {
        model.addAttribute("board", board3Service.boardView(id));
        return "boardview2"; // board1view.html 템플릿 파일이 있어야 합니다.
    }

    @GetMapping("/board3/delete")
    public String board3Delete(@RequestParam(name="id") Integer id) {
        board3Service.boardDelete(id);
        return "redirect:/board3/list";
    }

    @GetMapping("/board3/modify/{id}")
    public String board3Modify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", board3Service.boardView(id));
        return "boardmodify2";
    }

    @PostMapping("/board3/update/{id}")
    public String board3Update(@PathVariable("id") Integer id, Board3 board3, Model model, @RequestParam("file") MultipartFile file) throws Exception{
        try {
            Board3 board3Temp = board3Service.boardView(id);
            board3Temp.setTitle(board3.getTitle());
            board3Temp.setContent(board3.getContent());
            board3Service.write(board3Temp, file);
            model.addAttribute("message", "글 수정이 완료되었습니다");
            model.addAttribute("searchUrl", "/board3/list");
        } catch (Exception e) {
            model.addAttribute("message", "글 수정 중 오류가 발생했습니다.");
            model.addAttribute("searchUrl", "/board3/list");
            return "message";
        }
        return "message";
    }
}
