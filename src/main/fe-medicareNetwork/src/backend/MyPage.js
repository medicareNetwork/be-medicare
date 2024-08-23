import React, { useState } from 'react';

function MyPage() {
    // State management: cart items, password, personal info
    const [cartItems] = useState([
        { id: 1, name: '영양제 1', quantity: 2, price: 20 },
        { id: 2, name: '영양제 2', quantity: 1, price: 50 },
        // More items can be added
    ]);
    const [password, setPassword] = useState('');
    const [personalInfo, setPersonalInfo] = useState({
        name: '홍길동',
        email: 'example@example.com',
        phone: '010-1234-5678',
    });

    // Handler functions
    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    };

    const handlePersonalInfoChange = (e) => {
        const { name, value } = e.target;
        setPersonalInfo((prevInfo) => ({
            ...prevInfo,
            [name]: value,
        }));
    };

    const handlePasswordUpdate = () => {
        // Password update logic (e.g., send to server)
        alert('Password has been updated.');
    };

    const handlePersonalInfoUpdate = () => {
        // Personal info update logic (e.g., send to server)
        alert('Personal information has been updated.');
    };

    return (
        <div className="my-page">
            <h2>마이페이지</h2>
            <p>개인정보를 변경할 수 있습니다.</p>


            {/* 비밀번호 변경 */}
            <div className="password-update">
                <h3>비밀번호 변경</h3>
                <input
                    type="password"
                    placeholder="새로운 비밀번호 입력"
                    value={password}
                    onChange={handlePasswordChange}
                />
                <button className="button-style" onClick={handlePasswordUpdate}>비밀번호 변경</button>
            </div>

            {/* Personal Information Update */}
            <div className="personal-info-update">
                <h3>개인정보 변경</h3>
                <input
                    type="text"
                    name="name"
                    placeholder="Name"
                    value={personalInfo.name}
                    onChange={handlePersonalInfoChange}
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={personalInfo.email}
                    onChange={handlePersonalInfoChange}
                />
                <input
                    type="text"
                    name="phone"
                    placeholder="Phone Number"
                    value={personalInfo.phone}
                    onChange={handlePersonalInfoChange}
                />
                <button className="button-style" onClick={handlePersonalInfoUpdate}>개인정보 변경 </button>
            </div>
        </div>
    );
}

export default MyPage;