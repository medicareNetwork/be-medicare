import React, {useEffect, useState} from 'react';
import './css/Header.css';
import { useNavigate } from "react-router-dom";


const Header = ({ onCartClick, onCommunityClick, cartCount, isLoginIn, onLogout,member }) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [login, setLogin] = useState([]);


    useEffect(() => {
        const storedMember = sessionStorage.getItem("member1");
        if (storedMember) {
            const parsedMember = JSON.parse(storedMember);
            console.log('Parsed Member:', parsedMember); // 추가된 로그
            setLogin(parsedMember);
        }
    }, []);

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        console.log('Search term:', searchTerm);
    };

    const navigate = useNavigate();

    const handleSignAddClick = () => {
        navigate('/signAdd')
    }

    const handleMyPageClick = () => {
        navigate('/mypage')
    }

    const handleLoginAddclick = () => {
        navigate('/loginAdd')
    }
    const handleCartClick = () => {
        navigate('/cart');
    };
    const handleAdminClick = () => {
        navigate('/order/orderlistadmin');
    }

    return (
        <header className="header">
            <div className="header-content">
                <button  onClick={() => navigate('/')}>
                    <img src="/logg.jpg" alt="Medicare Logo" style={{width: '130px', height: '60px', border: 'none', padding: '0', margin: '0'}} />
                </button>
                <nav className="nav">
                    <ul className="left-menu">
                        <li><a href="/new-arrivals">신상품</a></li>
                        <li><a href="/best-sellers">인기상품</a></li>
                        <li><a href="/sale-items">기간 세일</a></li>
                    </ul>
                    <ul className="right-menu">
                        <li><a href="/community" onClick={onCommunityClick}>고객센터</a></li>
                        <li><a href="/contact-us">Contact Us</a></li>
                        <li><a href="/maps">주변 약국</a> </li>
                    </ul>
                </nav>
                <div className="auth">
                    <form className="search-form" onSubmit={handleSearchSubmit}>
                        <input
                            type="text"
                            className="search-input"
                            value={searchTerm}
                            onChange={handleSearchChange}
                            placeholder="Search"
                        />
                    </form>
                    {isLoginIn ? (
                        <>
                            <button className="login-btn" onClick={handleMyPageClick}>마이페이지</button>
                            <button className="login-btn" onClick={onLogout}>로그아웃</button>
                        </>
                    ) : (
                        <>
                            <button className="login-btn" onClick={handleSignAddClick}>회원가입</button>
                            <button className="login-btn" onClick={handleLoginAddclick}>로그인</button>
                        </>
                    )}
                    <button className="login-btn" onClick={handleCartClick}>장바구니</button>
                    {login && login.grade === 'many' && (
                        <button className="login-btn" onClick={handleAdminClick}>관리자</button>
                    )}
                </div>
            </div>
        </header>
    );
};

export default Header;
