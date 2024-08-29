import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AdditionalInfo = () => {
    const [formData, setFormData] = useState({
        age: '',
        weight: '',
        height: '',
        number: '',
        address: '',
    });


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



    return (
        <form onSubmit={handleSubmit} style={{ maxWidth: '400px', margin: '0 auto' }}>
            <label>
                나이
                <input type="text" name="age" value={formData.age} onChange={handleChange} required />
            </label>
            <label>
                몸무게
                <input type="text" name="weight" value={formData.weight} onChange={handleChange} required />
            </label>
            <label>
                신장
                <input type="text" name="height" value={formData.height} onChange={handleChange} required />
            </label>
            <label>
                전화번호
                <input type="text" name="number" value={formData.number} onChange={handleChange} required />
            </label>
            <label>
                주소
                <input type="text" name="address" value={formData.address} onChange={handleChange} required />
            </label>
            <button type="submit">입력완료</button>
        </form>
    );
};

export default AdditionalInfo;
