import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const AddPost1 = () => {
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

            await axios.post('http://localhost:8090/api/board2/write', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            navigate('/Gun'); // 글 작성 후 Gong 페이지로 이동
        } catch (error) {
            console.error('Failed to add post', error);
        }
    };

    return (
        <div className="container my-5" style={{ width: '50%', margin: '0 auto' }}>
            <h2 className="text-dark mb-4">건의사항을 작성해주세요 :)</h2>
            제목
            <div className="form-group mb-3">
                <input
                    type="text"
                    name="title"
                    value={newPost.title}
                    onChange={handleInputChange}
                    placeholder="Post Title"
                    className="form-control"
                />
            </div>
            내용
            <div className="form-group mb-4">
                <textarea
                    name="content"
                    value={newPost.content}
                    onChange={handleInputChange}
                    placeholder="Post Content"
                    className="form-control"
                    rows="5"
                />
            </div>

            <button
                onClick={handleAddPost}
                className="btn btn-dark btn-block"
            >
                작성완료
            </button>
        </div>
    );
};

export default AddPost1;
