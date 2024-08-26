import React from 'react';
import './css/Footer.css'; // Import the CSS file

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3>회사 정보</h3>
          <p>(주) Medicare Network</p>
          <p> 서울시 구로구 구로동 구트아카데미</p>
          <p>Phone: 010-23XX-XXXX</p>
          <p>Email: <a href="mailto:gallawer@naver.com">gallawer@naver.com</a></p>
        </div>
        <div className="footer-section">
          <h3>협력 업체</h3>
          <p><a href="/terms-of-service">경동 제약</a></p>
          <p><a href="/privacy-policy">종근당</a></p>
        </div>
        <div className="footer-section">
          <h3>SNS 커뮤니티</h3>
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