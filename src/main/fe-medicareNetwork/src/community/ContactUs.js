import React, { useState } from 'react';
import '../css/ContactUs.css';

const ContactUs = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        if (!name || !email || !message) {
            setErrorMessage('Please fill out all fields.');
            return;
        }

        setSuccessMessage('Your message has been sent successfully!');
        setErrorMessage('');
        setName('');
        setEmail('');
        setMessage('');
    };

    return (
        <div className="contact-us">
            <div className="contact-form-container">
                <h1 className="title">Contact Us</h1>
                <form className="contact-form" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <input
                            type="text"
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="Your Name"
                            className="input-field"
                        />
                    </div>
                    <div className="form-group">
                        <input
                            type="email"
                            id="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="Your Email"
                            className="input-field"
                        />
                    </div>
                    <div className="form-group">
                        <textarea
                            id="message"
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            placeholder="Your Message"
                            className="textarea-field"
                        />
                    </div>
                    <button type="submit" className="submit-btn">Send</button>
                    {successMessage && <p className="success-message">{successMessage}</p>}
                    {errorMessage && <p className="error-message">{errorMessage}</p>}
                </form>
            </div>
        </div>
    );
};

export default ContactUs;