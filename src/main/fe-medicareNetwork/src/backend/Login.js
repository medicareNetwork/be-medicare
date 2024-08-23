import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Session from "react-session-api/src";


function LoginForm() {

    const [formData, setFormData] = useState({
        memberEmail: '',
        memberPassword: ''
    });

    const navigate = useNavigate();

    const handleButtonClick = () => {
        navigate('/find-email')
    }

    const handlePasswordClick = () => {
        navigate("/find-password")
    }

    const [errorMessage, setErrorMessage] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();


    axios.post('http://localhost:8090/api/member/login', formData)
        .then(response => {
            const member = response.data;
            window.sessionStorage.setItem("member1",JSON.stringify(member));
            window.location.href = '/';
        })
        .catch(error => {
            if(error.response && error.response.status === 401) {
                setErrorMessage("아이디 및 비밀번호가 틀립니다");
            }
        });
};

return (
    <div className='login-container'>
        <form onSubmit={handleSubmit}>
            <h2>로그인</h2>
            <div>
                <input
                    type='text'
                    name='memberEmail'
                    placeholder='이메일'
                    value={formData.memberEmail}
                    onChange={handleChange}
                    required
                />
            </div>
            <div>
                <input
                    type='password'
                    name='memberPassword'
                    placeholder='비밀번호'
                    value={formData.memberPassword}
                    onChange={handleChange}
                    required
                />
            </div>
            {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
            <button type='submit'>로그인</button>
            <button onClick={handleButtonClick}>이메일 찾기</button>
            <button onClick={handlePasswordClick}>비밀번호 찾기</button>

        </form>
    </div>
);
}

export default LoginForm;
