import React, { useState, useEffect } from 'react';
import axios from "axios";
import '../css/SignAddForm.css';
import KakaoLogin from "./KakaoLogin";

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
        memberPostcode: '',
        memberDetailAddress: '',
        memberExtraAddress: '',
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [emailCheck, setEmailCheck] = useState(false);
    const [emailCheckMessage, setEmailCheckMessage] = useState('');


    // 다음 포스트코드 API 동적으로 로드
    useEffect(() => {
        const script = document.createElement("script");
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        script.async = true;
        document.body.appendChild(script);

        return () => {
            document.body.removeChild(script);
        };
    }, []);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        if (e.target.name === 'memberEmail') {
            setEmailCheck(false);
            setEmailCheckMessage('');
        }
    };

    const checkEmailButton = () => {
        axios.post('http://localhost:8090/api/member/check-email',
            { memberEmail: formData.memberEmail })
            .then(response => {
                if (response.data) {
                    setEmailCheckMessage("이 이메일은 사용중입니다");
                    setEmailCheck(false);
                } else {
                    setEmailCheckMessage("사용가능한 이메일입니다");
                    setEmailCheck(true);
                }
            })
            .catch(error => {
                console.error("이메일 중복체크를 확인해주세요");
            });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!emailCheck) {
            setErrorMessage("이메일 중복확인을 해주세요");
            return;
        }

        if (formData.memberPassword !== formData.confirmPassword) {
            setErrorMessage("비밀번호가 일치하지 않습니다");
            return;
        }

        axios.post('http://localhost:8090/api/member/save', formData)
            .then(response => {
                window.location.href = '/loginAdd';
            });
    };

    const handlePostcodeSearch = () => {
        new window.daum.Postcode({
            oncomplete: function(data) {
                let addr = ''; // 주소 변수
                let extraAddr = ''; // 참고항목 변수

                if (data.userSelectedType === 'R') { // 도로명 주소 선택
                    addr = data.roadAddress;
                } else { // 지번 주소 선택
                    addr = data.jibunAddress;
                }

                if (data.userSelectedType === 'R') {
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                    }
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                setFormData({
                    ...formData,
                    memberPostcode: data.zonecode,
                    memberAddress: addr,
                    memberExtraAddress: extraAddr,
                    memberDetailAddress: '',
                });

                document.getElementById("sample6_detailAddress").focus();
            }
        }).open();
    };

    return (
        <div className='signAdd-container'>
            <form onSubmit={handleSubmit}>
                <h2>회원가입</h2>
                <div className="flex-container">
                    <input
                        type='email'
                        name='memberEmail'
                        placeholder='이메일'
                        value={formData.memberEmail}
                        onChange={handleChange}
                        required
                    />
                    <button type='button' className='emailsign' onClick={checkEmailButton}>
                        중복 체크
                    </button>
                </div>
                {emailCheckMessage && <div style={{color: 'red'}}>{emailCheckMessage}</div>}
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
                        placeholder="핸드폰 번호(- 빼고 입력하세요)"
                        value={formData.memberNumber}
                        onChange={handleChange}
                        required
                    />
                </div>
                <div>
                    <input
                        type="text"
                        id="sample6_postcode"
                        name="memberPostcode"
                        placeholder="우편번호"
                        value={formData.memberPostcode}
                        onChange={handleChange}
                        readOnly
                    />
                    <button type="button" onClick={handlePostcodeSearch}>
                        우편번호 찾기
                    </button>
                </div>
                <div>
                    <input
                        type="text"
                        id="sample6_address"
                        name="memberAddress"
                        placeholder="주소"
                        value={formData.memberAddress}
                        onChange={handleChange}
                        readOnly
                    />
                </div>
                <div>
                    <input
                        type="text"
                        id="sample6_detailAddress"
                        name="memberDetailAddress"
                        placeholder="상세주소"
                        value={formData.memberDetailAddress}
                        onChange={handleChange}
                    />
                </div>
                <div>
                    <input
                        type="text"
                        id="sample6_extraAddress"
                        name="memberExtraAddress"
                        placeholder="참고항목"
                        value={formData.memberExtraAddress}
                        onChange={handleChange}
                        readOnly
                    />
                </div>
                {errorMessage && <div style={{color: 'red'}}>{errorMessage}</div>}
                <button type='submit'>회원가입</button>
                <KakaoLogin/>
            </form>
        </div>
    );
}

export default SignAddForm;
