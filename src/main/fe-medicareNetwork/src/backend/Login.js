import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import Session from "react-session-api/src";


function LoginForm({onLoginSuccess}) {

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

            //건웅 : LocalStorage -> 영구 저장소 ( 로그인정보를 저장하기에는 보안적으로 좋지는 않다고 함,
            // npm install js-cookie 라는것을 이용하는게 좋다고 함 ( 나중에 확인해보고 바꾸기 )
            localStorage.setItem('isLoggedIn', 'true');
            onLoginSuccess();
            navigate('/');


            //태립 : SessionStorage -> 브라우저를 닫으면 데이터가 지워짐
            // 개발단계에선 이것도 괜찮고, js-cookie는 날짜를 지정한 만큼 데이터를 저장해두기 때문에 로그인유지 등을 할수있는 장점이있음
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
