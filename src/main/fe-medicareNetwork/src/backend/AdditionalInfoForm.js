import React, { useState } from 'react';
import axios from 'axios';

const AdditionalInfo = () => {
    const [formData, setFormData] = useState({
        age: '',
        weight: '',
        height: '',
        number: '',
        address: ''
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
        <form onSubmit={handleSubmit}>
            <label>
                Age:
                <input type="text" name="age" value={formData.age} onChange={handleChange} required />
            </label>
            <label>
                Weight:
                <input type="text" name="weight" value={formData.weight} onChange={handleChange} required />
            </label>
            <label>
                Height:
                <input type="text" name="height" value={formData.height} onChange={handleChange} required />
            </label>
            <label>
                Phone Number:
                <input type="text" name="number" value={formData.number} onChange={handleChange} required />
            </label>
            <label>
                Address:
                <input type="text" name="address" value={formData.address} onChange={handleChange} required />
            </label>
            <button type="submit">Save</button>
        </form>
    );
};

export default AdditionalInfo;
