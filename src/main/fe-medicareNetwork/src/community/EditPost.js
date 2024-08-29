import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';


const EditPost = () => {
    const { id } = useParams();
    const [editPost, setEditPost] = useState({ title: '', content: '' });
    const navigate = useNavigate();

    useEffect(() => {
        fetchPost();
    }, [id]);

    const fetchPost = async () => {
        try {
            const response = await axios.get(`http://localhost:8090/api/board1/view/${id}`);
            setEditPost(response.data);
        } catch (error) {
            console.error('Failed to fetch post', error);
        }
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setEditPost(prevPost => ({ ...prevPost, [name]: value }));
    };

    const handleEditPost = async () => {
        try {
            const formData = new FormData();
            formData.append('title', editPost.title);
            formData.append('content', editPost.content);

            await axios.put(`http://localhost:8090/api/board1/modify/${id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            navigate('/gong'); // 글 수정 후 Gong 페이지로 이동
        } catch (error) {
            console.error('Failed to edit post', error);
        }
    };

    return (
        <div>
            <h1>Edit Post</h1>
            <input
                type="text"
                name="title"
                value={editPost.title}
                onChange={handleInputChange}
                placeholder="Post Title"
            />
            <textarea
                name="content"
                value={editPost.content}
                onChange={handleInputChange}
                placeholder="Post Content"
            />
            <button onClick={handleEditPost}>Save Changes</button>
        </div>
    );
};

export default EditPost;
