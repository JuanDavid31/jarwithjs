import React, { useState, useEffect } from 'react';
import { Route, Switch, Link } from 'react-router-dom';
import axios from 'axios';
import './App.css';
import Home from './components/Home';
import About from './components/About';

function App() {
  const [backendInfo, setBackendInfo] = useState(null);
  const [health, setHealth] = useState(null);

  useEffect(() => {
    // Fetch backend information
    const fetchData = async () => {
      try {
        const [infoResponse, healthResponse] = await Promise.all([
          axios.get('/api/info'),
          axios.get('/api/health')
        ]);
        setBackendInfo(infoResponse.data);
        setHealth(healthResponse.data);
      } catch (error) {
        console.error('Error fetching backend data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="App">
      <header className="App-header">
        <nav className="nav-bar">
          <h1>🚀 Java + React JAR</h1>
          <div className="nav-links">
            <Link to="/">Home</Link>
            <Link to="/about">About</Link>
          </div>
        </nav>
      </header>

      <main className="main-content">
        <Switch>
          <Route exact path="/" component={Home} />
          <Route path="/about" component={About} />
        </Switch>

        {backendInfo && health && (
          <div className="status-panel">
            <h3>🔗 Backend Status</h3>
            <div className="status-grid">
              <div className="status-item">
                <strong>Health:</strong> 
                <span className={`status ${health.status === 'UP' ? 'up' : 'down'}`}>
                  {health.status}
                </span>
              </div>
              <div className="status-item">
                <strong>Backend:</strong> {backendInfo.backend}
              </div>
              <div className="status-item">
                <strong>Frontend:</strong> {backendInfo.frontend}
              </div>
              <div className="status-item">
                <strong>Embedded:</strong> {backendInfo.embedded ? '✅ Yes' : '❌ No'}
              </div>
            </div>
            <p className="message">{health.message}</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;
