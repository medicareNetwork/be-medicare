package be.com.bemedicare.cart.repository;

import be.com.bemedicare.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
