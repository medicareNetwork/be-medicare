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
        <div className="Modify">
            <div>
                <span>제목</span>
                <input type="text" name="title" value={title} onChange={onChange}/>
            </div>
            <div>
                <span>내용</span>
                <textarea name="content" cols="30" rows="10" value={content} onChange={onChange}/>
            </div>
            <div>
                <p>기존 파일
                    : {product.filename && product.filename.includes("_") ? product.filename.split("_")[1] : '파일 없음'}</p>
                <input type="file" name="file" onChange={onChange}/>
            </div>
            <div>
                <span>가격</span>
                <input type="text" name="price" value={price} onChange={onChange}/>
            </div>
            <div>
                <button onClick={saveBoard}>저장</button>
                <button onClick={backToList}>취소</button>
            </div>
        </div>
    );
};

export default Modify;