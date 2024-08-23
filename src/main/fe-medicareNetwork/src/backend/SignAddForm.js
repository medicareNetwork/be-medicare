import React,{useState} from 'react';
import axios from "axios";
import '../css/SignAddForm.css';

function SignAddForm() {
    const [formData, setFormData] = useState({
        memberEmail: '',
        memberPassword: '',
        confirmPassword: '',
        memberQ: '다녔던 초등학교 이름은?',
        memberQnA: '',
        memberName: '',
        memberAge: '',
        memberWeight: '',
        memberHeight: '',
        memberNumber: '',
        memberAddress: '',
    });

    const [errorMessage, setErrorMessage] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (formData.memberPassword !== formData.confirmPassword) {
            setErrorMessage("비밀번호가 일치하지 않습니다");
            return;
        }

        axios.post('http://localhost:8090/api/member/save', formData)
            .then(response => {
                window.location.href = '/loginAdd';
            });

    };

    return (
        <div className='signAdd-container'>
            <form onSubmit={handleSubmit}>
                <h2>회원가입</h2>
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
                <div>
                    <input
                        type="password"
                        name="confirmPassword"
                        placeholder="비밀번호 확인"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberQ"
                        value={formData.memberQ}
                        readOnly
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberQnA"
                        placeholder="비밀번호 질문 답변"
                        value={formData.memberQnA}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberName"
                        placeholder="이름"
                        value={formData.memberName}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberAge"
                        placeholder="나이"
                        value={formData.memberAge}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberWeight"
                        placeholder="몸무게"
                        value={formData.memberWeight}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberHeight"
                        placeholder="신장"
                        value={formData.memberHeight}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberNumber"
                        placeholder="핸드폰 번호"
                        value={formData.memberNumber}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <input
                        type="text"
                        name="memberAddress"
                        placeholder="주소"
                        value={formData.memberAddress}
                        onChange={handleChange}
                    />
                </div>
                {errorMessage && <div style={{color:'red'}}>{errorMessage}</div>}
                <button type='submit'>회원가입</button>
            </form>
        </div>
    );


}

export default SignAddForm;
