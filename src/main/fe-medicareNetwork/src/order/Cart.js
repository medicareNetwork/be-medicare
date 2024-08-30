import React, { useState, useEffect } from 'react';
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import { CartPlusFill } from "react-bootstrap-icons";

function Cart() {
    const [cartItems, setCartItems] = useState([]); // 빈 배열로 초기화
    const [totalAmount, setTotalAmount] = useState(0); // 총액을 별도로 저장할 상태 추가
    const [message, setMessage] = useState('');
    const [updateTrigger, setUpdateTrigger] = useState(false); // 업데이트 트리거 상태

    const navigate = useNavigate();

    useEffect(() => {
        // 장바구니 데이터를 API에서 받아오는 부분
        fetch('http://localhost:8090/api/cart/list', {
            method: 'POST',
            credentials: 'include',// API 엔드포인트로 POST 요청
        })
            .then(response => {
                return response.json(); // JSON으로 변환
            })
            .then(data => {
                if (!data.cartItems || !data.boardMap) {
                    setMessage('잘못된 데이터 형식입니다.');
                    return;
                }

                const items = Object.entries(data.cartItems).map(([boardId, quantity]) => {
                    const board = data.boardMap[boardId];
                    if (!board) {
                        return null; // 잘못된 데이터가 있는 경우
                    }
                    return {
                        boardId: Number(boardId),
                        title: board.title,
                        filepath: board.filepath,
                        filename: board.filename,
                        price: board.price,
                        quantity: quantity,
                    };
                }).filter(item => item !== null); // null 값 제거

                setCartItems(items); // 변환된 items 배열 설정
                setTotalAmount(data.totalAmount || 0); // totalAmount를 상태로 설정
            })
            .catch(error => {
                setMessage('장바구니 데이터를 불러오는데 실패했습니다.');
            });
    }, [updateTrigger]); // 빈 배열을 두 번째 인자로 전달하여 컴포넌트 마운트 시 한 번만 실행

    const removeItem = (boardId) => {
        fetch('http://localhost:8090/api/cart/remove', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ boardId })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                setMessage(data.message); // 서버에서 보낸 메시지를 표시
                setUpdateTrigger(prev => !prev); // 트리거 상태를 변경하여 useEffect 다시 실행
            })
            .catch(error => {
                setMessage('아이템을 삭제하는데 실패했습니다.');
            });
    };

    const completeOrder = () => {
        fetch('http://localhost:8090/api/order/complete', {
            method: 'POST',
            credentials: 'include',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    setMessage('주문이 완료되었습니다.');
                    setCartItems([]); // 주문 완료 후 장바구니 비우기
                    setTotalAmount(0); // 총액 초기화
                    alert("주문이 성공적으로 처리되었습니다.");
                    navigate('/');
                } else {
                    setMessage('주문을 완료하는데 실패했습니다.');
                }
            })
            .catch(error => {
                setMessage('서버와의 통신 중 오류가 발생했습니다.');
            });
    };

    return (
        <div className="container mt-5">
            <div className="text-bg-dark p-3 mb-3 rounded">
                <CartPlusFill size={25} /> 장바구니
            </div>
            {message && <div className="alert alert-info">{message}</div>}
            <table className="table table-striped table-hover table-dark">
                <thead>
                <tr>
                    <th>상품이미지</th>
                    <th>상품명</th>
                    <th>수량</th>
                    <th>가격</th>
                    <th>총액</th>
                    <th>삭제</th>
                </tr>
                </thead>
                <tbody>
                {cartItems.map((item) => (
                    <tr key={item.boardId}>
                        <td className="align-middle">
                            <img
                                src={item.filepath ? item.filepath : '/files/img_ready.png'}
                                alt={item.filename}
                                style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                                className="rounded"
                            />
                        </td>
                        <td className="align-middle">{item.title || '상품 정보 없음'}</td>
                        <td className="align-middle">{item.quantity}</td>
                        <td className="align-middle">{item.price.toLocaleString('ko-KR')}원</td>
                        <td className="align-middle">{(item.price * item.quantity).toLocaleString('ko-KR')}원</td>
                        <td className="align-middle">
                            <button className="btn btn-danger btn-sm" onClick={() => removeItem(item.boardId)}>
                                삭제
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className="d-flex justify-content-between mt-4">
                <h2>총액: {totalAmount.toLocaleString('ko-KR')}원</h2> {/* totalAmount를 표시 */}
                <div>
                    <button className="btn btn-outline-dark me-2" onClick={() => navigate('/')}>
                        쇼핑 계속하기
                    </button>

                    <button className="btn btn-success" onClick={completeOrder}>
                        주문완료하기
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Cart;
