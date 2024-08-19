import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Header';
import VideoSection from './VideoSection';
import './App.css';
import videoSrc from './health.mp4'; // 비디오 파일 경로
import SupplementList from './SupplementList';
import SupplementButton from './SupplementButton';
import Footer from './Footer';
import NewArrivals from './NewArrivals';
import BestSellers from './BestSellers';
import SaleItems from './SaleItems';
import Cart from './Cart';
import Community from './Community'; // 게시판 페이지 import
import ContactUs from './ContactUs';
import axios from "axios";

function App() {
    const [isLoginScreen, setIsLoginScreen] = useState(false);
    const [cartItems, setCartItems] = useState([]);
    const [cartMessage, setCartMessage] = useState('');
    const [bestList, setBestList] = useState([]);

    // 로컬 스토리지에서 카트 아이템을 불러옵니다.
    useEffect(() => {
        const storedCartItems = localStorage.getItem('cartItems');
        if (storedCartItems) {
            setCartItems(JSON.parse(storedCartItems));
        }
    }, []);

    // 페이지를 처음 열때마다 로컬스토리지 초기화(쓸모없는 코드)
    // useEffect(() => {
    //     localStorage.setItem('cartItems', JSON.stringify(cartItems));
    // }, [cartItems]);

    useEffect(() => {
        axios.get('http://localhost:8090/api/bestList')
            .then((res) => {
                setBestList(res.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }, []);

    const handleLoginClick = () => {
        setIsLoginScreen(true);
    };

    const addToCart = (product) => {
        setCartItems(prevItems => {
            const newItems = [...prevItems, product];
            console.log('카트아이템 : ' + newItems);
            localStorage.setItem('cartItems',JSON.stringify(newItems));
            return newItems;
        });
        setCartMessage('Item added to cart');
        setTimeout(() => setCartMessage(''), 2000); // 2초 후에 메시지 사라짐
    };

    const handleCartClick = () => {
        // Cart 버튼 클릭 시 카트 페이지로 이동
        window.location.href = '/cart';
    };

    const handleCommunityClick = () => {
        // Community 버튼 클릭 시 커뮤니티 페이지로 이동
        window.location.href = '/community';
    };

    return (
        <Router>
            <div className="App">
                <Header onLoginClick={handleLoginClick} onCartClick={handleCartClick} onCommunityClick={handleCommunityClick} cartCount={cartItems.length} />
                {cartMessage && <div className="cart-message">{cartMessage}</div>}
                <div className="content">
                    {isLoginScreen ? (
                        <LoginScreen />
                    ) : (
                        <>
                            <Routes>
                                <Route path="/" element={<VideoSection videoSrc={videoSrc} />} />
                                <Route path="/new-arrivals" element={<NewArrivals addToCart={addToCart} />} />
                                <Route path="/best-sellers" element={<BestSellers addToCart={addToCart} bestList={bestList} />} />
                                <Route path="/sale-items" element={<SaleItems addToCart={addToCart} />} />
                                <Route path="/cart" element={<Cart cart={cartItems} />} />
                                <Route path="/community" element={<Community />} /> {/* Community 페이지 라우팅 추가 */}
                                <Route path="/contact-us" element={<ContactUs />} /> {/* Contact Us 페이지 추가 */}
                            </Routes>
                            <SupplementButton />
                            <SupplementList addToCart={addToCart}/>
                            <Footer />
                        </>
                    )}
                </div>
            </div>
        </Router>
    );
}

const LoginScreen = () => {
    return (
        <div className="login-screen">
            <div className="login-container">
                <h2>Sign In</h2>
                <form>
                    <div className="input-group">
                        <label htmlFor="username">ID</label>
                        <input type="text" id="username" name="username" />
                    </div>
                    <div className="input-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" name="password" />
                    </div>
                    <button type="submit" className="login-button">Sign In</button>
                </form>
            </div>
        </div>
    );
};

export default App;