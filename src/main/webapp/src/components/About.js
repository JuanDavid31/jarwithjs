import React from 'react';

function About() {
  return (
    <div className="page-container">
      <h2>📋 About This Project</h2>
      <p>
        This project demonstrates how to create a Java application that embeds
        a React frontend within a single JAR file, perfect for deployment scenarios
        where you want everything bundled together.
      </p>

      <div className="feature-card">
        <h3>🏗️ Project Structure</h3>
        <p>The project follows a standard Maven structure with these key components:</p>
        <ul>
          <li><strong>src/main/java/</strong> - Spring Boot Java application</li>
          <li><strong>src/main/webapp/</strong> - React 17 frontend source code</li>
          <li><strong>src/main/resources/</strong> - Spring Boot configuration</li>
          <li><strong>pom.xml</strong> - Maven build configuration with frontend plugin</li>
        </ul>
      </div>

      <div className="feature-card">
        <h3>⚙️ Build Process</h3>
        <p>The Maven build process automatically:</p>
        <ul>
          <li>Installs Node.js and npm during the build</li>
          <li>Runs <code>npm install</code> to get React dependencies</li>
          <li>Executes <code>npm run build</code> to create production React build</li>
          <li>Copies React build files to Spring Boot static resources</li>
          <li>Packages everything into a single executable JAR</li>
        </ul>
      </div>

      <div className="feature-card">
        <h3>🌐 API Endpoints</h3>
        <p>The backend provides these REST endpoints:</p>
        <ul>
          <li><strong>GET /api/health</strong> - Application health check</li>
          <li><strong>GET /api/info</strong> - Application information</li>
          <li><strong>GET /*</strong> - Serves React app for SPA routing</li>
        </ul>
      </div>

      <div className="feature-card">
        <h3>🚀 How to Run</h3>
        <p>To build and run this application:</p>
        <ol>
          <li>Run <code>mvn clean package</code> to build the JAR</li>
          <li>Execute <code>java -jar target/java-react-jar-1.0.0.jar</code></li>
          <li>Open <code>http://localhost:8080</code> in your browser</li>
        </ol>
      </div>
    </div>
  );
}

export default About;
