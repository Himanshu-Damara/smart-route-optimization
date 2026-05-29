# 🗺️ Smart Route Optimization System

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-18-blue?style=for-the-badge&logo=react&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-5-blue?style=for-the-badge&logo=typescript&logoColor=white)
![Leaflet](https://img.shields.io/badge/Leaflet.js-Map-brightgreen?style=for-the-badge&logo=leaflet&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-lightblue?style=for-the-badge)

**An AI-powered Vehicle Routing Problem (VRP) solver for optimizing multi-vehicle delivery routes using classical DSA algorithms.**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Architecture](#-architecture) • [Setup](#-getting-started) • [API Docs](#-api-endpoints) • [Algorithms](#-algorithms)

</div>

---

## 📸 Overview

RouteOptima solves the **Vehicle Routing Problem (VRP)** — a classic NP-hard optimization problem used by Amazon, Flipkart, Zomato, and logistics companies worldwide. Given a set of delivery orders, multiple vehicles with capacity constraints, and customer time windows, it computes the most efficient routes for every vehicle.

### Real-World Use Cases
- 🛒 E-commerce last-mile delivery (Amazon / Flipkart)
- 🍔 Food delivery optimization (Zomato / Swiggy)
- 🚚 Logistics & freight routing
- 🚗 Cab aggregator routing

---

## ✨ Features

### Core (Implemented)
| Feature | Description | DSA Used |
|---------|-------------|----------|
| **Route Optimization** | Find optimal delivery sequence per vehicle | Dijkstra + 2-Opt |
| **Vehicle Assignment** | Assign orders to vehicles respecting capacity | Greedy + Priority Queue |
| **Nearest Neighbor** | Build initial route from warehouse | Heuristic O(n²) |
| **2-Opt Improvement** | Swap route segments to reduce distance | Local Search |
| **Time Windows** | Respect delivery time constraints | BST + Interval Scheduling |
| **Interactive Map** | Live route visualization on map | Leaflet.js + OSM |
| **Real-time Dashboard** | Metrics, charts, fleet status | Recharts |
| **Add Deliveries** | Dynamically add new orders | REST API |
| **Distance Matrix** | Compute all pairwise distances | Haversine Formula |

### Data Structures Implemented from Scratch
- `Graph.java` — Weighted directed graph (adjacency list)
- `Heap.java` — Min-heap for priority queue operations
- `LinkedList.java` — Custom linked list for route sequences
- `BinarySearchTree.java` — For time window interval queries

---

## 🛠 Tech Stack

### Backend
```
Java 17          Spring Boot 3.2     Spring Data JPA
H2 Database      Maven               Lombok
Jackson          SLF4J               Jakarta Validation
```

### Frontend
```
React 18         TypeScript          Vite 6
Leaflet.js       React-Leaflet       Recharts
Axios            Lucide Icons        OpenStreetMap (free tiles)
```

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────────────┐
│              FRONTEND  (React + Vite — :5173)               │
│  Dashboard │ MapView │ Deliveries │ Vehicles │ Optimizer     │
└─────────────────────┬───────────────────────────────────────┘
                      │  /api/* → proxy
                      ↓
┌─────────────────────────────────────────────────────────────┐
│            BACKEND  (Spring Boot — :8080/api)               │
│                                                              │
│  Controllers: Route │ Delivery │ Vehicle │ Location │ Health │
│  Services:    RouteOptimizationService │ DeliveryService     │
│  Algorithms:  Dijkstra │ Greedy │ NearestNeighbor │ 2-Opt   │
│  DSA:         Graph │ Heap │ LinkedList │ BST                │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ↓
┌─────────────────────────────────────────────────────────────┐
│           DATA LAYER  (H2 In-Memory / PostgreSQL)           │
│    locations │ vehicles │ deliveries │ routes               │
│    Auto-seeded on startup with Delhi-NCR sample data        │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure
```
src/main/java/com/sro/
├── algorithm/
│   ├── DijkstraAlgorithm.java          # O((V+E) log V) shortest path
│   ├── GreedyVehicleAssignment.java    # Priority-based delivery assignment
│   ├── NearestNeighborHeuristic.java   # TSP approximation
│   ├── TwoOptOptimization.java         # Route improvement
│   └── VehicleRoutingProblem.java      # Main VRP orchestrator
├── config/
│   ├── DataInitializer.java            # Seeds sample Delhi-NCR data
│   └── WebConfig.java                  # CORS configuration
├── controller/
│   ├── RouteController.java
│   ├── DeliveryController.java
│   ├── VehicleController.java
│   └── LocationController.java
├── datastructure/
│   ├── Graph.java                      # Weighted directed graph
│   ├── Heap.java                       # Min-heap implementation
│   ├── LinkedList.java                 # Custom linked list
│   └── BinarySearchTree.java          # For time window queries
├── exception/
│   └── GlobalExceptionHandler.java    # Centralized error handling
├── model/
│   ├── Delivery.java │ Vehicle.java │ Route.java
│   ├── Location.java │ TimeWindow.java
│   └── (Status enums)
├── service/
│   ├── RouteOptimizationService.java  # Core business logic
│   ├── DeliveryService.java
│   ├── VehicleService.java
│   └── LocationService.java
└── util/
    ├── DistanceUtils.java             # Haversine distance formula
    ├── TimeUtils.java                 # Time window helpers
    └── Constants.java                 # App-wide constants

frontend/src/
├── pages/
│   ├── Dashboard.tsx    # Stat cards + bar/line charts
│   ├── MapView.tsx      # Leaflet map with route polylines
│   ├── Deliveries.tsx   # CRUD + add delivery modal
│   ├── Vehicles.tsx     # Fleet cards with capacity bars
│   └── Routes.tsx       # VRP optimizer + route detail cards
├── services/api.ts      # Axios API client
├── types/index.ts       # TypeScript interfaces
├── App.tsx              # Sidebar navigation
└── index.css            # Dark glassmorphism design system
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+** (OpenJDK / Temurin)
- **Maven 3.9+**
- **Node.js 18+** & **npm**

### 1. Clone the Repository
```bash
git clone https://github.com/Himanshu-Damara/smart-route-optimization.git
cd smart-route-optimization
```

### 2. Start the Backend
```bash
# Using Maven wrapper (recommended)
mvn spring-boot:run

# Or with your local Maven
mvn clean spring-boot:run
```

The backend starts at **`http://localhost:8080/api`**

> 📌 On first startup, **sample data is auto-seeded** — 11 Delhi-NCR locations, 3 vehicles, and 10 delivery orders.

### 3. Start the Frontend
```bash
cd frontend
npm install
npm run dev
```

The frontend starts at **`http://localhost:5173`**

> The Vite dev server automatically proxies `/api/*` → `http://localhost:8080/api`

### 4. Optional — H2 Database Console
```
URL:      http://localhost:8080/api/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave blank)
```

---

## 🗺️ Sample Data (Auto-Seeded)

### Locations (Real Delhi-NCR Coordinates)
| ID | Location | Type | Lat, Lng |
|----|----------|------|----------|
| W001 | Central Warehouse — Connaught Place | WAREHOUSE | 28.6139, 77.2090 |
| L001 | DLF Cyber City — Gurugram | CUSTOMER | 28.4966, 77.0891 |
| L002 | Sector 62 — Noida | CUSTOMER | 28.6276, 77.3694 |
| L003 | MG Road — Gurugram | CUSTOMER | 28.4702, 77.0263 |
| L004 | NIT — Faridabad | CUSTOMER | 28.3833, 77.3118 |
| L005 | Indirapuram — Ghaziabad | CUSTOMER | 28.6411, 77.3667 |
| L006 | Saket — South Delhi | CUSTOMER | 28.5245, 77.2066 |
| L007 | Rohini — North Delhi | CUSTOMER | 28.7041, 77.1025 |
| L008 | Dwarka — West Delhi | CUSTOMER | 28.5930, 77.0470 |
| L009 | Lajpat Nagar — Delhi | CUSTOMER | 28.5700, 77.2400 |
| L010 | Vasant Kunj — South Delhi | CUSTOMER | 28.5200, 77.1600 |

### Vehicles
| ID | Name | Capacity | Fuel Cost |
|----|------|---------|-----------|
| V001 | Delhi Express — Truck | 200 kg | ₹12/km |
| V002 | NCR Swift — Van | 100 kg | ₹8/km |
| V003 | Last Mile — Bike | 30 kg | ₹4/km |

---

## 📡 API Endpoints

### Locations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/locations` | Get all locations |
| POST | `/api/locations` | Create location |
| GET | `/api/locations/{id}` | Get by ID |

### Deliveries
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/deliveries` | Get all deliveries |
| POST | `/api/deliveries` | Create new delivery |
| GET | `/api/deliveries/status/pending` | Get pending deliveries |
| PUT | `/api/deliveries/{id}/mark-delivered` | Mark as delivered |
| DELETE | `/api/deliveries/{id}` | Delete delivery |

### Vehicles
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vehicles` | Get all vehicles |
| POST | `/api/vehicles` | Create vehicle |
| GET | `/api/vehicles/status/available` | Get idle vehicles |
| GET | `/api/vehicles/{id}/utilization` | Get capacity utilization |

### Routes & Optimization
| Method | Endpoint | Description |
|--------|----------|-------------|
| **POST** | **`/api/routes/optimize?warehouseLocationId=W001`** | **🔥 Run VRP Optimization** |
| GET | `/api/routes` | Get all routes |
| GET | `/api/routes/status/active` | Get active routes |
| GET | `/api/routes/metrics/all` | Summary metrics |
| PUT | `/api/routes/{id}/start` | Start a route |
| PUT | `/api/routes/{id}/complete` | Complete a route |

### Health
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Backend health check |

---

## ⚡ Algorithms

### Optimization Pipeline

```
INPUT: List of Deliveries + List of Vehicles + Warehouse Location
         │
         ▼
┌─────────────────────────────┐
│  STEP 1: Greedy Assignment  │  Sort by priority (P5→P1)
│  O(n × m)                  │  Assign to best available vehicle
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│  STEP 2: Nearest Neighbor   │  Start at warehouse
│  O(n²) per vehicle          │  Always visit nearest unvisited
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│  STEP 3: 2-Opt Swap         │  Try all (i,j) edge swaps
│  O(n²) per iteration        │  Keep if distance reduces
└──────────────┬──────────────┘
               │
               ▼
OUTPUT: Optimized routes with total distance, cost & time
```

### Complexity Analysis
| Algorithm | Time | Space | Use |
|-----------|------|-------|-----|
| Dijkstra | O((V+E) log V) | O(V) | Shortest path between locations |
| Greedy Assignment | O(n × m) | O(n+m) | Vehicle-to-delivery assignment |
| Nearest Neighbor | O(n²) | O(n) | Initial route construction |
| 2-Opt | O(n² × iter) | O(n) | Route improvement |

### Distance Calculation
Uses the **Haversine Formula** for accurate great-circle distances between GPS coordinates:

```
a = sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlon/2)
distance = 2 × R × arctan2(√a, √(1−a))
```
where R = 6371 km (Earth's radius)

---

## 🎨 UI Features

| Page | Features |
|------|---------|
| **Dashboard** | Animated stat cards, priority bar chart, route distance line chart, recent deliveries table |
| **Route Map** | Leaflet + OpenStreetMap, custom markers, colored polylines per vehicle, route legend, location list |
| **Deliveries** | Status filter tabs, search bar, priority badges (P1–P5), add delivery modal with time window picker |
| **Vehicles** | Fleet overview, capacity progress bars, driver info, status glow indicators, utilization % |
| **Optimize** | One-click VRP trigger, execution time display, expandable route cards with stop-by-stop timeline |

---

## 📊 Performance Targets

| Metric | Target | Algorithm |
|--------|--------|-----------|
| Optimization for 50 deliveries | < 5 seconds | 2-Opt bounded iterations |
| Distance reduction vs naive | ~25–35% | 2-Opt improvement |
| Vehicle capacity utilization | > 80% | Greedy assignment |
| Time window compliance | 100% | Pre-validation |

---

## 🧪 Testing

```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=DijkstraTest

# Run with test report
mvn verify
```

Test coverage includes:
- `GraphTest.java` — vertex/edge operations, Dijkstra correctness
- `HeapTest.java` — min-heap invariant, priority ordering
- `LinkedListTest.java` — insert/delete/traverse
- `DijkstraTest.java` — shortest path, edge cases
- `NearestNeighborTest.java` — route building
- `VRPTest.java` — end-to-end optimization
- `RouteOptimizationServiceTest.java` — service layer
- `RouteControllerTest.java` — REST API endpoints

---

## 🐳 Docker (Optional)

```bash
# Build image
docker build -t smart-route-optimization .

# Run with docker-compose
docker-compose up
```

---

## 🗂 Project Phases

- [x] **Phase 1** — Data structures (Graph, Heap, LinkedList, BST)
- [x] **Phase 2** — Algorithms (Dijkstra, Greedy, Nearest Neighbor, 2-Opt, VRP)
- [x] **Phase 3** — Spring Boot REST API (Controllers, Services, Repositories)
- [x] **Phase 4** — React Frontend (Map, Dashboard, CRUD, Optimizer UI)
- [ ] **Phase 5** — Traffic prediction, multi-day planning, ML demand forecasting

---

## 📚 Learning Outcomes

After studying this project you will understand:

**DSA in practice:**
- Graph representation (adjacency list) for real-world maps
- Priority queues for scheduling and vehicle selection
- Heuristic algorithms (Greedy, Nearest Neighbor) vs exact solvers
- Local search optimization (2-Opt) for NP-hard problems

**System Design:**
- Three-tier architecture (Frontend → API → Database)
- REST API design with Spring Boot
- In-memory vs persistent database trade-offs
- CORS and cross-origin API consumption

**Full-Stack Development:**
- Spring Data JPA with entity relationships
- React component patterns and state management
- Map integration with Leaflet.js
- Proxy configuration for development

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/traffic-prediction`
3. Commit changes: `git commit -m "feat: add traffic prediction module"`
4. Push and open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.

---

<div align="center">

Made with ❤️ by [Himanshu Damara](https://github.com/Himanshu-Damara)

⭐ Star this repo if it helped you learn DSA + System Design!

</div>
