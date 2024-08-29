import React from 'react';
import '../css/NewArrivals.css';
import {CartPlusFill} from "react-bootstrap-icons"; // 별도의 CSS 파일을 사용할 예정

const products = [
    { id: 1, name: '산호 칼슘 플러스', price: '34,000원', image: 'cal.avif' },
    { id: 2, name: '달맞이꽃 오일', price: '54,960원', image: 'eve.jfif' },
    { id: 3, name: '염소 오일', price: '22,000원', image: 'cream.jfif' },
    { id: 4, name: '달팽이 크림', price: '16,600원', image: 'snail.jfif' }
    // 추가 제품을 여기에 나열
];

const NewArrivals = ({ addToCart }) => {
    return (
        <div className="new-arrivals">
            <h1 className="title">New Arrivals</h1>
            <p className="subtitle">Discover the latest products in our collection</p>
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

export default NewArrivals;