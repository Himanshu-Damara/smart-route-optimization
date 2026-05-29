import React, { useEffect, useState, useCallback } from 'react';
import { deliveryApi, locationApi } from '../services/api';
import type { Delivery, Location } from '../types';

const PRIORITY_LABELS: Record<number, string> = {
  1: 'Very Low', 2: 'Low', 3: 'Normal', 4: 'High', 5: 'Critical'
};

function AddDeliveryModal({
  locations,
  onClose,
  onSave,
}: {
  locations: Location[];
  onClose: () => void;
  onSave: (d: Partial<Delivery>) => Promise<void>;
}) {
  const warehouse = locations.find(l => l.type === 'WAREHOUSE');
  const customers = locations.filter(l => l.type === 'CUSTOMER');

  const [form, setForm] = useState({
    deliveryLocationId: '',
    weight: '',
    volume: '',
    priority: '3',
    customerPhone: '',
    notes: '',
    timeStart: '09:00',
    timeEnd: '21:00',
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const handleSave = async () => {
    if (!form.deliveryLocationId || !form.weight || !form.volume) {
      setError('Please fill in all required fields.');
      return;
    }
    setSaving(true);
    setError('');
    try {
      const destLoc = locations.find(l => l.locationId === form.deliveryLocationId);
      await onSave({
        pickupLocation: warehouse,
        deliveryLocation: destLoc,
        weight: parseFloat(form.weight),
        volume: parseFloat(form.volume),
        priority: parseInt(form.priority),
        customerPhone: form.customerPhone,
        notes: form.notes,
        timeWindow: {
          startTime: form.timeStart + ':00',
          endTime: form.timeEnd + ':00',
        },
      });
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Failed to create delivery.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <span className="modal-title">➕ New Delivery Order</span>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>
        <div className="modal-body">
          {error && <div className="alert alert-error" style={{ marginBottom: 16 }}>{error}</div>}

          <div className="form-group">
            <label className="form-label">Destination *</label>
            <select
              className="form-select"
              value={form.deliveryLocationId}
              onChange={e => setForm({ ...form, deliveryLocationId: e.target.value })}
            >
              <option value="">Select customer location...</option>
              {customers.map(loc => (
                <option key={loc.locationId} value={loc.locationId}>{loc.name}</option>
              ))}
            </select>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Weight (kg) *</label>
              <input
                type="number"
                className="form-input"
                placeholder="e.g. 25"
                value={form.weight}
                min="0.1" max="500"
                onChange={e => setForm({ ...form, weight: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Volume (m³) *</label>
              <input
                type="number"
                className="form-input"
                placeholder="e.g. 1.5"
                value={form.volume}
                min="0.01" max="50"
                onChange={e => setForm({ ...form, volume: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Priority (1=Low, 5=Critical)</label>
            <div style={{ display: 'flex', gap: 8 }}>
              {[1,2,3,4,5].map(p => (
                <button
                  key={p}
                  className="btn btn-sm"
                  style={{
                    flex: 1,
                    background: form.priority === String(p)
                      ? (p >= 4 ? 'rgba(244,63,94,0.2)' : p === 3 ? 'rgba(245,158,11,0.2)' : 'rgba(59,130,246,0.2)')
                      : 'rgba(255,255,255,0.04)',
                    border: `1px solid ${form.priority === String(p) ? (p >= 4 ? '#f43f5e' : p === 3 ? '#f59e0b' : '#3b82f6') : 'rgba(255,255,255,0.1)'}`,
                    color: form.priority === String(p) ? 'var(--text-primary)' : 'var(--text-muted)',
                  }}
                  onClick={() => setForm({ ...form, priority: String(p) })}
                >
                  P{p}
                </button>
              ))}
            </div>
            <p style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: 6 }}>
              {PRIORITY_LABELS[parseInt(form.priority)]}
            </p>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Time Window Start</label>
              <input
                type="time"
                className="form-input"
                value={form.timeStart}
                onChange={e => setForm({ ...form, timeStart: e.target.value })}
              />
            </div>
            <div className="form-group">
              <label className="form-label">Time Window End</label>
              <input
                type="time"
                className="form-input"
                value={form.timeEnd}
                onChange={e => setForm({ ...form, timeEnd: e.target.value })}
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label">Customer Phone</label>
            <input
              type="tel"
              className="form-input"
              placeholder="+91-XXXXXXXXXX"
              value={form.customerPhone}
              onChange={e => setForm({ ...form, customerPhone: e.target.value })}
            />
          </div>

          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label">Notes</label>
            <textarea
              className="form-textarea"
              placeholder="Special instructions..."
              value={form.notes}
              onChange={e => setForm({ ...form, notes: e.target.value })}
            />
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-outline" onClick={onClose}>Cancel</button>
          <button className="btn btn-primary" onClick={handleSave} disabled={saving}>
            {saving ? <><span className="spinner"></span> Saving...</> : '➕ Create Delivery'}
          </button>
        </div>
      </div>
    </div>
  );
}

export default function Deliveries() {
  const [deliveries, setDeliveries] = useState<Delivery[]>([]);
  const [locations, setLocations] = useState<Location[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [filter, setFilter] = useState<string>('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [dels, locs] = await Promise.all([
        deliveryApi.getAll().catch(() => []),
        locationApi.getAll().catch(() => []),
      ]);
      setDeliveries(dels);
      setLocations(locs);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => { fetchData(); }, [fetchData]);

  const handleCreate = async (data: Partial<Delivery>) => {
    await deliveryApi.create(data);
    setShowModal(false);
    fetchData();
  };

  const filtered = deliveries.filter(d => {
    const matchesFilter = filter === 'ALL' || d.status === filter;
    const matchesSearch = !searchTerm ||
      d.deliveryId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.deliveryLocation?.name?.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  const statusCounts = {
    ALL: deliveries.length,
    PENDING: deliveries.filter(d => d.status === 'PENDING').length,
    ASSIGNED: deliveries.filter(d => d.status === 'ASSIGNED').length,
    IN_TRANSIT: deliveries.filter(d => d.status === 'IN_TRANSIT').length,
    DELIVERED: deliveries.filter(d => d.status === 'DELIVERED').length,
  };

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Deliveries</h1>
          <p className="page-subtitle">{deliveries.length} total orders · {statusCounts.PENDING} pending</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          ➕ Add Delivery
        </button>
      </div>

      <div className="page-body">
        {/* Filters */}
        <div style={{ display: 'flex', gap: 12, marginBottom: 20, alignItems: 'center', flexWrap: 'wrap' }}>
          <div style={{ display: 'flex', gap: 8 }}>
            {Object.entries(statusCounts).map(([status, count]) => (
              <button
                key={status}
                className={`btn btn-sm ${filter === status ? 'btn-primary' : 'btn-outline'}`}
                onClick={() => setFilter(status)}
              >
                {status === 'ALL' ? 'All' : status.replace('_', ' ')}
                <span style={{
                  background: 'rgba(255,255,255,0.15)',
                  borderRadius: 8,
                  padding: '1px 6px',
                  fontSize: '0.7rem',
                  marginLeft: 4,
                }}>
                  {count}
                </span>
              </button>
            ))}
          </div>
          <input
            className="form-input"
            style={{ maxWidth: 220 }}
            placeholder="🔍 Search deliveries..."
            value={searchTerm}
            onChange={e => setSearchTerm(e.target.value)}
          />
        </div>

        {/* Table */}
        <div className="card">
          <div style={{ overflowX: 'auto' }}>
            {loading ? (
              <div className="empty-state">
                <div className="spinner" style={{ width: 32, height: 32, margin: '0 auto 12px' }}></div>
                <p>Loading deliveries...</p>
              </div>
            ) : filtered.length === 0 ? (
              <div className="empty-state">
                <div className="empty-state-icon">📦</div>
                <div className="empty-state-title">No deliveries found</div>
                <p style={{ fontSize: '0.85rem' }}>
                  {deliveries.length === 0 ? 'Backend is loading or no data yet.' : 'Try adjusting filters.'}
                </p>
              </div>
            ) : (
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Order ID</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Weight</th>
                    <th>Priority</th>
                    <th>Time Window</th>
                    <th>Status</th>
                    <th>Notes</th>
                  </tr>
                </thead>
                <tbody>
                  {filtered.map(d => (
                    <tr key={d.id}>
                      <td style={{ fontFamily: 'JetBrains Mono, monospace', fontSize: '0.8rem', color: 'var(--accent-cyan)' }}>
                        {d.deliveryId}
                      </td>
                      <td style={{ fontSize: '0.82rem' }}>{d.pickupLocation?.name ?? '—'}</td>
                      <td style={{ color: 'var(--text-primary)', fontWeight: 500, fontSize: '0.85rem' }}>
                        {d.deliveryLocation?.name ?? '—'}
                      </td>
                      <td>{d.weight} kg / {d.volume}m³</td>
                      <td>
                        <span className={`badge priority-${d.priority}`}>
                          P{d.priority} — {PRIORITY_LABELS[d.priority]}
                        </span>
                      </td>
                      <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                        {d.timeWindow
                          ? `${d.timeWindow.startTime?.slice(0,5)} – ${d.timeWindow.endTime?.slice(0,5)}`
                          : '—'}
                      </td>
                      <td>
                        <span className={`badge badge-${d.status.toLowerCase().replace('_', '-')}`}>
                          {d.status.replace('_', ' ')}
                        </span>
                      </td>
                      <td style={{ fontSize: '0.78rem', color: 'var(--text-muted)', maxWidth: 140, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                        {d.notes || '—'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {showModal && (
        <AddDeliveryModal
          locations={locations}
          onClose={() => setShowModal(false)}
          onSave={handleCreate}
        />
      )}
    </div>
  );
}
