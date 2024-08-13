package be.com.bemedicare.board.controller;


import be.com.bemedicare.board.entity.Board1;
import be.com.bemedicare.board.entity.Board2;
import be.com.bemedicare.board.service.Board1Service;
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
public class Board1Controller {

    @Autowired
    private Board1Service board1Service;


    @GetMapping("/board1/write" )
    public String boardWriteForm() {

        return "board1write";

    }

    @PostMapping("/board1/writepro")
    public String boardWritePro(Board1 board, Model model, @RequestParam(name="file") MultipartFile file) throws Exception{
        try {
            System.out.println(file);
            board1Service.write(board, file);
            model.addAttribute("message", "글 작성이 완료되었습니다");
            model.addAttribute("searchUrl", "/board1/list");
        } catch (Exception e) {
            e.printStackTrace();  // 예외 발생 시 콘솔에 출력
            model.addAttribute("message", "글 작성 중 오류가 발생했습니다.");
            model.addAttribute("searchUrl", "/board1/write");
            return "message";
        }
        return "message";
    }

    @GetMapping("/board1/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                            @RequestParam(name="searchKeyword", required = false) String searchKeyword) {

        Page<Board1> list;

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            list = board1Service.boardList(pageable);
        } else {
            list = board1Service.boardSearchList(searchKeyword, pageable);
        }


        int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하므로 +1)
        int totalPages = list.getTotalPages(); // 전체 페이지 수

        int startPage;
        int endPage;

        if (totalPages <= 10) {
            // 전체 페이지 수가 10 이하일 경우, 모든 페이지를 표시
            startPage = 1;
            endPage = totalPages;
        } else {
            // 현재 페이지가 1~6일 경우, 시작 페이지를 1로 고정
            if (nowPage <= 6) {
                startPage = 1;
                endPage = 10;
            } else {
                // 현재 페이지가 7 이상일 경우, 현재 페이지를 중심으로 10개씩 표시
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

        return "board1list";
    }


    @GetMapping("/board1/view")
    public String boardView(Model model, @RequestParam(name="id") Integer id) {
        model.addAttribute("board", board1Service.boardView(id));
        return "board1view";
    }
    @GetMapping("/board1/delete")
    public String boardDelete(@RequestParam(name="id") Integer id) {

        board1Service.boardDelete(id);
        return "redirect:/board1/list";
    }

    @GetMapping("/board1/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", board1Service.boardView(id));
        return "board1modify";
    }

    @PostMapping("/board1/update/{id}")
    public String board2Update(@PathVariable("id") Integer id, Board2 board2, Model model, @RequestParam("file") MultipartFile file) throws Exception {
        try {
            Board1 boardTemp = board1Service.boardView(id);
            boardTemp.setTitle(board2.getTitle());
            boardTemp.setContent(board2.getContent());
            board1Service.write(boardTemp, file);
            model.addAttribute("message", "글 수정이 완료되었습니다");
            model.addAttribute("searchUrl", "/board1/list");
        } catch (Exception e) {
            model.addAttribute("message", "글 수정 중 오류가 발생했습니다.");
            model.addAttribute("searchUrl", "/board1/list");
            return "message";
        }
        return "message";
    }
    @GetMapping("/board1/firstpage")
    public String mainPage() {
        return "firstpage";
    }

    @GetMapping("/board1/ai")
    public String subPage() {
        return "ai";
    }
}


