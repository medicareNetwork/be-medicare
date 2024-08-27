import React, { useState } from 'react';
import {useNavigate} from "react-router-dom";

import axios from 'axios';

function OrderHistory() {
    const [cartItems, setCartItems] = useState([]);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const fetchOrderHistory = () => {
        axios.post('http://localhost:8090/api/order/list2', {}, { withCredentials: true })
            .then(response => {
                if (response.status === 200) {
                    setCartItems(response.data);
                } else {
                    setMessage('주문 내역을 불러오는 데 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error fetching order history:', error);
                setMessage('서버와의 통신 중 오류가 발생했습니다.');
            });
    };

    return (
        <div>
            <h1>주문 내역</h1>
            <button onClick={fetchOrderHistory}>주문 내역 확인</button>
            {message && <div>{message}</div>}
            <table>
                <thead>
                <tr>
                    <th>장바구니 번호</th>
                    <th>배송 정보</th>
                    <th>주문 날짜</th>
                    <th>주문 상태</th>
                </tr>
                </thead>
                <tbody>
                {cartItems.map((cart, index) => (
                    <tr key={index}>
                        <td>{cart.cartId}</td>
                        <td>{cart.deliveryInfo}</td>
                        <td>{cart.orderDate}</td>
                        <td>{cart.orderStatus}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderHistory;
