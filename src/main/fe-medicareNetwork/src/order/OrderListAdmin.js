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
    const handleDeliveryComplete = (item) => {
        alert('배달 완료');

        // 백엔드로 데이터 전송
        axios.post('http://localhost:8090/api/order/comp', {
            cartId: item.cartId

        }, { withCredentials: true })
            .then(response => {
                console.log('배달 완료 요청이 성공적으로 전송되었습니다.');
                window.location.reload(); // 페이지 새로고침
            })
            .catch(error => {
                console.error('배달 완료 요청에 실패했습니다.', error);
                alert('배달 완료 요청에 실패했습니다.');
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
        <div className="container my-5">
            <div className="text-bg-dark p-3" style={{ width: '65%', margin: '0 auto' }}>
                <h1 className="text-center mb-2">{cartItems.length > 0 ? "주문 내역" : "주문 내역이 없습니다."}</h1>
            </div>
            <div className="text-bg-light p-1"></div>

            {cartItems.length > 0 && (
                <table className="table table-dark table-striped" style={{ width: '65%', margin: '0 auto' }}>
                    <thead>
                    <tr>
                        <th scope="col" className="align-middle">상품 이름</th>
                        <th scope="col" className="text-start">주문 정보</th>
                        <th scope="col" className="text-end">액션</th>
                    </tr>
                    </thead>
                    <tbody>
                    {cartItems.map((item, index) => (
                        <tr key={index}>
                            <td className="align-middle">{item.title || "정보 없음"}</td>
                            <td className="text-start">
                                <p>주문 ID: {item.cartId}</p>
                                <p>주문 날짜: {new Date(item.orderDate).toLocaleDateString()}</p>
                                <p>주문 상태: {item.cartStatus}</p>
                                <p>배송 상태: {item.deliveryStatus}</p>
                                <p>배송 주소: {item.address || "정보 없음"}</p>
                                <p>수 취 인: {item.name || "정보 없음"}</p>
                                <p>구매 수량: {item.count || "정보 없음"}</p>
                                <p>총합 가격: {item.totalPrice || "정보 없음"}</p>
                            </td>
                            <td className="text-end">
                                {item.deliveryStatus === 'READY' && (
                                    <button
                                        className="btn btn-primary"
                                        onClick={() => handleDeliveryStart(item)}
                                    >
                                        배달 시작
                                    </button>
                                )}
                                {item.deliveryStatus === 'SHIP' && (
                                    <button
                                        className="btn btn-success"
                                        onClick={() => handleDeliveryComplete(item)}
                                    >
                                        배달 완료
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
export default OrderHistory;