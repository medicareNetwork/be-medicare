import React from 'react';
import './Footer.css'; // Import the CSS file

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3>Company Information</h3>
          <p>Medicare Network Inc.</p>
          <p> 123 goot Street, GuroGu, Seoul , SouthKorea</p>
          <p>Phone: (02) 777-7777</p>
          <p>Email: <a href="mailto:support@MedicareNetwork.com">support@MedicareNetwork.com</a></p>
        </div>
        <div className="footer-section">
          <h3>Customer Support</h3>
          <p><a href="/terms-of-service">Terms of Service</a></p>
          <p><a href="/privacy-policy">Privacy Policy</a></p>
        </div>
        <div className="footer-section">
          <h3>Follow Us</h3>
          <div className="social-media-links">
            <a href="https://facebook.com/MedicareNetwork" target="_blank" rel="noopener noreferrer" className="social-media-icon">Facebook</a>
            <a href="https://twitter.com/MedicareNetwork" target="_blank" rel="noopener noreferrer" className="social-media-icon">Twitter</a>
            <a href="https://instagram.com/MedicareNetwork" target="_blank" rel="noopener noreferrer" className="social-media-icon">Instagram</a>
          </div>
        </div>
        <div className="footer-section">
          <h3>Subscribe to Our Newsletter</h3>
          <form className="newsletter-form">
            <input type="email" placeholder="Enter your email address" required />
            <button type="submit">Subscribe</button>
          </form>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2024 MedicareNetwork Inc. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;