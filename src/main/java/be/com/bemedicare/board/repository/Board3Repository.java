package be.com.bemedicare.board.repository;

import be.com.bemedicare.board.entity.Board3;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Board3Repository extends JpaRepository<Board3, Integer> {
    Page<Board3> findByTitleContaining(String searchKeyword, Pageable pageable);
}
