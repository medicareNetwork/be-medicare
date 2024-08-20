import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const handleSubmit = async (e) => {
        e.preventDefault();

        try{
            const response = await axios.post('http://localhost:8090/member/login', {
                memberEmail : email,
                memberPassword : password
            });

            if (response.status === 200) {
                //로그인 성공 시 메인 페이지로 리다이렉션
                navigate('/');
            }
        } catch (error) {
            console.error('로그인에 실패하였습니다', error);
        }
    };

    return (
        <div className="login-screen">
            <div className="login-container">
                <form className="input-group" onSubmit={handleSubmit}>
                    <h2>로그인</h2>
                    <input type="text" name="memberEmail" placeholder="이메일"
                    value={email} onChange={(e) =>setEmail(e.target.value)} />
                    <input type="text" name="memberPassword" placeholder="비밀번호"
                   value={password} onChange={(e)=>setPassword(e.target.value)} />
                    <button type="submit" className="login-button">리액트 로그인</button>
                </form>
            </div>
        </div>
    );

};

export default  Login;