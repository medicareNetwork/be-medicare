package be.com.bemedicare.cart.repository;

import be.com.bemedicare.cart.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepositrory extends JpaRepository<Delivery,Long> {
}
