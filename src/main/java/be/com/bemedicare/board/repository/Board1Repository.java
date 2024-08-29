package be.com.bemedicare.board.repository;

import be.com.bemedicare.board.entity.Board1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Board1Repository extends JpaRepository<Board1, Long> {

Page<Board1> findByTitleContaining(String searchKeyword, Pageable pageable);


}
