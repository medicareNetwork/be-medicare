import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './Header';
import VideoSection from './video/VideoSection';
import './css/App.css';
import videoSrc from './video/health.mp4'; // 비디오 파일 경로
import Login from './backend/Login';
import SupplementList from './board/SupplementList';
import Footer from './Footer';
import NewArrivals from './board/NewArrivals';
import BestSellers from './board/BestSellers';
import SaleItems from './board/SaleItems';
import Cart from './order/Cart';
import Community from './community/Community'; // 게시판 페이지 import
import ContactUs from './community/ContactUs';
import axios from "axios";
import KakaoMap from "./KakaoMap";
import SignAddForm from "./backend/SignAddForm";
import LoginForm from "./backend/Login";
import FindEmail from "./backend/FindEmail";
import FindPassword from "./backend/FindPassword"
import MyPage from "./backend/MyPage";
import UpdateMember from "./backend/UpdateMember";
import PasswordChange from "./backend/PasswordChange";
import AdditionalInfoForm from './backend/AdditionalInfoForm';
import KakaoCallback from "./backend/KakaoCallback";



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

    useEffect(() => {
        axios.get('http://localhost:8090/api/bestList')
            .then((res) => {
                setBestList(res.data);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
            });
    }, []);


    // 로컬 스토리지에 true인가 확인
    useEffect(() => {
        const loggedIn = localStorage.getItem('isLoggedIn');
        if (loggedIn === 'true') {
            setIsLoginIn(true);
        }
    }, []);



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

    // 로그인 상태 변수
    const [isLoginIn, setIsLoginIn] = useState(false);

    // 로그인 성공시 true
    const handleLoginSuccess = () => {
        setIsLoginIn(true);
    }

    // 로그아웃시 false
    const handleLogout = () => {
        localStorage.removeItem('isLoggedIn'); // 로컬 스토리지에서 로그인 상태 제거
        setIsLoginIn(false);
        window.location.href = '/';
    };


    return (
        <Router>
            <div className="App">
                <Header
                        onCartClick={handleCartClick}
                        onCommunityClick={handleCommunityClick}
                        cartCount={cartItems.length}
                        isLoginIn={isLoginIn}
                        onLogout={handleLogout}/>

                {cartMessage && <div className="cart-message">{cartMessage}</div>}
                <div className="content">

                    {isLoginScreen ? (
                        <div>
                            <Login/>
                            <button onClick={closeLoginScreen}>닫기</button>
                        </div>
                    ) : (
                        <>
                            <Routes>
                                <Route path="/" element={<VideoSection videoSrc={videoSrc}/>}/>
                                <Route path="/new-arrivals" element={<NewArrivals addToCart={addToCart}/>}/>
                                <Route path="/best-sellers"
                                       element={<BestSellers addToCart={addToCart} bestList={bestList}/>}/>
                                <Route path="/sale-items" element={<SaleItems addToCart={addToCart}/>}/>
                                {/*<Route path="/cart" element={<Cart cart={cartItems}/>}/>*/}
                                <Route path="/community" element={<Community/>}/> {/* Community 페이지 라우팅 추가 */}
                                <Route path="/contact-us" element={<ContactUs/>}/> {/* Contact Us 페이지 추가 */}
                                <Route path='/signAdd' element={<SignAddForm/>}/>
                                <Route path='/find-Email' element={<FindEmail/>}/>
                                <Route path='/find-password' element={<FindPassword/>}/>
                                <Route path='/loginAdd' element={<LoginForm onLoginSuccess={handleLoginSuccess}/>}/>
                                <Route path="/maps" element={<KakaoMap/>}/>
                                <Route path='/mypage' element={<MyPage/>}/>
                                <Route path="/update" element={<UpdateMember />} />
                                <Route path="/passwordChange" element={<PasswordChange/>} />
                                <Route path="/callback" element={<KakaoCallback />} />
                                <Route path="/additional-info" element={<AdditionalInfoForm />} />
                                <Route path="/cart" element={<Cart />} />


                            </Routes>
                            <SupplementList addToCart={addToCart}/>
                            <Footer/>
                        </>
                    )}
                </div>
            </div>
        </Router>
    );
}


export default App;