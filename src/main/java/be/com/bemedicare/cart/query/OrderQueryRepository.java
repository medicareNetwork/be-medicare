package be.com.bemedicare.cart.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new be.com.bemedicare.cart.query.OrderQueryDto " +
                        " (c.id, m.memberName, c.orderDate, c.status, d.address)" +
                        " from Cart c" +
                        " join c.member m" +
                        " join c.delivery d",OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderQueryDto> findMemberCarts(Long memberId) {
        return em.createQuery(
                        "select new be.com.bemedicare.cart.query.OrderQueryDto " +
                                " (c.id, m.memberName, c.orderDate, c.status, d.address)" +
                                " from Cart c" +
                                " join c.member m" +
                                " join c.delivery d" +
                                " where m.id = :memberId",OrderQueryDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long cartId) {
        return em.createQuery(
                        "select new be.com.bemedicare.cart.query.OrderItemQueryDto " +
                                " (ci.cart.id, b.name, ci.orderPrice, ci.count)" +
                                " from CartItem ci" +
                                " join ci.board b" +
                                " where ci.cart.id = : cartId", OrderItemQueryDto.class)
                .setParameter("cartId", cartId)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto() {

        //루트 조회
        List<OrderQueryDto> result = findOrders();

        //cartItem 컬렉션을 MAP한번에 조회
        Map<Long, List<OrderItemQueryDto>> cartItemMap = findCartItemMap(toCartIds(result));

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행x)
        result.forEach( c-> c.setCartItems(cartItemMap.get(c.getCartId())));

        return result;
    }
    public List<OrderQueryDto> findOneByDto(Long memberId) {

        //루트 조회
        List<OrderQueryDto> result = findMemberCarts(memberId);

        //cartItem 컬렉션을 MAP한번에 조회
        Map<Long, List<OrderItemQueryDto>> cartItemMap = findCartItemMap(toCartIds(result));

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행x)
        result.forEach( c-> c.setCartItems(cartItemMap.get(c.getCartId())));

        return result;
    }

    private List<Long> toCartIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(c-> c.getCartId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findCartItemMap(List<Long> cartIds) {
        List<OrderItemQueryDto> cartItems = em.createQuery(
                "select new be.com.bemedicare.cart.query.OrderItemQueryDto" +
                        " (ci.cart.id, b.name, ci.orderPrice, ci.count)" +
                        " from CartItem ci" +
                        " join ci.board b" +
                        " where ci.cart.id in : cartIds", OrderItemQueryDto.class)
                .setParameter("cartIds", cartIds)
                .getResultList();

        return cartItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getCartId));

    }
//    private Map<Long, List<OrderItemQueryDto>> findMemberMap(List<Long> memberId) {
//        List<OrderItemQueryDto> cartItems = em.createQuery(
//                        "select new be.com.bemedicare.cart.query.OrderItemQueryDto" +
//                                " (ci.cart.id, b.name, ci.orderPrice, ci.count)" +
//                                " from CartItem ci" +
//                                " join ci.board b" +
//                                " where ci.cart.id in : cartIds", OrderItemQueryDto.class)
//                .setParameter("memberId", memberId)
//                .getResultList();
//
//        return cartItems.stream()
//                .collect(Collectors.groupingBy(OrderItemQueryDto::getCartId));
//
//    }

}
