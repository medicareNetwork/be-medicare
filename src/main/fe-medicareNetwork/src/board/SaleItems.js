import React from 'react';
import '../css/SaleItems.css';
import handleAddToCart from "./SupplementList";
import {CartPlusFill} from "react-bootstrap-icons"; // 별도의 CSS 파일을 사용할 예정

const products = [
    { id: 1, name: '알부민 정', price: '8,000원', image: 'albumin.webp' },
    { id: 2, name: '먹는 콜라겐', price: '18,000원', image: 'collagen.jpg' },
    { id: 3, name: '비타민 C', price: '23,000원', image: 'vitaminc.jpg' },
    { id: 4, name: '로열 젤리', price: '25,000원', image: 'royal.jpg' }
    // 추가 제품을 여기에 나열
];

const SaleItems = ({ addToCart }) => {
    return (
        <div className="sale-items">
            <h1 className="title">Sale Items</h1>
            <p className="subtitle">Grab our best deals on selected products</p>
            <div className="products-grid">
                {products.map(product => (
                    <div key={product.id} className="product-card">
                        <img src={`/images/${product.image}`} alt={product.name} className="product-image"/>
                        <h2 className="product-name">{product.name}</h2>
                        <p className="product-price">{product.price}</p>
                        <button
                            className="btn btn-dark add-to-cart-button ml-2"
                            onClick={() => addToCart(product)}
                        >
                            장바구니 담기 <CartPlusFill size={20}/>
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default SaleItems;