package be.com.bemedicare.email.service;

import be.com.bemedicare.cart.entity.Cart;
import be.com.bemedicare.cart.entity.CartItem;
import be.com.bemedicare.cart.entity.Delivery;
import be.com.bemedicare.cart.repository.CartRepository;
import be.com.bemedicare.member.entity.MemberEntity;
import be.com.bemedicare.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    public void sendOrderEmail(Cart cart) {

        MemberEntity member = cart.getMember();
        Delivery delivery = cart.getDelivery();
        List<CartItem> cartItems = cart.getCartItems();

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("안녕하세요, ").append(member.getMemberName()).append("님!\n");
        emailContent.append("주문이 완료되었습니다.\n\n");
        emailContent.append("배송지: ").append(delivery.getAddress()).append("\n\n");
        emailContent.append("주문 내역:\n");

        for(CartItem cartItem : cartItems) {
            emailContent.append(" - ")
                    .append(cartItem.getBoard().getTitle())
                    .append(") - 가격 : ")
                    .append(cartItem.getOrderPrice())
                    .append("\n");
        }
        emailContent.append("주문해주셔서 감사합니다 !");


        if(member.getId() !=null ) {
//            if(member.getEmail() !=null ) {

            try{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("goottflix@gmail.com");
                message.setTo("yoohwanjoo@nate.com");
                message.setSubject("감사합니다. Medicare 상품 주문이 완료되었습니다.");
                message.setText(emailContent.toString());

                mailSender.send((message));
                System.out.println("이메일 전송 성공!!!!");
            } catch (MailException e){
                System.out.println("이메일 전송 실패"+e.toString());
                e.printStackTrace();
            }
        }else {
            throw new IllegalArgumentException("이메일주소가 없는 유저입니다.");
        }

    }

}
