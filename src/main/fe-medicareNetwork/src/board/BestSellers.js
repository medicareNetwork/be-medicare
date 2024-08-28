import React from 'react';
import '../css/BestSellers.css'; // 별도의 CSS 파일을 사용할 예정

const BestSellers = ({ addToCart , bestList}) => {
    return (
        <div className="best-sellers">
            <h1 className="title">Best Sellers</h1>
            <p className="subtitle">Explore our top-selling products</p>
            <div className="supplement-list">
                {bestList.map((product) => (
                    <div key={product.id} className="supplement-item">
                        <img src={`${product.filepath}`} alt={product.filename} className="product-image"/>
                        <div className="supplement-info">
                            <h2 className="supplement-description">{product.title}</h2>
                            <p className="supplement-price">{product.price}</p>
                            <button onClick={() => addToCart(product)} className="add-to-cart-btn">Add to Cart</button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default BestSellers;