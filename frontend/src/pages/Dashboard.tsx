import React, { useEffect, useState, useCallback } from 'react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts';
import { TruckIcon, PackageIcon, RouteIcon, ActivityIcon, RefreshCwIcon, ZapIcon } from 'lucide-react';
import { deliveryApi, vehicleApi, routeApi } from '../services/api';
import type { Delivery, Vehicle, Route } from '../types';

// Lucide doesn't have RouteIcon/PackageIcon by default names – use what's available
const Icons = {
  Truck: (p: any) => <svg {...p} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="1" y="3" width="15" height="13"/><polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg>,
  Package: (p: any) => <svg {...p} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="16.5" y1="9.4" x2="7.5" y2="4.21"/><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg>,
  Route: (p: any) => <svg {...p} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="6" cy="19" r="3"/><path d="M9 19h8.5a3.5 3.5 0 0 0 0-7h-11a3.5 3.5 0 0 1 0-7H15"/><circle cx="18" cy="5" r="3"/></svg>,
  Activity: (p: any) => <svg {...p} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>,
  Zap: (p: any) => <svg {...p} xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>,
};

interface Stats {
  totalDeliveries: number;
  pendingDeliveries: number;
  deliveredCount: number;
  totalVehicles: number;
  idleVehicles: number;
  totalRoutes: number;
  totalDistance: number;
  totalCost: number;
}

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#f43f5e'];

export default function Dashboard() {
  const [stats, setStats] = useState<Stats | null>(null);
  const [deliveries, setDeliveries] = useState<Delivery[]>([]);
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [lastRefresh, setLastRefresh] = useState<Date>(new Date());

  const fetchAll = useCallback(async () => {
    setLoading(true);
    try {
      const [dels, vehs, rts] = await Promise.all([
        deliveryApi.getAll().catch(() => []),
        vehicleApi.getAll().catch(() => []),
        routeApi.getAll().catch(() => []),
      ]);
      setDeliveries(dels);
      setVehicles(vehs);
      setRoutes(rts);
      setStats({
        totalDeliveries: dels.length,
        pendingDeliveries: dels.filter(d => d.status === 'PENDING').length,
        deliveredCount: dels.filter(d => d.status === 'DELIVERED').length,
        totalVehicles: vehs.length,
        idleVehicles: vehs.filter(v => v.status === 'IDLE').length,
        totalRoutes: rts.length,
        totalDistance: rts.reduce((s, r) => s + (r.totalDistance || 0), 0),
        totalCost: rts.reduce((s, r) => s + (r.totalCost || 0), 0),
      });
      setLastRefresh(new Date());
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchAll(); }, [fetchAll]);

  // Priority distribution chart data
  const priorityData = [1,2,3,4,5].map(p => ({
    priority: `P${p}`,
    count: deliveries.filter(d => d.priority === p).length,
    color: COLORS[p-1]
  }));

  // Status distribution
  const statusData = ['PENDING', 'ASSIGNED', 'IN_TRANSIT', 'DELIVERED'].map(s => ({
    name: s.replace('_', ' '),
    value: deliveries.filter(d => d.status === s).length,
  }));

  // Recent route distances as line chart
  const routeDistData = routes.slice(0, 10).map((r, i) => ({
    route: r.routeId || `R${i+1}`,
    distance: parseFloat((r.totalDistance || 0).toFixed(1)),
    cost: parseFloat((r.totalCost || 0).toFixed(0)),
  }));

  if (loading && !stats) {
    return (
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '60vh' }}>
        <div style={{ textAlign: 'center' }}>
          <div className="spinner" style={{ width: 40, height: 40, margin: '0 auto 16px' }}></div>
          <p style={{ color: 'var(--text-secondary)' }}>Loading dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="page-header">
        <div>
          <h1 className="page-title">Dashboard</h1>
          <p className="page-subtitle">
            Last updated: {lastRefresh.toLocaleTimeString()} &nbsp;·&nbsp;
            Real-time route optimization metrics
          </p>
        </div>
        <button className="btn btn-outline btn-sm" onClick={fetchAll} disabled={loading}>
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <polyline points="23 4 23 10 17 10"/><polyline points="1 20 1 14 7 14"/>
            <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"/>
          </svg>
          {loading ? 'Refreshing...' : 'Refresh'}
        </button>
      </div>

      <div className="page-body">
        {/* Stat Cards */}
        <div className="stat-grid" style={{ marginBottom: 28 }}>
          <div className="stat-card blue animate-in">
            <div className="stat-icon blue">
              <Icons.Package width={22} height={22} />
            </div>
            <div className="stat-value">{stats?.totalDeliveries ?? 0}</div>
            <div className="stat-label">Total Deliveries</div>
            <div className="stat-change up">
              ↑ {stats?.pendingDeliveries ?? 0} pending · {stats?.deliveredCount ?? 0} delivered
            </div>
          </div>

          <div className="stat-card emerald animate-in" style={{ animationDelay: '0.1s' }}>
            <div className="stat-icon emerald">
              <Icons.Truck width={22} height={22} />
            </div>
            <div className="stat-value">{stats?.totalVehicles ?? 0}</div>
            <div className="stat-label">Fleet Vehicles</div>
            <div className="stat-change up">
              {stats?.idleVehicles ?? 0} idle · {(stats?.totalVehicles ?? 0) - (stats?.idleVehicles ?? 0)} active
            </div>
          </div>

          <div className="stat-card violet animate-in" style={{ animationDelay: '0.15s' }}>
            <div className="stat-icon violet">
              <Icons.Route width={22} height={22} />
            </div>
            <div className="stat-value">{stats?.totalRoutes ?? 0}</div>
            <div className="stat-label">Optimized Routes</div>
            <div className="stat-change up">
              {stats?.totalDistance.toFixed(1) ?? '0'} km total
            </div>
          </div>

          <div className="stat-card amber animate-in" style={{ animationDelay: '0.2s' }}>
            <div className="stat-icon amber">
              <Icons.Activity width={22} height={22} />
            </div>
            <div className="stat-value">₹{stats?.totalCost.toFixed(0) ?? '0'}</div>
            <div className="stat-label">Total Fuel Cost</div>
            <div className="stat-change">
              Across all routes
            </div>
          </div>

          <div className="stat-card rose animate-in" style={{ animationDelay: '0.25s' }}>
            <div className="stat-icon rose">
              <Icons.Zap width={22} height={22} />
            </div>
            <div className="stat-value">
              {stats?.totalDeliveries
                ? Math.round((stats.deliveredCount / stats.totalDeliveries) * 100)
                : 0}%
            </div>
            <div className="stat-label">Delivery Rate</div>
            <div className="stat-change up">Completion rate</div>
          </div>
        </div>

        {/* Charts Row */}
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 20, marginBottom: 28 }}>
          {/* Priority Distribution */}
          <div className="card">
            <div className="card-header">
              <span className="card-title">Deliveries by Priority</span>
            </div>
            <div className="card-body">
              {deliveries.length === 0 ? (
                <div className="empty-state" style={{ padding: '20px' }}>
                  <p>No delivery data available</p>
                </div>
              ) : (
                <ResponsiveContainer width="100%" height={200}>
                  <BarChart data={priorityData} barSize={32}>
                    <XAxis dataKey="priority" stroke="#475569" tick={{ fill: '#94a3b8', fontSize: 12 }} />
                    <YAxis stroke="#475569" tick={{ fill: '#94a3b8', fontSize: 12 }} />
                    <Tooltip
                      contentStyle={{ background: '#1e293b', border: '1px solid rgba(255,255,255,0.1)', borderRadius: 8, color: '#f1f5f9' }}
                      cursor={{ fill: 'rgba(255,255,255,0.04)' }}
                    />
                    <Bar dataKey="count" fill="#3b82f6" radius={[4,4,0,0]} />
                  </BarChart>
                </ResponsiveContainer>
              )}
            </div>
          </div>

          {/* Route distances */}
          <div className="card">
            <div className="card-header">
              <span className="card-title">Route Distances (km)</span>
            </div>
            <div className="card-body">
              {routeDistData.length === 0 ? (
                <div className="empty-state" style={{ padding: '20px' }}>
                  <p>Run optimization to see route data</p>
                </div>
              ) : (
                <ResponsiveContainer width="100%" height={200}>
                  <LineChart data={routeDistData}>
                    <XAxis dataKey="route" stroke="#475569" tick={{ fill: '#94a3b8', fontSize: 12 }} />
                    <YAxis stroke="#475569" tick={{ fill: '#94a3b8', fontSize: 12 }} />
                    <Tooltip
                      contentStyle={{ background: '#1e293b', border: '1px solid rgba(255,255,255,0.1)', borderRadius: 8, color: '#f1f5f9' }}
                    />
                    <Line type="monotone" dataKey="distance" stroke="#10b981" strokeWidth={2} dot={{ fill: '#10b981' }} />
                  </LineChart>
                </ResponsiveContainer>
              )}
            </div>
          </div>
        </div>

        {/* Recent Deliveries Table */}
        <div className="card">
          <div className="card-header">
            <span className="card-title">Recent Deliveries</span>
            <span className="badge badge-pending">{stats?.pendingDeliveries ?? 0} pending</span>
          </div>
          <div style={{ overflowX: 'auto' }}>
            {deliveries.length === 0 ? (
              <div className="empty-state">
                <div className="empty-state-icon">📦</div>
                <div className="empty-state-title">No deliveries found</div>
                <p style={{ fontSize: '0.85rem' }}>Backend may still be starting up</p>
              </div>
            ) : (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Destination</th>
                    <th>Weight</th>
                    <th>Priority</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {deliveries.slice(0, 8).map(d => (
                    <tr key={d.id}>
                      <td style={{ fontFamily: 'JetBrains Mono, monospace', fontSize: '0.8rem', color: 'var(--accent-cyan)' }}>
                        {d.deliveryId}
                      </td>
                      <td style={{ color: 'var(--text-primary)', fontWeight: 500 }}>
                        {d.deliveryLocation?.name ?? '—'}
                      </td>
                      <td>{d.weight} kg</td>
                      <td>
                        <span className={`badge priority-${d.priority}`}>P{d.priority}</span>
                      </td>
                      <td>
                        <span className={`badge badge-${d.status.toLowerCase().replace('_', '-')}`}>
                          {d.status}
                        </span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
