import React, { useState, useEffect } from 'react';
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import { CartPlusFill } from "react-bootstrap-icons";

function Cart() {
    const [cartItems, setCartItems] = useState([]); // 빈 배열로 초기화
    const [totalAmount, setTotalAmount] = useState(0);
    const [message, setMessage] = useState('');
    const [updateTrigger, setUpdateTrigger] = useState(false); // 업데이트 트리거 상태


    const navigate = useNavigate();

    const handleCartClick = () => {
        navigate('/cart'); // navigate로 이동 처리
    };

    useEffect(() => {
        // 장바구니 데이터를 API에서 받아오는 부분
        fetch('http://localhost:8090/api/cart/list', {
            method: 'POST',
            credentials: 'include',// API 엔드포인트로 POST 요청
        })
            .then(response => {
                console.log('Response:', response); // 응답 객체 확인
                return response.json(); // JSON으로 변환
            })
            .then(data => {
                console.log('Data:', data); // 변환된 JSON 데이터 확인

                if (!data.cartItems || !data.boardMap) {
                    console.error('Invalid data format:', data);
                    setMessage('잘못된 데이터 형식입니다.');
                    return;
                }

                const items = Object.entries(data.cartItems).map(([boardId, quantity]) => {
                    const board = data.boardMap[boardId];
                    if (!board) {
                        console.error(`No board found for boardId: ${boardId}`);
                        return null; // 잘못된 데이터가 있는 경우
                    }
                    return {
                        boardId: Number(boardId),
                        title: board.title,
                        filepath: board.filepath,
                        filename: board.filename,
                        price: board.price,
                        quantity: quantity
                    };
                }).filter(item => item !== null); // null 값 제거

                console.log('Items:', items); // 매핑된 아이템들 확인
                setCartItems(items); // 변환된 items 배열 설정
                setTotalAmount(data.totalAmount || 0);
            })
            .catch(error => {
                console.error('Error:', error); // 에러 발생 시 에러 로그 출력
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
                console.error('Error:', error);
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
                console.log('Order completion response:', data); // 서버 응답을 로그로 출력하여 확인

                if (data.success) {
                    setMessage('주문이 완료되었습니다.');
                    setCartItems([]); // 주문 완료 후 장바구니 비우기
                    setTotalAmount(0);
                    alert("주문이 성공적으로 처리되었습니다.");

                    navigate('/');
                } else {
                    setMessage('주문을 완료하는데 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error during order completion:', error);
                setMessage('서버와의 통신 중 오류가 발생했습니다.');
            });
    };


    return (
        <div className="container mt-5">
            <div className="text-bg-dark p-3"><CartPlusFill size={25} />  장바구니  </div>
            {message && <div>{message}</div>}
            <table className="table table-striped table-hover">
                <thead className="thead-dark">
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
                        <td>
                            <img src={item.filepath ? item.filepath : '/files/img_ready.png'} alt={item.filename}/>
                        </td>
                        <td className="align-middle">{item.title || '상품 정보 없음'}</td>
                        <td className="align-middle">{item.quantity}</td>
                        <td className="align-middle">{item.price}</td>
                        <td className="align-middle">{item.price * item.quantity}</td>
                        <td className="align-middle">
                            <button onClick={() => removeItem(item.boardId)}>삭제</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <h2>총액: {totalAmount}원</h2>
            <button onClick={() => navigate('/')}>쇼핑 계속하기</button>
            <button onClick={completeOrder}>주문완료하기</button>
        </div>
    );
}

export default Cart;
