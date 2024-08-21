import { useState, useEffect } from 'react';
import axios from 'axios';

const useCheckLoginStatus = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                const response = await axios.get('/api/member/session'); // 세션에서 로그인 정보 확인
                if (response.status === 200 && response.data) {
                    setIsLoggedIn(true); // 로그인된 상태로 설정
                } else {
                    setIsLoggedIn(false); // 로그인되지 않은 상태로 설정
                }
            } catch (error) {
                console.error('로그인 상태 확인 중 오류가 발생했습니다:', error);
                setIsLoggedIn(false); // 오류 발생 시 로그아웃 상태로 설정
            }
        };

        checkLoginStatus();
    }, []);

    return isLoggedIn;
};

export default useCheckLoginStatus;
