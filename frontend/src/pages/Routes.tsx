import React, { useEffect, useState, useCallback } from 'react';
import { routeApi } from '../services/api';
import type { Route, OptimizationResponse } from '../types';

const ROUTE_COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#f43f5e', '#06b6d4'];

function RouteCard({ route, color }: { route: Route; color: string }) {
  const [expanded, setExpanded] = useState(false);

  return (
    <div className="route-card">
      <div className="route-card-header" onClick={() => setExpanded(!expanded)}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 14 }}>
          <div style={{
            width: 40,
            height: 40,
            borderRadius: 10,
            background: color + '22',
            border: `1px solid ${color}44`,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '1.2rem',
          }}>
            🚚
          </div>
          <div>
            <div style={{ fontWeight: 700, color: 'var(--text-primary)', fontSize: '0.95rem' }}>
              {route.vehicle?.name ?? 'Unknown Vehicle'}
            </div>
            <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontFamily: 'JetBrains Mono, monospace' }}>
              {route.routeId} · {route.deliveries?.length ?? 0} stops
            </div>
          </div>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <div style={{ textAlign: 'right' }}>
            <div style={{ fontSize: '0.9rem', fontWeight: 700, color }}>
              {(route.totalDistance || 0).toFixed(1)} km
            </div>
            <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
              ₹{(route.totalCost || 0).toFixed(0)} · {Math.round(route.totalTime || 0)}min
            </div>
          </div>
          <span className={`badge badge-${route.status?.toLowerCase()}`}>
            {route.status}
          </span>
          <span style={{ color: 'var(--text-muted)', fontSize: '1rem' }}>
            {expanded ? '▲' : '▼'}
          </span>
        </div>
      </div>

      {expanded && (
        <div className="route-card-body">
          {/* Route metrics */}
          <div style={{ display: 'flex', gap: 16, marginBottom: 16, flexWrap: 'wrap' }}>
            {[
              { label: 'Distance', value: `${(route.totalDistance||0).toFixed(2)} km`, color: '#3b82f6' },
              { label: 'Est. Time', value: `${Math.round(route.totalTime||0)} min`, color: '#8b5cf6' },
              { label: 'Fuel Cost', value: `₹${(route.totalCost||0).toFixed(2)}`, color: '#f59e0b' },
              { label: 'Deliveries', value: route.deliveries?.length ?? 0, color: '#10b981' },
            ].map(m => (
              <div key={m.label} style={{
                background: 'rgba(255,255,255,0.03)',
                borderRadius: 8,
                padding: '10px 14px',
                flex: '1 1 100px',
                minWidth: 100,
              }}>
                <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                  {m.label}
                </div>
                <div style={{ fontSize: '1rem', fontWeight: 700, color: m.color as string, marginTop: 2 }}>
                  {m.value}
                </div>
              </div>
            ))}
          </div>

          {/* Waypoint sequence */}
          <div style={{ fontSize: '0.8rem', color: 'var(--text-secondary)', marginBottom: 8, fontWeight: 600 }}>
            Delivery Sequence:
          </div>
          <div style={{ position: 'relative', paddingLeft: 20 }}>
            {/* Vertical line */}
            <div style={{
              position: 'absolute',
              left: 4,
              top: 8,
              bottom: 8,
              width: 2,
              background: `linear-gradient(to bottom, ${color}, transparent)`,
            }} />

            {/* Start - warehouse */}
            <div className="route-waypoint">
              <div className="route-waypoint-dot" style={{ background: color, boxShadow: `0 0 6px ${color}` }} />
              <div>
                <div style={{ color: color, fontWeight: 600 }}>🏭 Warehouse (Start)</div>
              </div>
            </div>

            {/* Delivery stops */}
            {route.deliveries?.map((d, i) => (
              <div className="route-waypoint" key={d.deliveryId}>
                <div className="route-waypoint-dot" style={{ background: 'rgba(255,255,255,0.3)', border: `1px solid ${color}` }} />
                <div style={{ flex: 1, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <span style={{ color: 'var(--text-primary)' }}>
                      📦 Stop {i+1}: {d.deliveryLocation?.name}
                    </span>
                    <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
                      {d.weight}kg · {d.deliveryId}
                    </div>
                  </div>
                  <span className={`badge priority-${d.priority}`}>P{d.priority}</span>
                </div>
              </div>
            ))}

            {/* Return */}
            <div className="route-waypoint">
              <div className="route-waypoint-dot" style={{ background: color, opacity: 0.5 }} />
              <div style={{ color: 'var(--text-muted)' }}>🏭 Warehouse (Return)</div>
            </div>
          </div>

          {/* Action buttons */}
          <div style={{ display: 'flex', gap: 10, marginTop: 16 }}>
            {route.status === 'PLANNED' && (
              <button
                className="btn btn-primary btn-sm"
                onClick={async () => {
                  if (route.id) await routeApi.startRoute(route.id);
                }}
              >
                ▶️ Start Route
              </button>
            )}
            {route.status === 'IN_PROGRESS' && (
              <button
                className="btn btn-emerald btn-sm"
                onClick={async () => {
                  if (route.id) await routeApi.completeRoute(route.id);
                }}
              >
                ✅ Complete Route
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default function Routes() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [optimizing, setOptimizing] = useState(false);
  const [result, setResult] = useState<OptimizationResponse | null>(null);

  const fetchRoutes = useCallback(async () => {
    setLoading(true);
    try {
      const data = await routeApi.getAll().catch(() => []);
      setRoutes(data);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchRoutes(); }, [fetchRoutes]);

  const handleOptimize = async () => {
    setOptimizing(true);
    setResult(null);
    try {
      const res = await routeApi.optimize('W001');
      setResult(res);
      fetchRoutes();
    } catch (e: any) {
      setResult({
        success: false,
        message: e?.response?.data?.message || 'Optimization failed',
        totalDistance: 0,
        totalCost: 0,
        capacityUtilization: 0,
        executionTimeMs: 0,
      });
    } finally {
      setOptimizing(false);
    }
  };

  const totalDist = routes.reduce((s, r) => s + (r.totalDistance || 0), 0);
  const totalCost = routes.reduce((s, r) => s + (r.totalCost || 0), 0);

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Routes</h1>
          <p className="page-subtitle">{routes.length} routes · {totalDist.toFixed(1)} km · ₹{totalCost.toFixed(0)} total cost</p>
        </div>
        <button className="btn btn-outline btn-sm" onClick={fetchRoutes} disabled={loading}>
          🔄 Refresh
        </button>
      </div>

      <div className="page-body">
        {/* Optimization Panel */}
        <div className="optimize-panel" style={{ marginBottom: 28 }}>
          <div style={{ fontSize: '2rem', marginBottom: 8 }}>⚡</div>
          <div className="optimize-title">
            <span style={{ background: 'linear-gradient(135deg,#3b82f6,#8b5cf6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              VRP Route Optimizer
            </span>
          </div>
          <p style={{ color: 'var(--text-secondary)', fontSize: '0.9rem', maxWidth: 480, margin: '0 auto' }}>
            Automatically assign pending deliveries to vehicles using Dijkstra + Greedy + 2-Opt algorithms.
            Minimizes total distance, cost, and time.
          </p>

          {result && (
            <div
              className={`alert ${result.success ? 'alert-success' : 'alert-error'}`}
              style={{ marginTop: 20, textAlign: 'left', maxWidth: 600, margin: '20px auto 0' }}
            >
              <div>
                <strong>{result.success ? '✅ Optimization Complete!' : '❌ ' + result.message}</strong>
                {result.success && (
                  <div style={{ marginTop: 8, fontSize: '0.85rem', display: 'flex', gap: 20, flexWrap: 'wrap' }}>
                    <span>📏 {result.totalDistance.toFixed(2)} km</span>
                    <span>💰 ₹{result.totalCost.toFixed(2)}</span>
                    <span>🏭 {result.capacityUtilization.toFixed(1)}% utilization</span>
                    <span>⏱ {result.executionTimeMs}ms</span>
                  </div>
                )}
              </div>
            </div>
          )}

          <div className="optimize-btn-wrapper">
            <button
              className="btn btn-primary btn-lg"
              onClick={handleOptimize}
              disabled={optimizing}
              style={{ minWidth: 220, justifyContent: 'center' }}
            >
              {optimizing ? (
                <><span className="spinner"></span> Optimizing Routes...</>
              ) : (
                <>⚡ Run Optimization</>
              )}
            </button>
          </div>
        </div>

        {/* Routes list */}
        {loading ? (
          <div className="empty-state">
            <div className="spinner" style={{ width: 36, height: 36, margin: '0 auto 16px' }}></div>
            <p>Loading routes...</p>
          </div>
        ) : routes.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">🗺️</div>
            <div className="empty-state-title">No routes yet</div>
            <p style={{ fontSize: '0.875rem', marginTop: 8 }}>
              Click <strong>"Run Optimization"</strong> above to generate optimized delivery routes.
            </p>
          </div>
        ) : (
          <div>
            <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)', marginBottom: 16 }}>
              {routes.length} optimized routes — click any route to expand
            </div>
            {routes.map((route, idx) => (
              <RouteCard
                key={route.routeId}
                route={route}
                color={ROUTE_COLORS[idx % ROUTE_COLORS.length]}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
