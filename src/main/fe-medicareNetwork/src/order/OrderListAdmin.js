import React, { useEffect, useState } from 'react';
import axios from 'axios';

const OrderHistory = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handleDeliveryStart = (item) => {
        alert('배달 시작');

        // 백엔드로 데이터 전송
        axios.post('http://localhost:8090/api/order/ship', {
            cartId: item.cartId

        }, { withCredentials: true })
            .then(response => {
                console.log('배달 시작 요청이 성공적으로 전송되었습니다.');
                window.location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('배달 시작 요청에 실패했습니다.', error);
                alert('배달 시작 요청에 실패했습니다.');
            });
    }
    useEffect(() => {
        const member = JSON.parse(window.sessionStorage.getItem("member1"));

        if (!member) {
            setError(new Error('로그인이 필요합니다.'));
            setLoading(false);
            return;
        }

        axios.post(`http://localhost:8090/api/order/list`, {}, { withCredentials: true }) // 회원의 주문 내역 API 호출
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
                            <p>주문 날짜: {new Date(item.orderDate).toLocaleDateString()}</p>
                            <p>주문 상태: {item.cartStatus}</p>
                            <p>배송 상태: {item.deliveryStatus}</p>
                            <p>배송 주소: {item.address || "정보 없음"}</p>
                            <p>수 취 인 : {item.name || "정보 없음"}</p>
                            <p>구매 수량: {item.count || "정보 없음"}</p>
                            <p>총합 가격: {item.totalPrice || "정보 없음"}</p>
                            {item.deliveryStatus != 'SHIP' && (
                                <button onClick={() => handleDeliveryStart(item)}>배달 시작</button>)}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OrderHistory;
