import React, { useState, useEffect } from 'react';
import StarRating from './StarRating';
import '../css/SupplementList.css';
import axios from "axios";

const PAGE_SIZE = 20;

const SupplementList = ({ addToCart, member }) => { // member 정보를 props로 받아옴
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [quantities, setQuantities] = useState({}); // 수량을 저장할 상태

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

    const handlePageChange = (page) => {
        setCurrentPage(page);
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

    return (
        <div className="supplement-list">
            {products.map(item => (
                <div className="supplement-item" key={item.id}>
                    <img src={`${item.filepath}`} alt={`${item.filename}`} />
                    <div className="supplement-info">
                        <p className="supplement-price">{item.price}</p>
                        <p className="supplement-description">{item.content}</p>
                        <StarRating rating={4} />
                        <input
                            type="number"
                            min="1"
                            value={quantities[item.id] || 1}
                            onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                            className="quantity-input"
                        />
                        <button
                            className="add-to-cart-button"
                            onClick={() => handleAddToCart(item)} // Add to Cart function
                        >
                            Add to Cart
                        </button>
                    </div>
                </div>
            ))}
            <div className="pagination">
                <button
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                >
                    &laquo; Previous
                </button>
                {[...Array(totalPages).keys()].map((page) => (
                    <button
                        key={page}
                        onClick={() => handlePageChange(page)}
                        className={currentPage === page ? 'active' : ''}
                    >
                        {page + 1}
                    </button>
                ))}
                <button
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === totalPages - 1}
                >
                    Next &raquo;
                </button>
            </div>
        </div>
    );
};

export default SupplementList;
