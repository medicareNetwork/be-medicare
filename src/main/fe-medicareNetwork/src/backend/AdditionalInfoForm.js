import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdditionalInfo = () => {
    const [formData, setFormData] = useState({
        age: '',
        weight: '',
        height: '',
        number: '',
        memberAddress: '',
        memberPostcode: '',
        memberDetailAddress: '',
        memberExtraAddress: '',
    });

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
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post('http://localhost:8090/api/saveAdditionalInfo', formData, { withCredentials: true })
            .then(response => {
                console.log('Additional info saved:', response);
                window.location.href = '/'; // 추가 정보 입력 후 홈으로 이동
            })
            .catch(error => {
                console.error('Failed to save additional info:', error);
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
        <form onSubmit={handleSubmit} style={{maxWidth: '400px', margin: '0 auto'}}>
            <label>
                나이
                <input type="text" name="age" value={formData.age} onChange={handleChange} required/>
            </label>
            <label>
                몸무게
                <input type="text" name="weight" value={formData.weight} onChange={handleChange} required/>
            </label>
            <label>
                신장
                <input type="text" name="height" value={formData.height} onChange={handleChange} required/>
            </label>
            <label>
                전화번호
                <input type="text" name="number" value={formData.number} onChange={handleChange} required/>
            </label>
            <div style={{display: 'flex', alignItems: 'center'}}>
                <input
                    type="text"
                    id="sample6_postcode"
                    name="memberPostcode"
                    placeholder="우편번호"
                    value={formData.memberPostcode}
                    onChange={handleChange}
                    readOnly
                    style={{ flexGrow: 1, marginRight: '10px' }}
                />
                <button type="button" onClick={handlePostcodeSearch} style={{ flexShrink: 0 }}>
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
            <button type="submit">입력완료</button>
        </form>
    );
};

export default AdditionalInfo;
