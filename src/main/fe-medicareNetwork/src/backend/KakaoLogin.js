import React from 'react';

const KakaoLogin = () => {
    const clientId = "092a728241e101fb9feb2e81e3c1b0ab";
    const redirectUri = "http://localhost:8090/api/callback";

    const kakaoLoginUrl = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectUri}`;

    const handleLogin = () => {
        window.location.href = kakaoLoginUrl;
    };

    return (
        <div>
            <button onClick={handleLogin}>카카오 로그인</button>
        </div>
    );
};

export default KakaoLogin;
