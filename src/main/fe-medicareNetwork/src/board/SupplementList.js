import React, { useState, useEffect } from 'react';
import StarRating from './StarRating';
import '../css/SupplementList.css';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import { CartPlusFill } from "react-bootstrap-icons";


const PAGE_SIZE = 20;

const SupplementList = ({ addToCart, member }) => { // member 정보를 props로 받아옴
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [quantities, setQuantities] = useState({}); // 수량을 저장할 상태
    const [login, setLogin] = useState([]);
    const navigate = useNavigate();

    const fetchProducts = async (page) => {
        try {
            const response = await axios.get(`http://localhost:8090/api/list?page=${page}&size=${PAGE_SIZE}`);
            setProducts(response.data.content); // 페이지 데이터
            setTotalPages(response.data.totalPages); // 전체 페이지 수
        } catch (error) {
            console.error('Error fetching products:', error);
        }
    };

    useEffect(() => {
        fetchProducts(currentPage);
    }, [currentPage]);

    useEffect(() => {
        if(sessionStorage.getItem("member1")!=null){

            setLogin(JSON.parse(sessionStorage.getItem("member1")));
        }
    }, []);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const NewItem_Button = () => {
        window.location.href="/NewItem";
    };

    const handleQuantityChange = (itemId, value) => {
        setQuantities({
            ...quantities,
            [itemId]: value
        });
    };

    const handleAddToCart = async (item) => {
        console.log('Item:', item); // item이 전달은 되는거니


        const quantity = quantities[item.id] || 1; // 설정된 수량, 기본값 1
        const itemName = item.title;

        try {
            const response = await axios.post('http://localhost:8090/api/cart/add', {
                boardId: item.id,
                amount: quantity,
                // id: member.id // member 세션에서 처리할때 이렇게 갈거같음 ㅠㅠ
            }, {withCredentials : true});
            console.log('Response from server:', response); // 서버 응답을 로그로 출력하여 확인
            console.log('Response from server:', response.data);

            if (response.data.success) {
                alert(itemName+' ' + quantity +'개가 장바구니에 담겼습니다.');
                // 필요한 경우 추가적인 작업 수행 (예: 장바구니 업데이트 등)
            } else {
                alert('장바구니 담기에 실패하였습니다.');
            }
        } catch (error) {
            console.error('Error adding item to cart:', error);
            alert('An error occurred while adding the item to the cart.');
        }
    };

    const modifyItem = async (item) => {
        alert("Item : " + item.title);
        navigate("/Modify", {state:{item},})
    };

    const hrefViews = async (item) =>{
        navigate("/Views", {state:{item},})
    }

    return (
        <div>
            <div className="supplement-list">
                {products.map(item => (
                    <div className="supplement-item" key={item.id}>
                        <img onClick={() => hrefViews(item)} src={`${item.filepath}`} alt={`${item.filename}`} />
                        <div className="supplement-info">
                            <h2 onClick={() => hrefViews(item)} className="supplement-description">{item.title}</h2>
                            <p className="supplement-price">{item.price}</p>
                            <StarRating rating={4} />
                            <div className="d-flex justify-content-between align-items-center mt-1">
                                <input
                                    type="number"
                                    min="1"
                                    value={quantities[item.id] || 1}
                                    onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                    className="form-control quantity-input"
                                />
                                <button
                                    className="btn btn-dark add-to-cart-button ml-5"
                                    onClick={() => handleAddToCart(item)}
                                >
                                    장바구니 담기<CartPlusFill size={20} />
                                </button>
                            </div>
                            {(login.email === item.name) && (
                                <button onClick={() => modifyItem(item)} className="btn btn-secondary btn-sm mt-2">수정</button>
                            )}
                        </div>
                    </div>
                ))}
            </div>

            {/* Pagination Section */}
            <div className="pagination mt-4 d-flex justify-content-center">
                <nav>
                    <ul className="pagination">
                        <li className={`page-item ${currentPage === 0 ? 'disabled' : ''}`}>
                            <button className="page-link" onClick={() => handlePageChange(currentPage - 1)}>&laquo; 이전</button>
                        </li>
                        {[...Array(totalPages).keys()].map((page) => (
                            <li key={page} className={`page-item ${currentPage === page ? 'active' : ''}`}>
                                <button className="page-link" onClick={() => handlePageChange(page)}>
                                    {page + 1}
                                </button>
                            </li>
                        ))}
                        <li className={`page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}`}>
                            <button className="page-link" onClick={() => handlePageChange(currentPage + 1)}>다음 &raquo;</button>
                        </li>
                    </ul>
                </nav>
            </div>

            {/* Product Registration Section */}
            <div className="text-center mt-4">
                {login.grade === "many" && (
                    <button className="btn btn-outline-dark" onClick={() => NewItem_Button()}>상품 등록</button>
                )}
            </div>
        </div>
    );
}

export default SupplementList;