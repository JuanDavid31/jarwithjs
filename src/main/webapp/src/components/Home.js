import React from 'react';

function Home() {
  return (
    <div className="page-container">
      <h2>🏠 Welcome to Java + React JAR</h2>
      <p>
        This is a demonstration of a Java application with an embedded React 17 frontend,
        all packaged into a single executable JAR file!
      </p>

      <div className="feature-grid">
        <div className="feature-card">
          <h3>🚀 Spring Boot Backend</h3>
          <p>
            Powerful Java backend using Spring Boot framework, providing REST APIs
            and serving the React application from embedded static resources.
          </p>
        </div>

        <div className="feature-card">
          <h3>⚛️ React 17 Frontend</h3>
          <p>
            Modern React 17 application with routing, state management, and API
            integration, built with Webpack and embedded in the JAR.
          </p>
        </div>

        <div className="feature-card">
          <h3>📦 Single JAR Deployment</h3>
          <p>
            Everything packaged into one executable JAR file - no need to deploy
            separate frontend and backend applications.
          </p>
        </div>

        <div className="feature-card">
          <h3>🔗 Full Integration</h3>
          <p>
            Frontend communicates with backend APIs seamlessly, with proper
            routing and static resource handling.
          </p>
        </div>
      </div>

      <div className="feature-card">
        <h3>🛠️ Technologies Used</h3>
        <div className="tech-list">
          <span className="tech-tag">Java 11</span>
          <span className="tech-tag">Spring Boot</span>
          <span className="tech-tag">React 17</span>
          <span className="tech-tag">Webpack</span>
          <span className="tech-tag">Maven</span>
          <span className="tech-tag">Babel</span>
          <span className="tech-tag">Axios</span>
          <span className="tech-tag">React Router</span>
        </div>
      </div>
    </div>
  );
}

export default Home;
