import React, { useState, useEffect } from 'react';
import '../css/SupplementList.css';
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";

const Modify = () => {
    const navigate = useNavigate();

    const { state } = useLocation();

    // product가 정의되지 않았을 때 기본값 설정
    const product = state ? state.item : {};
    const [board, setBoard] = useState({
        title: product.title || '',
        content: product.content || '',
        file: null,  // 파일은 null로 초기화합니다.
        price: product.price || '',
        name: product.name || ''
    });

    const {title, content, file, price, name } = board;

    const onChange = (event) => {
        const { name, value, type, files } = event.target;

        if (type === 'file') {
            setBoard({
                ...board,
                file: files[0],  // 선택된 첫 번째 파일만 저장
            });
        } else {
            setBoard({
                ...board,
                [name]: value,
            });
        }
    };

    const saveBoard = async () => {
        const formData = new FormData();
        formData.append('id',product.id);
        formData.append('title', title);
        formData.append('content', content);
        formData.append('price', price);
        if (file) {
            formData.append('file', file);
        }

        try {
            await axios.post(`http://localhost:8090/api/modify`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            alert("수정 완료");
            navigate("/");
        } catch (error) {
            console.error("저장 중 오류 발생:", error);
            alert("등록 실패");
        }
    };

    const backToList = () => {
        navigate("/");
    };


    return (
        <div className="container mt-5 d-flex justify-content-center">
            <div className="card bg-dark text-white" style={{ width: '50%' }}>
                <div className="card-header text-center">
                    <h2>상품 수정</h2>
                </div>
                <div className="card-body">
                    <div className="mb-3">
                        <label htmlFor="title" className="form-label">제목</label>
                        <input
                            type="text"
                            name="title"
                            value={title}
                            onChange={onChange}
                            className="form-control bg-dark text-white border-light"
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="content" className="form-label">내용</label>
                        <textarea
                            name="content"
                            cols="30"
                            rows="5"
                            value={content}
                            onChange={onChange}
                            className="form-control bg-dark text-white border-light"
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="file" className="form-label">이미지 업로드</label>
                        <input
                            type="file"
                            name="file"
                            onChange={onChange}
                            className="form-control bg-dark text-white border-light"
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="price" className="form-label">가격</label>
                        <input
                            type="text"
                            name="price"
                            value={price}
                            onChange={onChange}
                            className="form-control bg-dark text-white border-light"
                        />
                    </div>
                    <div className="mb-3">
                        <p>기존 파일: {product.filename && product.filename.includes("_") ? product.filename.split("_")[1] : '파일 없음'}</p>
                    </div>
                    <div className="d-flex justify-content-between">
                        <button onClick={saveBoard} className="btn btn-success">저장</button>
                        <button onClick={backToList} className="btn btn-secondary">취소</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Modify;