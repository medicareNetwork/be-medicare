package be.com.bemedicare.board.service;



import be.com.bemedicare.board.entity.Board3;
import be.com.bemedicare.board.repository.Board3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class Board3Service {

    @Autowired
    private Board3Repository board3Repository;

    public void write(Board3 board3, MultipartFile file) throws Exception {
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        board3.setFilename2(fileName);
        board3.setFilepath2("/files/" + fileName);

        board3Repository.save(board3);
    }

    public Page<Board3> boardList(Pageable pageable) {
        return board3Repository.findAll(pageable);
    }

    public Page<Board3> boardSearchList(String searchKeyword, Pageable pageable) {
        return board3Repository.findByTitleContaining(searchKeyword, pageable);
    }

    public Board3 boardView(Integer id) {
        return board3Repository.findById(id).orElse(null);
    }

    public void boardDelete(Integer id) {
        board3Repository.deleteById(id);
    }
}
