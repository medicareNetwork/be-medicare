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
        <div>
            {member ? (
                <div>
                    <h1>안녕하세요</h1>
                    <h1>{member.memberName} 님</h1>
                    <p>나이: {member.memberAge}</p>
                    <p>몸무게: {member.memberWeight}</p>
                    <p>신장: {member.memberHeight}</p>
                    <p>핸드폰 번호: {member.memberNumber}</p>
                    <p>주소: {member.memberAddress}</p>
                </div>
            ) : (
                <div>You are not logged in.</div>
            )}
            <button onClick={handleMemberUpdate}>개인정보 수정</button>
            <button onClick={handlePasswordChange}>비밀번호 수정</button>
        </div>
    );
};

export default MyPage;
