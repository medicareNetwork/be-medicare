package be.com.bemedicare.board.service;


import be.com.bemedicare.board.entity.Board2;
import be.com.bemedicare.board.repository.Board2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class Board2Service {

    @Autowired
    private Board2Repository board2Repository;

    public void write(Board2 board2, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        board2.setFilename1(fileName);
        board2.setFilepath1("/files/" + fileName);

        board2Repository.save(board2);
    }

    public Page<Board2> boardList(Pageable pageable) {
        return board2Repository.findAll(pageable);
    }

    public Page<Board2> boardSearchList(String searchKeyword, Pageable pageable) {
        return board2Repository.findByTitleContaining(searchKeyword, pageable);
    }

    public Board2 boardView(Integer id) {
        return board2Repository.findById(id).orElse(null);
    }

    public void boardDelete(Integer id) {
        board2Repository.deleteById(id);
    }
}
