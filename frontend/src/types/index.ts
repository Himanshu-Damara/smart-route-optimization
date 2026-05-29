// Types for Smart Route Optimization System

export interface Location {
  id?: number;
  locationId: string;
  name: string;
  latitude: number;
  longitude: number;
  address?: string;
  type: 'WAREHOUSE' | 'CUSTOMER' | 'WAYPOINT';
}

export interface TimeWindow {
  startTime: string; // "HH:mm:ss"
  endTime: string;
}

export interface Delivery {
  id?: number;
  deliveryId: string;
  pickupLocation: Location;
  deliveryLocation: Location;
  weight: number;
  volume: number;
  priority: number; // 1-5
  timeWindow?: TimeWindow;
  status: 'PENDING' | 'ASSIGNED' | 'IN_TRANSIT' | 'DELIVERED';
  customerPhone?: string;
  notes?: string;
  createdAt?: string;
  assignedAt?: string;
  deliveredAt?: string;
}

export interface Vehicle {
  id?: number;
  vehicleId: string;
  name: string;
  currentLocation?: Location;
  maxWeightCapacity: number;
  maxVolumeCapacity: number;
  currentWeight: number;
  currentVolume: number;
  fuelCostPerKm: number;
  status: 'IDLE' | 'IN_TRANSIT' | 'FULL';
  driverName?: string;
  driverPhone?: string;
  plateNumber?: string;
  availableFrom?: string;
  availableUntil?: string;
  totalDistance: number;
  totalCost: number;
}

export interface Route {
  id?: number;
  routeId: string;
  vehicle: Vehicle;
  deliveries: Delivery[];
  waypoints: Location[];
  totalDistance: number;
  totalTime: number;
  totalCost: number;
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED';
  startTime?: string;
  estimatedEndTime?: string;
  actualEndTime?: string;
}

export interface OptimizationResponse {
  success: boolean;
  message: string;
  routes?: Route[];
  totalDistance: number;
  totalCost: number;
  capacityUtilization: number;
  executionTimeMs: number;
}

export interface RouteMetrics {
  totalRoutes: number;
  totalDistance: number;
  totalCost: number;
  totalDeliveries: number;
  averageDistancePerRoute: number;
  averageCostPerRoute: number;
}
