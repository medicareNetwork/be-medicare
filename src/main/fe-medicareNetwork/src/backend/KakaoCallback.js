import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const KakaoCallback = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const code = urlParams.get('code');

        if (code) {
            fetch(`http://localhost:8090/api/callback?code=${code}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                .then(response => response.json())
                .then(data => {
                    if (data.isNewUser) {
                        navigate('/additional-info'); // 새로운 사용자일 경우 추가 정보 입력 페이지로 리디렉션
                    } else {
                        navigate('/'); // 기존 사용자일 경우 메인 페이지로 리디렉션
                    }
                })
                .catch(error => {
                    console.error('Error during fetching callback:', error);
                });
        }
    }, [navigate]);

    return <div>Loading...</div>;
};

export default KakaoCallback;
