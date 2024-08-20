import React from 'react';
import '../css/SaleItems.css'; // 별도의 CSS 파일을 사용할 예정

const products = [
    { id: 1, name: 'Albumin', price: '$800', image: 'albumin.webp' },
    { id: 2, name: 'collagen', price: '$19.99', image: 'collagen.jpg' },
    { id: 3, name: 'Vitamin C', price: '$24.99', image: 'vitaminc.jpg' },
    { id: 4, name: 'RoyalJelly', price: '$25.99', image: 'royal.jpg' }
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
                        <img src={`/images/${product.image}`} alt={product.name} className="product-image" />
                        <h2 className="product-name">{product.name}</h2>
                        <p className="product-price">{product.price}</p>
                        <button onClick={() => addToCart(product)} className="add-to-cart-btn">Add to Cart</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default SaleItems;