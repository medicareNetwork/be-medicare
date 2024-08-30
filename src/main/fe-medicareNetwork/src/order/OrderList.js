import React, { useEffect, useState } from 'react';
import axios from 'axios';

const OrderHistory = () => {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handleCancelOrder = (item) => {
        const confirmCancel = window.confirm('정말 주문을 취소하겠습니까?');
        if (confirmCancel) {
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
    };

    useEffect(() => {
        const member = JSON.parse(window.sessionStorage.getItem("member1"));

        if (!member) {
            setError(new Error('주문내역이 없습니다.'));
            setLoading(false);
            return;
        }

        axios.post(`http://localhost:8090/api/order/list2`, {}, { withCredentials: true })
            .then(response => {
                const sortedCartItems = response.data.sort((a, b) => a.cartId - b.cartId);
                setCartItems(sortedCartItems);
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
            <div className="text-bg-dark p-3" style={{width: '70%', margin: '0 auto'}}>
                <h1 className="text-center mb-2">{cartItems.length > 0 ? "주문 내역" : "주문 내역이 없습니다."}</h1>
            </div>
            <div className="text-bg-light p-1"></div>

            {cartItems.length > 0 && (
                <table className="table table-dark table-striped" style={{width: '70%', margin: '0 auto'}}>
                    <thead>
                    <tr>
                        <th scope="col" className="align-middle">상품 이미지</th>
                        <th scope="col" className="align-middle">상품 이름</th>
                        <th scope="col" className="text-start">주문 정보</th>
                        <th scope="col" className="text-end">주문 취소</th>
                    </tr>
                    </thead>
                    <tbody>
                    {cartItems.map((item, index) => (
                        <tr key={index}>
                            <td className="align-middle">
                                <img
                                    src={item.filepath ? item.filepath : '/files/img_ready.png'}
                                    alt={item.filename}
                                    style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                                    className="rounded"
                                />
                            </td>
                            <td className="align-middle">{item.title || "정보 없음"}</td>
                            <td className="text-start">
                                <p>주문 ID: {item.cartId}</p>
                                <p>주문 날짜: {new Date(item.orderDate).toLocaleDateString()}</p>
                                <p>주문 상태: {item.cartStatus}</p>
                                {item.cartStatus !== 'CANCEL' && (
                                    <>
                                        <p>배송 상태: {item.deliveryStatus}</p>
                                        <p>배송 주소: {item.address || "정보 없음"}</p>
                                        <p>수취인: {item.name || "정보 없음"}</p>
                                        <p>구매 수량: {item.count || "정보 없음"}</p>
                                        <p>총합 가격: {item.totalPrice || "정보 없음"}</p>
                                    </>
                                )}
                            </td>
                            <td className="text-end">
                                {item.cartStatus === 'ORDER' && item.deliveryStatus !== 'SHIP' && item.deliveryStatus !== 'COMP' && (
                                    <button
                                        className="btn btn-danger"
                                        onClick={() => handleCancelOrder(item)}
                                    >
                                        주문 취소
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
