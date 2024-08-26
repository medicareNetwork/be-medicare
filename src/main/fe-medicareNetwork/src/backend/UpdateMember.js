import React, { useState} from 'react';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';

const UpdateMember = () => {
    const location = useLocation();
    const [member, setMember] = useState(location.state?.member || {
        memberName: '',
        memberAge: '',
        memberHeight: '',
        memberWeight: '',
        memberAddress: '',
        memberNumber: '',
    });

    const navigate = useNavigate();
    const [errors, setErrors] = useState({});


    const handleChange = (e) => {
        const { name, value } = e.target;
        setMember({
            ...member,
            [name]: value,
        });
    };

    const errorForm = () => {
        const newError = {};

        if (!member.memberName) newError.memberName = "이름을 입력하세요"
        if (!member.memberAge) newError.memberAge = "나이를 입력하세요"
        if (!member.memberHeight) newError.memberHeight = "키를 입력하세요"
        if (!member.memberWeight) newError.memberWeight = "몸무게를 입력하세요"
        if (!member.memberAddress) newError.memberAddress = "주소를 입력하세요"
        if (!member.memberNumber) newError.memberNumber = "전화번호를 입력하세요"

        return newError;

    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const updateError = errorForm();
        if (Object.keys(updateError).length > 0) {
            setErrors(updateError);
            return;
        }


        axios.post('http://localhost:8090/api/member/update', member, { withCredentials: true })
            .then(response => {
                alert('정보가 성공적으로 업데이트되었습니다.');
                navigate('/mypage');
            })
            .catch(error => {
                console.error('Error updating member info', error);
                alert('업데이트 중 오류가 발생했습니다.');
            });
    };



    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>이름: </label>
                <input
                    type="text"
                    name="memberName"
                    value={member.memberName}
                    onChange={handleChange}
                />
                {errors.memberName && <p style={{ color: 'red' }}>{errors.memberName}</p>}
            </div>
            <div>
                <label>나이: </label>
                <input
                    type="text"
                    name="memberAge"
                    value={member.memberAge}
                    onChange={handleChange}
                />
                {errors.memberAge && <p style={{ color: 'red' }}>{errors.memberAge}</p>}

            </div>
            <div>
                <label>키: </label>
                <input
                    type="text"
                    name="memberHeight"
                    value={member.memberHeight}
                    onChange={handleChange}
                />
                {errors.memberHeight && <p style={{ color: 'red' }}>{errors.memberHeight}</p>}
            </div>
            <div>
                <label>몸무게: </label>
                <input
                    type="text"
                    name="memberWeight"
                    value={member.memberWeight}
                    onChange={handleChange}
                />
                {errors.memberWeight && <p style={{ color: 'red' }}>{errors.memberWeight}</p>}
            </div>
            <div>
                <label>주소: </label>
                <input
                    type="text"
                    name="memberAddress"
                    value={member.memberAddress}
                    onChange={handleChange}
                />
                {errors.memberAddress && <p style={{ color: 'red' }}>{errors.memberAddress}</p>}
            </div>
            <div>
                <label>전화번호: </label>
                <input
                    type="text"
                    name="memberNumber"
                    value={member.memberNumber}
                    onChange={handleChange}
                />
                {errors.memberNumber && <p style={{ color: 'red' }}>{errors.memberNumber}</p>}
            </div>
            <button type="submit">수정완료</button>
        </form>
    );
};

export default UpdateMember;
