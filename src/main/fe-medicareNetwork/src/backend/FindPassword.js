import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function FindPassword () {

    const [email, setEmail] = useState('');
    const [qna, setQnA] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();


    const handleSubmit = async (e) => {
        e.preventDefault();



        try {
            const response = await axios.post("http://localhost:8090/api/member/find-password", {
                memberEmail: email,
                memberQnA: qna
            });
            setPassword(response.data.memberPassword);
            setError('');
        } catch (err) {
            setError("비밀번호를 찾을수 없습니다");
            setPassword('');
        }
    };

    const LoginAddClick = () => {
        navigate("/loginAdd")
    }

    return (
        <div className='find-password-container'>
            <form onSubmit={handleSubmit}>
                <input
                    type='email'
                    placeholder='이메일'
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type='text'
                    placeholder='질문 답변'
                    value={qna}
                    onChange={(e) => setQnA(e.target.value)}
                />
                <div style={{textAlign: 'center', marginTop: '20px'}}>
                    {password && <div style={{fontSize: '20px', fontWeight: 'bold'}}>찾은 비밀번호 : {password}</div>}
                    {error && <div style={{color: 'red', fontSize: '20px', fontWeight: 'bold'}}>{error}</div>}
                </div>
                <button type='submit'>비밀번호 찾기</button>
                <button onClick={LoginAddClick} type={"submit"}>로그인</button>
            </form>
        </div>
    );


}

export default FindPassword;