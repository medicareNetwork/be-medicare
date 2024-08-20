import React, { useEffect, useState } from 'react';
import axios from "axios";

const MainPage = () => {
    const [user, setUser] = useState(null);

    useEffect(() => {
        axios.get('/api/member/session')
            .then(response=> {
                if (response.status === 200) {
                    setUser(response.data); // 세션에서 가져온 사용자 정보 설정
                }
            })
            .catch(error => {
                console.error('Error fetching session data : ', error);
                //catch에서 로그인페이지로 리다이렉트 또는 에러처리
            });
    }, []);

}