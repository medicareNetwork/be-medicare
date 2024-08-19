package be.com.bemedicare.board.repository;

import be.com.bemedicare.board.entity.Board2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Board2Repository extends JpaRepository<Board2, Integer> {
    Page<Board2> findByTitleContaining(String searchKeyword, Pageable pageable);
}
