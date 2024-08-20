import React from 'react';
import '../css/NewArrivals.css'; // 별도의 CSS 파일을 사용할 예정

const products = [
    { id: 1, name: 'Coral Calcium', price: '$29.99', image: 'cal.avif' },
    { id: 2, name: 'Evening Primrose Oil', price: '$49.99', image: 'eve.jfif' },
    { id: 3, name: 'Goat Milk Horse Oil', price: '$19.99', image: 'cream.jfif' },
    { id: 4, name: 'Black Snail Cream', price: '$19.99', image: 'snail.jfif' }
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

export default NewArrivals;