import React, { useState, useEffect } from 'react';
import StarRating from './StarRating';
import '../css/SupplementList.css';
import axios from "axios";

const PAGE_SIZE = 20;

const SupplementList = ({ addToCart }) => {
    const [products, setProducts] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

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

    const NewItem_Button = () => {
        window.location.href="/NewItem";
    };

    return (
        <div className="supplement-list">
            {products.map(item => (
                <div className="supplement-item" key={item.id}>
                    <img src={`${item.filepath}`} alt={`${item.filename}`} />
                    <div className="supplement-info">
                        <h2 className="supplement-description">{item.title}</h2>
                        <p className="supplement-price">{item.price}</p>
                        <StarRating rating={4}/>
                        <button
                            className="add-to-cart-button"
                            onClick={() => addToCart(item)} // Add to Cart function
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
                    disabled={currentPage === totalPages-1}
                >
                    Next &raquo;
                </button>
            </div>
            <div className="NewItem-Button">
                <button onClick={() => NewItem_Button()}>상품 등록</button>
            </div>
        </div>
    );
};

export default SupplementList;