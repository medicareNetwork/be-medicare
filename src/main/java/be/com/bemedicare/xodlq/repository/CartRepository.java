package be.com.bemedicare.xodlq.repository;

import be.com.bemedicare.xodlq.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByItemIdAndMemberId(Integer itemId, Integer memberId);
}
