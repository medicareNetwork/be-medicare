import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const PasswordChange = () => {
    const [passwords, setPasswords] = useState({
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: '',
    });

    const [errors, setErrors] = useState({});
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPasswords({
            ...passwords,
            [name]: value,
        });
    };

    const validateForm = () => {
        const newErrors = {};

        if (!passwords.currentPassword) newErrors.currentPassword = "현재 비밀번호를 입력하세요.";
        if (!passwords.newPassword) newErrors.newPassword = "새 비밀번호를 입력하세요.";
        if (!passwords.confirmNewPassword) newErrors.confirmNewPassword = "새 비밀번호 확인을 입력하세요.";
        if (passwords.newPassword !== passwords.confirmNewPassword) newErrors.confirmNewPassword = "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.";

        return newErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formErrors = validateForm();
        if (Object.keys(formErrors).length > 0) {
            setErrors(formErrors);
            return;
        }

        try {
            const response = await axios.post('http://localhost:8090/api/member/changePassword', passwords, { withCredentials: true });

            if (response.status === 200) {
                alert("비밀번호가 성공적으로 변경되었습니다.");
                navigate('/mypage');
            }
        } catch (error) {
            if (error.response && error.response.status === 401) {
                alert("현재 비밀번호가 일치하지 않습니다.");
            } else {
                setMessage("비밀번호 변경 중 오류가 발생했습니다.");
            }
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>현재 비밀번호: </label>
                <input
                    type="password"
                    name="currentPassword"
                    value={passwords.currentPassword}
                    onChange={handleChange}
                />
                {errors.currentPassword && <p style={{ color: 'red' }}>{errors.currentPassword}</p>}
            </div>
            <div>
                <label>새 비밀번호: </label>
                <input
                    type="password"
                    name="newPassword"
                    value={passwords.newPassword}
                    onChange={handleChange}
                />
                {errors.newPassword && <p style={{ color: 'red' }}>{errors.newPassword}</p>}
            </div>
            <div>
                <label>새 비밀번호 확인: </label>
                <input
                    type="password"
                    name="confirmNewPassword"
                    value={passwords.confirmNewPassword}
                    onChange={handleChange}
                />
                {errors.confirmNewPassword && <p style={{ color: 'red' }}>{errors.confirmNewPassword}</p>}
            </div>
            <button type="submit">비밀번호 변경</button>
            {message && <p>{message}</p>}
        </form>
    );
};

export default PasswordChange;
