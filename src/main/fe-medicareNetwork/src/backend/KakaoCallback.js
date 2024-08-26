import React, { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const KakaoCallback = () => {
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        const urlParams = new URLSearchParams(location.search);
        const isNewUser = urlParams.get('isNewUser') === 'true';
        const userId = urlParams.get('userId');

        console.log('Data received:', { isNewUser, userId });

        if (isNewUser) {
            console.log('Navigating to /additional-info');
            navigate('/additional-info');
        } else {
            console.log('Navigating to /');
            navigate('/');
        }
    }, [navigate, location]);

    return <div>Loading...</div>;
};

export default KakaoCallback;
