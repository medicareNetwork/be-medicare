import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Header';
import VideoSection from './video/VideoSection';
import './css/App.css';
import videoSrc from './video/health.mp4'; // 비디오 파일 경로
import Login from './backend/Login';
import SupplementList from './board/SupplementList';
import SupplementButton from './SupplementButton';
import Footer from './Footer';
import NewArrivals from './board/NewArrivals';
import BestSellers from './board/BestSellers';
import SaleItems from './board/SaleItems';
import Cart from './order/Cart';
import Community from './community/Community'; // 게시판 페이지 import
import useCheckLoginStatus from './session/CheckLogin';
import ContactUs from './community/ContactUs';
import axios from "axios";
import KakaoMap from "./KakaoMap";
import SignAddForm from "./backend/SignAddForm";
import LoginForm from "./backend/Login";
import FindEmail from "./backend/FindEmail";
import FindPassword from "./backend/FindPassword"

function App() {
    const [isLoginScreen, setIsLoginScreen] = useState(false);
    const [cartItems, setCartItems] = useState([]);
    const [cartMessage, setCartMessage] = useState('');
    const [bestList, setBestList] = useState([]);
    const isLoggedIn = useCheckLoginStatus(); // 로그인 상태 확인

    // 로컬 스토리지에서 카트 아이템을 불러옵니다.
    useEffect(() => {
        const storedCartItems = localStorage.getItem('cartItems');
        if (storedCartItems) {
            setCartItems(JSON.parse(storedCartItems));
        }
    }, []);

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
        if (!isLoggedIn) {
            setIsLoginScreen(true);
        }
    };
    const closeLoginScreen = () => {
        setIsLoginScreen(false);  // 로그인 화면 닫기
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
                <Header onLoginClick={handleLoginClick}
                        onCartClick={handleCartClick}
                        onCommunityClick={handleCommunityClick}
                        cartCount={cartItems.length} />
                {cartMessage && <div className="cart-message">{cartMessage}</div>}
                <div className="content">
                    {isLoginScreen ? (
                        <div>
                            <Login />
                            <button onClick={closeLoginScreen}>닫기</button>
                        </div>
                    ) : (
                        <>
                            <Routes>
                                <Route path="/login" element={<Login />} />
                                <Route path="/" element={<VideoSection videoSrc={videoSrc} />} />
                                <Route path="/new-arrivals" element={<NewArrivals addToCart={addToCart} />} />
                                <Route path="/best-sellers" element={<BestSellers addToCart={addToCart} bestList={bestList} />} />
                                <Route path="/sale-items" element={<SaleItems addToCart={addToCart} />} />
                                <Route path="/cart" element={<Cart cart={cartItems} />} />
                                <Route path="/community" element={<Community />} /> {/* Community 페이지 라우팅 추가 */}
                                <Route path="/contact-us" element={<ContactUs />} /> {/* Contact Us 페이지 추가 */}
                            </Routes>
                            <SupplementButton />
                            <SupplementList addToCart={addToCart} />
                            <Footer />
                        </>
                        )}
                </div>
            </div>
        </Router>
    );
}

const LoginScreen2 = () => {
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