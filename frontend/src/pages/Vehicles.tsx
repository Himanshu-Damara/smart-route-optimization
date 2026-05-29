import React, { useEffect, useState, useCallback } from 'react';
import { vehicleApi } from '../services/api';
import type { Vehicle } from '../types';

function CapacityBar({ used, max, color }: { used: number; max: number; color: string }) {
  const pct = Math.min(100, max > 0 ? (used / max) * 100 : 0);
  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
        <span style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>{used.toFixed(1)} / {max} kg</span>
        <span style={{ fontSize: '0.72rem', color }}>{pct.toFixed(0)}%</span>
      </div>
      <div className="progress-bar">
        <div
          className="progress-fill"
          style={{
            width: `${pct}%`,
            background: pct > 85 ? 'linear-gradient(135deg,#f43f5e,#f97316)' :
              pct > 60 ? 'linear-gradient(135deg,#f59e0b,#f97316)' :
              'linear-gradient(135deg,#10b981,#06b6d4)',
          }}
        />
      </div>
    </div>
  );
}

export default function Vehicles() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchVehicles = useCallback(async () => {
    setLoading(true);
    try {
      const data = await vehicleApi.getAll().catch(() => []);
      setVehicles(data);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchVehicles(); }, [fetchVehicles]);

  const statusColorMap: Record<string, string> = {
    IDLE: '#94a3b8',
    IN_TRANSIT: '#10b981',
    FULL: '#f43f5e',
  };

  const total = vehicles.length;
  const idle = vehicles.filter(v => v.status === 'IDLE').length;
  const active = vehicles.filter(v => v.status === 'IN_TRANSIT').length;
  const full = vehicles.filter(v => v.status === 'FULL').length;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Fleet Vehicles</h1>
          <p className="page-subtitle">
            {total} vehicles · {idle} idle · {active} active · {full} full
          </p>
        </div>
        <button className="btn btn-outline btn-sm" onClick={fetchVehicles} disabled={loading}>
          🔄 Refresh
        </button>
      </div>

      <div className="page-body">
        {/* Summary strip */}
        <div style={{ display: 'flex', gap: 16, marginBottom: 28, flexWrap: 'wrap' }}>
          {[
            { label: 'Total Fleet', value: total, color: '#3b82f6' },
            { label: 'Idle', value: idle, color: '#10b981' },
            { label: 'In Transit', value: active, color: '#8b5cf6' },
            { label: 'Full', value: full, color: '#f43f5e' },
          ].map(item => (
            <div
              key={item.label}
              style={{
                background: 'var(--bg-card)',
                border: `1px solid ${item.color}22`,
                borderRadius: 12,
                padding: '14px 20px',
                minWidth: 130,
                display: 'flex',
                flexDirection: 'column',
                gap: 4,
              }}
            >
              <span style={{ fontSize: '1.5rem', fontWeight: 800, color: item.color }}>{item.value}</span>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                {item.label}
              </span>
            </div>
          ))}

          <div style={{
            background: 'var(--bg-card)',
            border: '1px solid rgba(245,158,11,0.2)',
            borderRadius: 12,
            padding: '14px 20px',
            display: 'flex',
            flexDirection: 'column',
            gap: 4,
          }}>
            <span style={{ fontSize: '1.5rem', fontWeight: 800, color: '#f59e0b' }}>
              ₹{vehicles.reduce((s, v) => s + (v.totalCost || 0), 0).toFixed(0)}
            </span>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
              Total Fuel Cost
            </span>
          </div>

          <div style={{
            background: 'var(--bg-card)',
            border: '1px solid rgba(6,182,212,0.2)',
            borderRadius: 12,
            padding: '14px 20px',
            display: 'flex',
            flexDirection: 'column',
            gap: 4,
          }}>
            <span style={{ fontSize: '1.5rem', fontWeight: 800, color: '#06b6d4' }}>
              {vehicles.reduce((s, v) => s + (v.totalDistance || 0), 0).toFixed(1)} km
            </span>
            <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.5px' }}>
              Total Distance
            </span>
          </div>
        </div>

        {/* Vehicle Cards Grid */}
        {loading ? (
          <div className="empty-state">
            <div className="spinner" style={{ width: 36, height: 36, margin: '0 auto 16px' }}></div>
            <p>Loading vehicles...</p>
          </div>
        ) : vehicles.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">🚛</div>
            <div className="empty-state-title">No vehicles found</div>
            <p style={{ fontSize: '0.85rem' }}>Backend may still be initializing.</p>
          </div>
        ) : (
          <div className="vehicle-grid">
            {vehicles.map(v => {
              const statusColor = statusColorMap[v.status] || '#94a3b8';
              const utilPct = v.maxWeightCapacity > 0
                ? Math.round((v.currentWeight / v.maxWeightCapacity) * 100)
                : 0;

              return (
                <div className="vehicle-card" key={v.vehicleId}>
                  {/* Status indicator */}
                  <div style={{
                    position: 'absolute',
                    top: 20,
                    right: 20,
                    width: 10,
                    height: 10,
                    borderRadius: '50%',
                    background: statusColor,
                    boxShadow: `0 0 8px ${statusColor}`,
                  }} />

                  <div className="vehicle-name">🚚 {v.name}</div>
                  <div className="vehicle-id">{v.vehicleId} · {v.plateNumber || 'No plate'}</div>

                  <div style={{ display: 'flex', gap: 8, marginTop: 12 }}>
                    <span className={`badge badge-${v.status.toLowerCase().replace('_', '-')}`}>
                      {v.status}
                    </span>
                    <span className="badge" style={{ background: 'rgba(255,255,255,0.05)', color: 'var(--text-muted)' }}>
                      ₹{v.fuelCostPerKm}/km
                    </span>
                  </div>

                  {/* Driver info */}
                  {v.driverName && (
                    <div className="vehicle-driver">
                      <span>👤</span>
                      <div>
                        <div style={{ fontWeight: 600, color: 'var(--text-primary)', fontSize: '0.82rem' }}>
                          {v.driverName}
                        </div>
                        <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
                          {v.driverPhone}
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Capacity bar */}
                  <div style={{ marginTop: 16 }}>
                    <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', marginBottom: 6, textTransform: 'uppercase', letterSpacing: '0.5px' }}>
                      Load Capacity
                    </div>
                    <CapacityBar used={v.currentWeight} max={v.maxWeightCapacity} color={statusColor} />
                  </div>

                  {/* Stats grid */}
                  <div className="vehicle-stats">
                    <div className="vehicle-stat-item">
                      <div className="vehicle-stat-label">Max Weight</div>
                      <div className="vehicle-stat-value">{v.maxWeightCapacity} kg</div>
                    </div>
                    <div className="vehicle-stat-item">
                      <div className="vehicle-stat-label">Utilization</div>
                      <div className="vehicle-stat-value" style={{
                        color: utilPct > 85 ? '#f43f5e' : utilPct > 60 ? '#f59e0b' : '#10b981'
                      }}>
                        {utilPct}%
                      </div>
                    </div>
                    <div className="vehicle-stat-item">
                      <div className="vehicle-stat-label">Distance</div>
                      <div className="vehicle-stat-value">{(v.totalDistance || 0).toFixed(1)} km</div>
                    </div>
                    <div className="vehicle-stat-item">
                      <div className="vehicle-stat-label">Cost</div>
                      <div className="vehicle-stat-value">₹{(v.totalCost || 0).toFixed(0)}</div>
                    </div>
                  </div>

                  {/* Availability */}
                  <div style={{ marginTop: 14, fontSize: '0.75rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: 6 }}>
                    🕐 Available: {v.availableFrom?.slice(0, 5) ?? '06:00'} – {v.availableUntil?.slice(0, 5) ?? '22:00'}
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}
