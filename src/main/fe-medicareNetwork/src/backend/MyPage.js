import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const MyPage = () => {
    const [member, setMember] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);


    const navigate = useNavigate();


    const handleMemberUpdate = () => {
        navigate('/update', {state: {member}});
    }

    const handlePasswordChange = () => {
        navigate('/passwordChange')
    }

    const handleOrderList = () => {
        navigate('/order/OrderList');
    }

    useEffect(() => {
        axios.get('http://localhost:8090/api/member/mypage', { withCredentials: true }) // 쿠키를 포함한 요청을 보낼 경우
            .then(response => {
                setMember(response.data);
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
        <div className="container" style={{ padding: '20px', maxWidth: '450px', margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
            <div>
                {member ? (
                    <div>
                        <h1 className="text-dark text-center mb-3">마이페이지</h1>
                        <div className="text-end mb-4">
                            <button
                                className="btn btn-dark"
                                onClick={handleOrderList}
                            >
                                주문 내역 보기
                            </button>
                        </div>
                        <div className="bg-light p-4 rounded shadow-sm">
                            <h2 className="border-bottom pb-2 mb-4">회원 정보</h2>
                            <p><strong>나이:</strong> {member.memberAge}</p>
                            <p><strong>몸무게:</strong> {member.memberWeight}</p>
                            <p><strong>신장:</strong> {member.memberHeight}</p>
                            <p><strong>핸드폰 번호:</strong> {member.memberNumber}</p>
                            <p><strong>주소:</strong> {member.memberAddress}</p>
                        </div>
                    </div>
                ) : (
                    <div className="text-center text-muted mt-5">정보 불러오는중입니다...</div>
                )}
                {member && (
                    <div className="d-flex justify-content-between mt-4">
                        <button
                            onClick={handleMemberUpdate}
                            className="btn btn-success"
                        >
                        개인정보 수정
                        </button>
                        <button
                            onClick={handlePasswordChange}
                            className="btn btn-danger"
                        >
                            비밀번호 수정
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default MyPage;