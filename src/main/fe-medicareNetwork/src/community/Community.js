import React, { useState } from 'react';
import '../css/Community.css'; // 게시판의 스타일을 위한 CSS 파일

const Community = () => {
    const [posts, setPosts] = useState([
        { id: 1, title: 'Welcome to the Community!', content: 'Feel free to share your thoughts and experiences.' },
        { id: 2, title: 'How to Choose the Right Supplement', content: 'Tips and advice on selecting the best supplements for your needs.' }
    ]);
    const [newPost, setNewPost] = useState({ title: '', content: '' });

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewPost(prevPost => ({ ...prevPost, [name]: value }));
    };

    const handleAddPost = () => {
        if (newPost.title && newPost.content) {
            setPosts(prevPosts => [
                ...prevPosts,
                { id: prevPosts.length + 1, title: newPost.title, content: newPost.content }
            ]);
            setNewPost({ title: '', content: '' });
        }
    };

    return (
        <div className="community">
            <h1 className="title">Community Board</h1>
            <div className="post-form">
                <h2>Create a New Post</h2>
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
                <button onClick={handleAddPost}>Add Post</button>
            </div>
            <div className="posts-list">
                {posts.map(post => (
                    <div key={post.id} className="post">
                        <h2 className="post-title">{post.title}</h2>
                        <p className="post-content">{post.content}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Community;