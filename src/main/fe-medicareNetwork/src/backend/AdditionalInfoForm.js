import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AdditionalInfoForm = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        age: '',
        weight: '',
        height: '',
        number: '',
        address: ''
    });

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        axios.post('http://localhost:8090/api/saveAdditionalInfo', formData)
            .then(() => {
                navigate('/');
            })
            .catch(error => {
                console.error('Error saving additional info:', error);
            });
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" name="age" placeholder="Age" value={formData.age} onChange={handleChange} />
            <input type="text" name="weight" placeholder="Weight" value={formData.weight} onChange={handleChange} />
            <input type="text" name="height" placeholder="Height" value={formData.height} onChange={handleChange} />
            <input type="text" name="number" placeholder="Phone Number" value={formData.number} onChange={handleChange} />
            <input type="text" name="address" placeholder="Address" value={formData.address} onChange={handleChange} />
            <button type="submit">Submit</button>
        </form>
    );
};

export default AdditionalInfoForm;
