import React, { useState, useEffect } from 'react';
import '../css/SupplementList.css';
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";

const Views = () => {
    const navigate = useNavigate();

    const {state} = useLocation();

    const product = state ? state.item : {};

    const [quantities, setQuantities] = useState({}); // 수량을 저장할 상태

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

    const backCart = async (item) =>{
        handleAddToCart(item);
        navigate("/");
    }

    const onCart = async (item) => {
        handleAddToCart(item);
        navigate("/Cart");
    };

    return (
        <div className="container my-5">
            <div className="row">
                <div className="col-md-6 text-center mb-4">
                    <img src={product.filepath} alt={product.filename} className="img-fluid rounded shadow-sm" />
                </div>
                <div className="col-md-6">
                    <div className="bg-dark text-light p-4 rounded shadow-sm">
                        <h2>{product.title}</h2>
                        <p>{product.content}</p>
                        <p><strong>판매자:</strong> {product.name}</p>
                        <div className="mt-4">
                            <div className="d-flex align-items-center mb-3">
                                <input
                                    type="number"
                                    min="1"
                                    value={quantities[product.id] || 1}
                                    onChange={(e) => handleQuantityChange(product.id, parseInt(e.target.value))}
                                    className="form-control"
                                    style={{maxWidth: '70px'}}  // 가로 사이즈를 줄이기 위해 maxWidth 적용
                                />
                            </div>
                            <div className="d-flex justify-content-between">
                                <button
                                    onClick={() => backCart(product)}
                                    className="btn btn-outline-light mr-2"
                                    style={{flex: '0 0 45%'}}
                                >
                                    장바구니 담기
                                </button>
                                <button
                                    onClick={() => onCart(product)}
                                    className="btn btn-light ml-2"
                                    style={{flex: '0 0 45%'}}
                                >
                                    바로구매
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default Views;