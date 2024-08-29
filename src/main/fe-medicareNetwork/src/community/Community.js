import React from 'react';
import { useNavigate } from 'react-router-dom'; // useNavigate 사용
import '../css/Community.css';

const Community = () => {
    const navigate = useNavigate(); // useNavigate로 페이지 이동

    const handleNavigate = (path) => {
        navigate(path); // 동적으로 경로 이동
    };

    return (

        <div className="community-container">
            <div className="community">
                <button id="Gong" onClick={() => handleNavigate('/Gong')}>공지 바로가기</button>
                {/* Gong 페이지로 이동 */}
            </div>
            <div className="community">
                <button id="Gun" onClick={() => handleNavigate('/Gun')}>건의 바로가기</button>
                {/* Gun 페이지로 이동 */}
            </div>
            <div  className="community">
                <button id="Mun" onClick={() => handleNavigate('/Mun')}>문의 바로가기</button>
                {/* Mun 페이지로 이동 */}
            </div>
        </div>

    );
};

export default Community;
