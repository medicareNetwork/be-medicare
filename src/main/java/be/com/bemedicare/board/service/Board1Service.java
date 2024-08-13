package be.com.bemedicare.board.service;

import be.com.bemedicare.board.entity.Board1;
import be.com.bemedicare.board.repository.Board1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class Board1Service {

    @Autowired
    private Board1Repository board1Repository;

    // 글 작성 처리
    public void write(Board1 board, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        board1Repository.save(board);


    }
    // 게시글 리스트 처리
    public Page<Board1> boardList(Pageable pageable) {
        return board1Repository.findAll(pageable);
    }
    // 게시물 검색
    public Page<Board1> boardSearchList(String searchKeyword, Pageable pageable) {

        return board1Repository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board1 boardView(Integer id) {
        return board1Repository.findById(id).get();
    }

    //특정 게시글 삭제

    public void boardDelete(Integer id) {
        board1Repository.deleteById(id);
    }
}