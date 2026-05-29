import axios from 'axios';
import type { Delivery, Vehicle, Route, OptimizationResponse, Location, RouteMetrics } from '../types';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

// Locations
export const locationApi = {
  getAll: () => api.get<Location[]>('/locations').then(r => r.data),
  getById: (id: string) => api.get<Location>(`/locations/${id}`).then(r => r.data),
  create: (loc: Partial<Location>) => api.post<Location>('/locations', loc).then(r => r.data),
};

// Deliveries
export const deliveryApi = {
  getAll: () => api.get<Delivery[]>('/deliveries').then(r => r.data),
  getPending: () => api.get<Delivery[]>('/deliveries/pending').then(r => r.data),
  getById: (id: string) => api.get<Delivery>(`/deliveries/${id}`).then(r => r.data),
  create: (d: Partial<Delivery>) => api.post<Delivery>('/deliveries', d).then(r => r.data),
  updateStatus: (id: string, status: string) =>
    api.put<Delivery>(`/deliveries/${id}/status?status=${status}`).then(r => r.data),
  delete: (id: number) => api.delete(`/deliveries/${id}`),
};

// Vehicles
export const vehicleApi = {
  getAll: () => api.get<Vehicle[]>('/vehicles').then(r => r.data),
  getById: (id: string) => api.get<Vehicle>(`/vehicles/${id}`).then(r => r.data),
  create: (v: Partial<Vehicle>) => api.post<Vehicle>('/vehicles', v).then(r => r.data),
  updateStatus: (id: string, status: string) =>
    api.put<Vehicle>(`/vehicles/${id}/status?status=${status}`).then(r => r.data),
};

// Routes
export const routeApi = {
  getAll: () => api.get<Route[]>('/routes').then(r => r.data),
  getActive: () => api.get<Route[]>('/routes/status/active').then(r => r.data),
  getById: (id: number) => api.get<Route>(`/routes/${id}`).then(r => r.data),
  optimize: (warehouseId: string) =>
    api.post<OptimizationResponse>(`/routes/optimize?warehouseLocationId=${warehouseId}`).then(r => r.data),
  startRoute: (id: number) => api.put<Route>(`/routes/${id}/start`).then(r => r.data),
  completeRoute: (id: number) => api.put<Route>(`/routes/${id}/complete`).then(r => r.data),
  getMetrics: () => api.get<RouteMetrics>('/routes/metrics/all').then(r => r.data),
};

// Health check
export const healthApi = {
  check: () => api.get('/health').then(r => r.data),
};

export default api;
