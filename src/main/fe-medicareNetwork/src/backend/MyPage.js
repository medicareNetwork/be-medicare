import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function MyPage() {
    const [memberData, setMemberData] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('http://localhost:8090/api/member/mypage')
            .then(response => {
                setMemberData(response.data);
            })
            .catch(error => {
                if (error.response && error.response.status === 401) {
                    setErrorMessage("로그인이 필요합니다.");
                    navigate('/login');
                } else {
                    setErrorMessage("정보를 불러오는 데 실패했습니다.");
                }
            });
    }, [navigate]);

    return (
        <div className='mypage-container'>
            <h2>마이페이지</h2>
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            {memberData ? (
                <div>
                    <p>나이: {memberData.memberAge}</p>
                    <p>몸무게: {memberData.memberWeight}</p>
                    <p>키: {memberData.memberHeight}</p>
                    <p>전화번호: {memberData.memberNumber}</p>
                    <p>주소: {memberData.memberAddress}</p>
                </div>
            ) : (
                <p>로딩 중...</p>
            )}
        </div>
    );
}

export default MyPage;
