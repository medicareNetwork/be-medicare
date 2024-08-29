import React, {useState} from 'react';
import '../css/BestSellers.css';
import {CartPlusFill} from "react-bootstrap-icons"; // 별도의 CSS 파일을 사용할 예정\
import handleAddToCart from "./SupplementList";
import handleQuantityChange from "./SupplementList";

const BestSellers = ({ addToCart , bestList}) => {
    const [quantities, setQuantities] = useState({}); // 수량을 저장할 상태

    return (

        <div className="best-sellers">
            <h1 className="title">Best Sellers</h1>
            <p className="subtitle">Explore our top-selling products</p>
            <div className="supplement-list">
                {bestList.map((item) => (
                    <div key={item.id} className="supplement-item">
                        <img src={`${item.filepath}`} alt={item.filename} className="product-image"/>
                        <div className="supplement-info">
                            <h2 className="supplement-description">{item.title}</h2>
                            <p className="supplement-price">{item.price}</p>
                            <div className="d-flex justify-content-between align-items-center mt-3">
                                <button
                                    className="btn btn-dark add-to-cart-button ml-2"
                                    onClick={() => handleAddToCart(item)}
                                >
                                    장바구니 담기 <CartPlusFill size={20}/>
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default BestSellers;