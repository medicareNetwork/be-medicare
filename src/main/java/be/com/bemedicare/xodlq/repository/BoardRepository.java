package be.com.bemedicare.xodlq.repository;

import be.com.bemedicare.xodlq.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByCategoryContaining(String categoryKeyword, Pageable pageable);

    Page<Board> findByCategoryContainingAndTitleContaining(String categoryKeyword, String searchKeyword, Pageable pageable);

    List<Board> findTop50ByOrderByViewsDesc();
}
