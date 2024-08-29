package be.com.bemedicare.board.service;

import be.com.bemedicare.board.entity.Board3;
import be.com.bemedicare.board.repository.Board3Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class Board3Service {

    @Autowired
    private Board3Repository board3Repository;

    // 글 작성 처리
    public Board3 write(Board3 board3, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            handleFileUpload(board3, file);
        }

        return board3Repository.save(board3);
    }

    // 게시글 리스트 처리
    public Page<Board3> boardList(Pageable pageable) {
        return board3Repository.findAll(pageable);
    }

    // 게시물 검색
    public Page<Board3> boardSearchList(String searchKeyword, Pageable pageable) {
        return board3Repository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board3 boardView(long id) {
        Optional<Board3> boardOptional = board3Repository.findById(id);
        return boardOptional.orElse(null);
    }

    // 특정 게시글 삭제
    public boolean boardDelete(long id) {
        Optional<Board3> boardOptional = board3Repository.findById(id);
        if (boardOptional.isPresent()) {
            Board3 board = boardOptional.get();
            if (board.getFilepath() != null) {
                deleteExistingFile(board);
            }
            board3Repository.deleteById(id);
            return true;
        } else {
            return false; // 게시글이 존재하지 않음을 알림
        }
    }

    // 파일 업로드 처리
    private void handleFileUpload(Board3 board3, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        board3.setFilename(fileName);
        board3.setFilepath("/files/" + fileName);
    }

    // 기존 파일 삭제 처리
    private void deleteExistingFile(Board3 board3) {
        File oriFile = new File(System.getProperty("user.dir") + "/src/main/resources/static" + board3.getFilepath());
        if (oriFile.exists()) {
            oriFile.delete();
        }
    }
}
