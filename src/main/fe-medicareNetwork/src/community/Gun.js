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
            const response = await axios.get('http://localhost:8090/api/board2/list');
            setPosts(response.data.content);
        } catch (error) {
            console.error('Failed to fetch posts', error);
        }
    };

    const handleAddPostClick = () => {
        navigate('/Gun/add');
    };

    const handleEditPostClick = (id) => {
        navigate(`/Gun/edit/${id}`);
    };

    const handleDeletePost = async (id) => {
        try {
            await axios.delete(`http://localhost:8090/api/board2/delete/${id}`);
            fetchPosts();
        } catch (error) {
            console.error('Failed to delete post', error);
        }
    };

    return (
        <div className="container my-5" style={{ width: 700, margin: '0 auto' }}>
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h1 className="text-dark">건의사항</h1>
                <button className="btn btn-dark" onClick={handleAddPostClick}>글쓰기</button>
            </div>

            <div className="posts-list">
                {posts.map(post => (
                    <div key={post.id} className="card mb-3 bg-dark text-light">
                        <div className="card-body">
                            <h2 className="card-title">{post.title}</h2>
                            <p className="card-text">{post.content}</p>
                            <div className="d-flex justify-content-end">
                                <button
                                    className="btn btn-outline-danger me-2"
                                    onClick={() => handleDeletePost(post.id)}
                                >
                                    Delete
                                </button>
                                <button
                                    className="btn btn-outline-light"
                                    onClick={() => handleEditPostClick(post.id)}
                                >
                                    Edit
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Gong;
