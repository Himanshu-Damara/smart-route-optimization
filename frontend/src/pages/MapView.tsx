import React, { useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { locationApi, routeApi } from '../services/api';
import type { Location, Route } from '../types';

// Fix Leaflet default icon paths (Vite build issue)
delete (L.Icon.Default.prototype as any)._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
});

// Custom marker icons
const warehouseIcon = L.divIcon({
  className: '',
  html: `<div style="
    width:38px;height:38px;background:linear-gradient(135deg,#3b82f6,#06b6d4);
    border-radius:50% 50% 50% 0;transform:rotate(-45deg);
    display:flex;align-items:center;justify-content:center;
    box-shadow:0 4px 15px rgba(59,130,246,0.6);border:2px solid rgba(255,255,255,0.3);">
    <span style="transform:rotate(45deg);font-size:16px;">🏭</span>
  </div>`,
  iconSize: [38, 38],
  iconAnchor: [19, 38],
  popupAnchor: [0, -40],
});

const customerIcon = (color: string) => L.divIcon({
  className: '',
  html: `<div style="
    width:30px;height:30px;background:${color};
    border-radius:50% 50% 50% 0;transform:rotate(-45deg);
    display:flex;align-items:center;justify-content:center;
    box-shadow:0 3px 10px rgba(0,0,0,0.4);border:2px solid rgba(255,255,255,0.2);">
    <span style="transform:rotate(45deg);font-size:13px;">📦</span>
  </div>`,
  iconSize: [30, 30],
  iconAnchor: [15, 30],
  popupAnchor: [0, -32],
});

const ROUTE_COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#8b5cf6', '#f43f5e', '#06b6d4', '#f97316'];

// Component to fit map bounds
function MapBoundsController({ locations }: { locations: Location[] }) {
  const map = useMap();
  useEffect(() => {
    if (locations.length > 0) {
      const bounds = L.latLngBounds(
        locations.map(loc => [loc.latitude, loc.longitude] as [number, number])
      );
      map.fitBounds(bounds, { padding: [40, 40] });
    }
  }, [locations, map]);
  return null;
}

export default function MapView() {
  const [locations, setLocations] = useState<Location[]>([]);
  const [routes, setRoutes] = useState<Route[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedRoute, setSelectedRoute] = useState<string | null>(null);

  useEffect(() => {
    const load = async () => {
      try {
        const [locs, rts] = await Promise.all([
          locationApi.getAll().catch(() => []),
          routeApi.getAll().catch(() => []),
        ]);
        setLocations(locs);
        setRoutes(rts);
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const warehouse = locations.find(l => l.type === 'WAREHOUSE');
  const customers = locations.filter(l => l.type === 'CUSTOMER');

  // Default to Delhi if no data
  const center: [number, number] = warehouse
    ? [warehouse.latitude, warehouse.longitude]
    : [28.6139, 77.2090];

  // Build route polylines
  const routePolylines = routes.map((route, idx) => {
    const color = ROUTE_COLORS[idx % ROUTE_COLORS.length];
    const points: [number, number][] = [];

    if (warehouse) points.push([warehouse.latitude, warehouse.longitude]);

    route.deliveries?.forEach(d => {
      if (d.deliveryLocation) {
        points.push([d.deliveryLocation.latitude, d.deliveryLocation.longitude]);
      }
    });

    if (warehouse) points.push([warehouse.latitude, warehouse.longitude]);

    return { route, color, points };
  });

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Route Map</h1>
          <p className="page-subtitle">
            Live visualization of delivery routes across Delhi-NCR
          </p>
        </div>
        <div style={{ display: 'flex', gap: 12, alignItems: 'center' }}>
          {loading && <div className="spinner"></div>}
          <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
            {routePolylines.map(({ route, color }, i) => (
              <button
                key={route.routeId}
                className="btn btn-sm"
                style={{
                  background: selectedRoute === route.routeId
                    ? color + '33'
                    : 'rgba(255,255,255,0.05)',
                  border: `1px solid ${color}55`,
                  color: color,
                }}
                onClick={() => setSelectedRoute(
                  selectedRoute === route.routeId ? null : route.routeId
                )}
              >
                {route.vehicle?.vehicleId ?? route.routeId}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="page-body" style={{ paddingTop: 16 }}>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 320px', gap: 20 }}>
          {/* Map */}
          <div className="map-container">
            <MapContainer
              center={center}
              zoom={11}
              style={{ width: '100%', height: '100%' }}
            >
              <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              />

              {locations.length > 0 && <MapBoundsController locations={locations} />}

              {/* Warehouse marker */}
              {warehouse && (
                <Marker position={[warehouse.latitude, warehouse.longitude]} icon={warehouseIcon}>
                  <Popup>
                    <div>
                      <strong style={{ color: '#3b82f6' }}>🏭 {warehouse.name}</strong>
                      <br />
                      <small style={{ color: '#94a3b8' }}>{warehouse.address}</small>
                      <br />
                      <small style={{ color: '#64748b' }}>
                        {warehouse.latitude.toFixed(4)}, {warehouse.longitude.toFixed(4)}
                      </small>
                    </div>
                  </Popup>
                </Marker>
              )}

              {/* Customer markers */}
              {customers.map((loc, idx) => {
                const routeForLoc = routePolylines.find(rp =>
                  rp.route.deliveries?.some(d => d.deliveryLocation?.locationId === loc.locationId)
                );
                const color = routeForLoc ? routeForLoc.color : '#94a3b8';

                return (
                  <Marker
                    key={loc.locationId}
                    position={[loc.latitude, loc.longitude]}
                    icon={customerIcon(color)}
                  >
                    <Popup>
                      <div>
                        <strong style={{ color }}>{loc.name}</strong>
                        <br />
                        <small style={{ color: '#94a3b8' }}>{loc.address}</small>
                        <br />
                        <small style={{ color: '#64748b' }}>
                          {loc.latitude.toFixed(4)}, {loc.longitude.toFixed(4)}
                        </small>
                      </div>
                    </Popup>
                  </Marker>
                );
              })}

              {/* Route polylines */}
              {routePolylines.map(({ route, color, points }) => {
                const isSelected = selectedRoute === null || selectedRoute === route.routeId;
                return points.length >= 2 ? (
                  <Polyline
                    key={route.routeId}
                    positions={points}
                    pathOptions={{
                      color,
                      weight: isSelected ? 4 : 2,
                      opacity: isSelected ? 0.9 : 0.3,
                      dashArray: route.status === 'PLANNED' ? '8,4' : undefined,
                    }}
                  />
                ) : null;
              })}
            </MapContainer>
          </div>

          {/* Sidebar info */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
            {/* Legend */}
            <div className="card">
              <div className="card-header">
                <span className="card-title">Route Legend</span>
              </div>
              <div className="card-body" style={{ padding: '16px' }}>
                {routePolylines.length === 0 ? (
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem', textAlign: 'center', padding: 8 }}>
                    No routes yet. Run optimization!
                  </div>
                ) : (
                  routePolylines.map(({ route, color }) => (
                    <div
                      key={route.routeId}
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: 10,
                        padding: '8px 0',
                        borderBottom: '1px solid rgba(255,255,255,0.05)',
                        cursor: 'pointer',
                      }}
                      onClick={() => setSelectedRoute(
                        selectedRoute === route.routeId ? null : route.routeId
                      )}
                    >
                      <div style={{
                        width: 24,
                        height: 4,
                        background: color,
                        borderRadius: 2,
                        flexShrink: 0,
                      }} />
                      <div style={{ flex: 1 }}>
                        <div style={{ fontSize: '0.82rem', fontWeight: 600, color: color }}>
                          {route.vehicle?.name ?? route.routeId}
                        </div>
                        <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)' }}>
                          {route.deliveries?.length ?? 0} stops · {route.totalDistance?.toFixed(1)} km
                        </div>
                      </div>
                      <span className={`badge badge-${route.status?.toLowerCase()}`} style={{ fontSize: '0.65rem' }}>
                        {route.status}
                      </span>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* Location list */}
            <div className="card" style={{ flex: 1, overflow: 'hidden' }}>
              <div className="card-header">
                <span className="card-title">Locations ({locations.length})</span>
              </div>
              <div style={{ overflowY: 'auto', maxHeight: 320 }}>
                {locations.map(loc => (
                  <div
                    key={loc.locationId}
                    style={{
                      padding: '10px 16px',
                      borderBottom: '1px solid rgba(255,255,255,0.04)',
                      display: 'flex',
                      alignItems: 'center',
                      gap: 10,
                    }}
                  >
                    <span style={{ fontSize: '1rem' }}>
                      {loc.type === 'WAREHOUSE' ? '🏭' : '📍'}
                    </span>
                    <div>
                      <div style={{ fontSize: '0.82rem', fontWeight: 500, color: 'var(--text-primary)' }}>
                        {loc.name}
                      </div>
                      <div style={{ fontSize: '0.7rem', color: 'var(--text-muted)' }}>
                        {loc.latitude.toFixed(4)}, {loc.longitude.toFixed(4)}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
