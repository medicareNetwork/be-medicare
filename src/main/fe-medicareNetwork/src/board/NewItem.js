import React, { useState, useEffect } from 'react';
import '../css/SupplementList.css';
import axios from "axios";
import {useNavigate} from "react-router-dom";

const NewItem = () => {
    const navigate = useNavigate();

    const [board, setBoard] = useState({
        title: '',
        content: '',
        file: null,  // 파일은 null로 초기화합니다.
        price: '',
        name:JSON.parse(sessionStorage.getItem("member1")).email
    });

    const { title, content, file, price, name } = board;

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
        formData.append('title', title);
        formData.append('content', content);
        formData.append('price', price);
        formData.append('name',name);
        if (file) {
            formData.append('file', file);
        }

        try {
            await axios.post(`http://localhost:8090/api/write`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            alert("등록 완료");
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
        <div className="NewItem">
            <div>
                <span>제목</span>
                <input type="text" name="title" value={title} onChange={onChange}/>
            </div>
            <div>
                <span>내용</span>
                <textarea name="content" cols="30" rows="10" value={content} onChange={onChange}/>
            </div>
            <div>
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

export default NewItem;