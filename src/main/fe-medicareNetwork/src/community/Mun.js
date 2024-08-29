import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const Gong = () => {
    const [posts, setPosts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchPosts();
    }, []);

    const fetchPosts = async () => {
        try {
            const response = await axios.get('http://localhost:8090/api/board3/list');
            setPosts(response.data.content);
        } catch (error) {
            console.error('Failed to fetch posts', error);
        }
    };

    const handleAddPostClick = () => {
        navigate('/Mun/add');
    };

    const handleEditPostClick = (id) => {
        navigate(`/Mun/edit/${id}`);
    };

    const handleDeletePost = async (id) => {
        try {
            await axios.delete(`http://localhost:8090/api/board3/delete/${id}`);
            fetchPosts();
        } catch (error) {
            console.error('Failed to delete post', error);
        }
    };

    return (
        <div>
            <h1>문의사항</h1>
            <button onClick={handleAddPostClick}>글쓰기</button>

            <div className="posts-list">
                {posts.map(post => (
                    <div key={post.id} className="post">
                        <h2>{post.title}</h2>
                        <p>{post.content}</p>
                        <button onClick={() => handleDeletePost(post.id)}>Delete</button>
                        <button onClick={() => handleEditPostClick(post.id)}>Edit</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Gong;
