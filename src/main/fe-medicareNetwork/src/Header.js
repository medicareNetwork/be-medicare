import React, { useState } from 'react';
import './Header.css';

const Header = ({ onLoginClick, onCartClick, onCommunityClick, cartCount }) => {
    const [searchTerm, setSearchTerm] = useState('');

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        console.log('Search term:', searchTerm);
    };

    return (
        <header className="header">
            <div className="header-content">
                <button className="logo" onClick={() => window.location.href = '/'}>Medicare Network</button>
                <nav className="nav">
                    <ul className="left-menu">
                        <li><a href="/new-arrivals">New Arrivals</a></li>
                        <li><a href="/best-sellers">Best Sellers</a></li>
                        <li><a href="/sale-items">Sale Items</a></li>
                    </ul>
                    <ul className="right-menu">
                        <li><a href="/community" onClick={onCommunityClick}>Community</a></li>
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
                    <button className="login-btn" onClick={onLoginClick}>Sign In</button>
                    <button className="cart-btn" onClick={onCartClick}>
                        Cart {cartCount > 0 && `(${cartCount})`}
                    </button>
                </div>
            </div>
        </header>
    );
};

export default Header;