import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const AddPost = () => {
    const [newPost, setNewPost] = useState({ title: '', content: '' });
    const navigate = useNavigate();

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewPost(prevPost => ({ ...prevPost, [name]: value }));
    };

    const handleAddPost = async () => {
        try {
            const formData = new FormData();
            formData.append('title', newPost.title);
            formData.append('content', newPost.content);

            await axios.post('http://localhost:8090/api/board1/write', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            navigate('/gong'); // 글 작성 후 Gong 페이지로 이동
        } catch (error) {
            console.error('Failed to add post', error);
        }
    };

    return (
        <div>
            <h1>공지사항 작성중입니다</h1>
            <input
                type="text"
                name="title"
                value={newPost.title}
                onChange={handleInputChange}
                placeholder="Post Title"
            />
            <textarea
                name="content"
                value={newPost.content}
                onChange={handleInputChange}
                placeholder="Post Content"
            />
            <button id="t" onClick={handleAddPost}>작성완료</button>
        </div>
    );
};

export default AddPost;
