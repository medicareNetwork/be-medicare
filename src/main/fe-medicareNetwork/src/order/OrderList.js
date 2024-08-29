import React, { useEffect, useState } from 'react';
import axios from 'axios';

const OrderHistory = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handleCancelOrder = (item) => {

        const confirmCancel = window.confirm('정말 주문을 취소하겠습니까?');
        if (confirmCancel) {
        // 백엔드로 데이터 전송
        axios.post('http://localhost:8090/api/order/cancel', {
            cartId: item.cartId

        }, { withCredentials: true })
            .then(response => {
                console.log('주문이 취소되었습니다.');
                window.location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('주문 취소 요청에 실패했습니다.', error);
                alert('주문 취소 요청에 실패했습니다.');
            });
        } else {
            console.log("주문 취소가 취소되었습니다.")
        }
    }
    useEffect(() => {
        const member = JSON.parse(window.sessionStorage.getItem("member1"));

        if (!member) {
            setError(new Error('주문내역이 없습니다.'));
            setLoading(false);
            return;
        }

        axios.post(`http://localhost:8090/api/order/list2`, {}, { withCredentials: true }) // 회원의 주문 내역 API 호출
            .then(response => {
                setCartItems(response.data);
                setLoading(false);
            })
            .catch(err => {
                setError(err);
                setLoading(false);
            });
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div>
            <h1>{cartItems.length > 0 ? "주문 내역" : "주문 내역이 없습니다."}</h1>
            {cartItems.length > 0 && (
                <ul>
                    {cartItems.map((item, index) => (
                        <li key={index}>
                            <h2>상품 이름: {item.title || "정보 없음"}</h2>
                            <p>주문 ID: {item.cartId}</p>
                            <p>주문 날짜: {new Date(item.orderDate).toLocaleDateString()}</p> {/* 날짜 포맷팅 */}
                            <p>주문 상태: {item.cartStatus}</p>
                            {item.cartStatus !== 'CANCEL' && (<>
                            <p>배송 상태: {item.deliveryStatus}</p>
                            <p>배송 주소: {item.address || "정보 없음"}</p>
                            <p>수 취 인 : {item.name || "정보 없음"}</p>
                            <p>구매 수량: {item.count || "정보 없음"}</p>
                            <p>총합 가격: {item.totalPrice || "정보 없음"}</p></>)}
                            {item.cartStatus == 'ORDER' && item.deliveryStatus != 'SHIP' && (
                                <button onClick={() => handleCancelOrder(item)}>주문 취소</button>)}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OrderHistory;
