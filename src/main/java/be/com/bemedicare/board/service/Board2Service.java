package be.com.bemedicare.board.service;

import be.com.bemedicare.board.entity.Board2;
import be.com.bemedicare.board.repository.Board2Repository;
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
public class Board2Service {

    @Autowired
    private Board2Repository board2Repository;

    // 글 작성 처리
    public Board2 write(Board2 board, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            handleFileUpload(board, file);
        }

        return board2Repository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board2> boardList(Pageable pageable) {
        return board2Repository.findAll(pageable);
    }

    // 게시물 검색
    public Page<Board2> boardSearchList(String searchKeyword, Pageable pageable) {
        return board2Repository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board2 boardView(Long id) {
        Optional<Board2> boardOptional = board2Repository.findById(id);
        if (boardOptional.isPresent()) {
            Board2 board = boardOptional.get();
            return board;
        } else {
            return null;  // 또는 예외를 던져 처리 가능
        }
    }

    // 특정 게시글 삭제
    public boolean boardDelete(Long id) {
        Optional<Board2> boardOptional = board2Repository.findById(id);
        if (boardOptional.isPresent()) {
            Board2 board = boardOptional.get();
            if (board.getFilepath() != null) {
                deleteExistingFile(board);
            }
            board2Repository.deleteById(id);
            return true;
        } else {
            return false; // 게시글이 존재하지 않음을 알림
        }
    }

    // 파일 업로드 처리
    private void handleFileUpload(Board2 board, MultipartFile file) throws IOException {
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files";
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
    }

    // 기존 파일 삭제 처리
    private void deleteExistingFile(Board2 board) {
        File oriFile = new File(System.getProperty("user.dir") + "/src/main/resources/static" + board.getFilepath());
        if (oriFile.exists()) {
            oriFile.delete();
        }
    }
}
