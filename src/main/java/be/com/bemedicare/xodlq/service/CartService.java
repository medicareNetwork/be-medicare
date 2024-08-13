package be.com.bemedicare.xodlq.service;

import be.com.bemedicare.xodlq.entity.Cart;
import be.com.bemedicare.xodlq.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void save(Cart cart){
        Optional<Cart> fCart = cartRepository.findByItemIdAndMemberId(cart.getItemId(), cart.getMemberId());

        if(!fCart.isEmpty()){
            System.out.println(fCart.get().getItemId());

            fCart.get().setAmount(fCart.get().getAmount()+cart.getAmount());

            cartRepository.save(fCart.get());
        }else {
            cartRepository.save(cart);
        }
    }
}
