import React, {useState} from "react";
import axios from "axios";


function FindPassword () {

    const [email, setEmail] = useState('');
    const [qna, setQnA] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

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
                <button type='submit'>비밀번호 찾기</button>
            </form>
            {password && <div>찾은 비밀번호 : {password}</div>}
            {error && <div style={{ color : 'red'}}>{error}</div>}
        </div>
    );


}

export default FindPassword;