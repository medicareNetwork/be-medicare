import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function FindEmail() {

    const [name, setName] = useState('');
    const [number, setNumber] = useState('');
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    const findPasswordClick = () => {
        navigate("/find-password")
    }

    const LoginClick = () => {
        navigate("/loginAdd")
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.post('http://localhost:8090/api/member/find-email', {
                memberName: name,
                memberNumber: number
            });
            setEmail(response.data.email);
            setError('');
        } catch (err) {
            setError("이메일을 찾을수 없습니다");
            setEmail('');
        }
    };

    return (
        <div className='findEmail-container'>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="이름"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="전화번호"
                    value={number}
                    onChange={(e) => setNumber(e.target.value)}
                />
                <button type="submit">이메일 찾기</button>
            </form>
            {email && <div>찾은 이메일: {email}</div>}
            {error && <div style={{ color: 'red' }}>{error}</div>}
            <button  onClick={LoginClick} type={"submit"}>로그인</button>
            <button onClick={findPasswordClick} type={"submit"}>비밀번호 찾기</button>
        </div>
    );

}


export default FindEmail;