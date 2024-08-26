import React, { useState } from 'react';
import './css/Header.css';
import { useNavigate } from "react-router-dom";

const Header = ({ onCartClick, onCommunityClick, cartCount, isLoginIn, onLogout }) => {
    const [searchTerm, setSearchTerm] = useState('');

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
        navigate('/myPage')
    }

    const handleLoginAddclick = () => {
        navigate('/loginAdd')
    }

    return (
        <header className="header">
            <div className="header-content">
                <button  onClick={() => window.location.href = '/'}>
                    <img src="/logg.jpg" alt="Medicare Logo" style={{width: '130px', height: '60px', border: 'none', padding: '0', margin: '0'}} />
                </button>
                <nav className="nav">
                    <ul className="left-menu">
                        <li><a href="/new-arrivals">신상품</a></li>
                        <li><a href="/best-sellers">인기상품</a></li>
                        <li><a href="/sale-items">기간 세일</a></li>
                    </ul>
                    <ul className="right-menu">
                        <li><a href="/community/Community" onClick={onCommunityClick}>고객센터</a></li>
                        <li><a href="/contact-us">Contact Us</a></li>
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
                    <button className="cart-btn" onClick={onCartClick}>
                        Cart {cartCount > 0 && `(${cartCount})`}
                    </button>
                </div>
            </div>
        </header>
    );
};

export default Header;
