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
        <div style={{ padding: '20px', maxWidth: '600px', margin: '0 auto', fontFamily: 'Arial, sans-serif' }}>
            {member ? (
                <div>
                    <h1 style={{ color: '#333', textAlign: 'center', marginBottom: '20px' }}>마이페이지</h1>
                    <div style={{ backgroundColor: '#f9f9f9', padding: '20px', borderRadius: '10px', boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)' }}>
                        <h2 style={{ borderBottom: '2px solid #ddd', paddingBottom: '10px', marginBottom: '20px' }}>회원 정보</h2>
                        <p><strong>나이:</strong> {member.memberAge}</p>
                        <p><strong>몸무게:</strong> {member.memberWeight}</p>
                        <p><strong>신장:</strong> {member.memberHeight}</p>
                        <p><strong>핸드폰 번호:</strong> {member.memberNumber}</p>
                        <p><strong>주소:</strong> {member.memberAddress}</p>

                    </div>
                </div>
            ) : (
                <div style={{ textAlign: 'center', color: '#888', marginTop: '50px' }}>정보 불러오는중입니다...</div>
            )}
            {member && (
                <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
                    <button
                        onClick={handleMemberUpdate}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#4CAF50',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontWeight: 'bold'
                        }}
                    >
                        개인정보 수정
                    </button>
                    <button
                        onClick={handlePasswordChange}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#FF5722',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontWeight: 'bold'
                        }}
                    >
                        비밀번호 수정
                    </button>
                </div>
            )}
        </div>
    );
};

export default MyPage;
