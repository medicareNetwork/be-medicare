import React from 'react';
import './css/SupplementButton.css';

const SupplementButton = () => {
  return (
    <div className="supplement-button-container">
      <p className="supplement-description">
        Not sure which supplements are right for you? <br />
        Click below for personalized recommendations.
      </p>
      <button className="supplement-button">
        Get a Supplement Recommendation
      </button>
    </div>
  );
};

export default SupplementButton;