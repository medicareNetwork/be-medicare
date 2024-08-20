import React from 'react';
import '../css/BestSellers.css'; // 별도의 CSS 파일을 사용할 예정

const products = [
    { id: 1, name: 'Vitamin C', price: '$39.99', image: 'vitaminc.jpg' },
    { id: 2, name: 'Omega 3', price: '$59.99', image: 'omega.webp' },
    { id: 3, name: 'Protein Powder', price: '$29.99', image: 'protein.webp' },
    { id: 4, name: 'Collagen', price: '$49.99', image: 'collagen.jpg' }
    // 추가 제품을 여기에 나열
];

const BestSellers = ({ addToCart , bestList}) => {
    return (
        <div className="best-sellers">
            <h1 className="title">Best Sellers</h1>
            <p className="subtitle">Explore our top-selling products</p>
            <div className="products-grid">
                {bestList.map((product, index) => (
                    <div key={index} className="product-card">
                        <img src={`${product.filepath}`} alt={product.filename} className="product-image" />
                        <h2 className="product-name">{product.title}</h2>
                        <p className="product-price">{product.price}</p>
                        <button onClick={() => addToCart(product)} className="add-to-cart-btn">Add to Cart</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default BestSellers;