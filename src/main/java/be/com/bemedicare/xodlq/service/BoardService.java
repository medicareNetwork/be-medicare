package be.com.bemedicare.xodlq.service;

import be.com.bemedicare.member.dto.MemberDTO;
import be.com.bemedicare.member.repository.MemberRepository;
import be.com.bemedicare.xodlq.DTO.BoardDTO;
import be.com.bemedicare.xodlq.entity.Board;
import be.com.bemedicare.xodlq.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    private MemberRepository memberRepository;

    //글 작성
    public void write(Board board, MultipartFile file, MemberDTO member) throws IOException {

        System.out.println("board.getPrice() = " + board.getPrice());
        if(file!=null){
            handleFileUpload(board,file);
        }
        System.out.println("board.getContent() = " + board.getContent());

        board.setName(member.getMemberName());
        System.out.println("board.getTitle() = " + board.getTitle());

        boardRepository.save(board);
    }

    //글 수정
    public void modify(Board board, MultipartFile file) throws IOException{

        if(file!=null){
            deleteExistingFile(board);

            handleFileUpload(board, file);
        }

        boardRepository.save(board);
    }

    private void handleFileUpload(Board board, MultipartFile file) throws IOException {

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        UUID uuid = UUID.randomUUID();

        String fileName = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);

        board.setFilepath("/files/" + fileName);
    }

    public void deleteExistingFile(Board board) {
        File oriFile = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static" + board.getFilepath());
        if (oriFile.exists()) {
            oriFile.delete();
        }
    }

    //게시글 리스트 페이지
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    public Page<Board> boardCategoryList(String categoryKeyword, Pageable pageable){
        return boardRepository.findByCategoryContaining(categoryKeyword,pageable);
    }

    public Page<Board> boardCategorySearchList(String categoryKeyword, String searchKeyword, Pageable pageable){
        return boardRepository.findByCategoryContainingAndTitleContaining(categoryKeyword, searchKeyword, pageable);
    }

    //특정 게시글 불러오기
    public Board boardView(Long id){
        Board board=boardRepository.findById(id).orElse(null);
        viewsUp(board);

        return board;
    }

    //특정 게시글 삭제
    public void boardDelete(Board board){

        if(board.getFilepath()!=null){
            deleteExistingFile(board);
        }

        boardRepository.deleteById(board.getId());
    }

    //게시글 조회수 증가
    public void viewsUp(Board board){
        board.setViews(board.getViews()+1);
        boardRepository.save(board);
    }

    //조회수 높은순서대로 50개 불러오기
    public List<Board> bestList(){
        return boardRepository.findTop50ByOrderByViewsDesc();
    }
}
