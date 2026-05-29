import React, { useState, useEffect } from 'react';
import './index.css';
import Dashboard from './pages/Dashboard';
import MapView from './pages/MapView';
import Deliveries from './pages/Deliveries';
import Vehicles from './pages/Vehicles';
import Routes from './pages/Routes';

type Page = 'dashboard' | 'map' | 'deliveries' | 'vehicles' | 'routes';

const NavIcon = ({ page }: { page: Page }) => {
  const icons: Record<Page, JSX.Element> = {
    dashboard: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/>
        <rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/>
      </svg>
    ),
    map: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <polygon points="1 6 1 22 8 18 16 22 23 18 23 2 16 6 8 2 1 6"/>
        <line x1="8" y1="2" x2="8" y2="18"/><line x1="16" y1="6" x2="16" y2="22"/>
      </svg>
    ),
    deliveries: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/>
        <polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/>
      </svg>
    ),
    vehicles: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <rect x="1" y="3" width="15" height="13"/>
        <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/>
        <circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/>
      </svg>
    ),
    routes: (
      <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
        <circle cx="6" cy="19" r="3"/>
        <path d="M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15"/>
        <circle cx="18" cy="5" r="3"/>
      </svg>
    ),
  };
  return icons[page];
};

const NAV_ITEMS: { page: Page; label: string }[] = [
  { page: 'dashboard', label: 'Dashboard' },
  { page: 'map', label: 'Route Map' },
  { page: 'deliveries', label: 'Deliveries' },
  { page: 'vehicles', label: 'Vehicles' },
  { page: 'routes', label: 'Optimize' },
];

export default function App() {
  const [currentPage, setCurrentPage] = useState<Page>('dashboard');
  const [backendOnline, setBackendOnline] = useState<boolean | null>(null);

  // Check backend health
  useEffect(() => {
    const check = async () => {
      try {
        const res = await fetch('/api/health');
        setBackendOnline(res.ok || res.status < 500);
      } catch {
        setBackendOnline(false);
      }
    };
    check();
    const interval = setInterval(check, 10000);
    return () => clearInterval(interval);
  }, []);

  const renderPage = () => {
    switch (currentPage) {
      case 'dashboard': return <Dashboard />;
      case 'map': return <MapView />;
      case 'deliveries': return <Deliveries />;
      case 'vehicles': return <Vehicles />;
      case 'routes': return <Routes />;
    }
  };

  return (
    <div className="app-layout">
      {/* Sidebar */}
      <nav className="sidebar">
        <div className="sidebar-logo">
          <div className="logo-title">🗺️ RouteOptima</div>
          <div className="logo-subtitle">Smart Delivery System</div>
        </div>

        <div className="sidebar-nav">
          {NAV_ITEMS.map(({ page, label }) => (
            <button
              key={page}
              id={`nav-${page}`}
              className={`nav-item ${currentPage === page ? 'active' : ''}`}
              onClick={() => setCurrentPage(page)}
            >
              <NavIcon page={page} />
              <span>{label}</span>
              {page === 'routes' && (
                <span style={{
                  marginLeft: 'auto',
                  fontSize: '0.65rem',
                  background: 'rgba(59,130,246,0.2)',
                  color: '#3b82f6',
                  padding: '2px 6px',
                  borderRadius: 4,
                  fontWeight: 700,
                }}>
                  AI
                </span>
              )}
            </button>
          ))}
        </div>

        <div className="sidebar-footer">
          <div className="status-badge">
            <div
              className="status-dot"
              style={{
                background: backendOnline === null ? '#f59e0b' :
                  backendOnline ? '#10b981' : '#f43f5e',
                boxShadow: `0 0 8px ${backendOnline === null ? '#f59e0b' : backendOnline ? '#10b981' : '#f43f5e'}`,
              }}
            />
            <span>
              {backendOnline === null ? 'Connecting...' :
                backendOnline ? 'Backend Online' : 'Backend Offline'}
            </span>
          </div>
          <div style={{ marginTop: 8, fontSize: '0.65rem', color: 'var(--text-muted)' }}>
            Spring Boot :8080 · H2 DB
          </div>
          <div style={{ marginTop: 4, fontSize: '0.65rem', color: 'var(--text-muted)' }}>
            Dijkstra + Greedy + 2-Opt
          </div>
        </div>
      </nav>

      {/* Main content */}
      <main className="main-content">
        {backendOnline === false && (
          <div className="alert alert-warning" style={{ margin: '16px 32px 0', borderRadius: 12 }}>
            ⚠️ Cannot connect to backend (localhost:8080). Make sure Spring Boot is running.
          </div>
        )}
        {renderPage()}
      </main>
    </div>
  );
}
