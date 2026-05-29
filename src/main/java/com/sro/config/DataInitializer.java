package com.sro.config;

import com.sro.model.*;
import com.sro.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Data Initializer - Seeds sample data for Smart Route Optimization System
 * Uses real Delhi-NCR coordinates for demonstration
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public void run(String... args) {
        if (locationRepository.count() > 0) {
            logger.info("Data already exists, skipping initialization.");
            return;
        }

        logger.info("Seeding sample data for Smart Route Optimization System...");
        seedLocations();
        seedVehicles();
        seedDeliveries();
        logger.info("Sample data seeded successfully!");
    }

    private void seedLocations() {
        // Warehouse (Origin)
        Location warehouse = new Location("W001", "Central Warehouse - Delhi", 28.6139, 77.2090);
        warehouse.setAddress("Connaught Place, New Delhi, Delhi 110001");
        warehouse.setType(LocationType.WAREHOUSE);
        locationRepository.save(warehouse);

        // Customer locations - real Delhi-NCR areas
        Location gurugram = new Location("L001", "DLF Cyber City - Gurugram", 28.4966, 77.0891);
        gurugram.setAddress("DLF Cyber City, Gurugram, Haryana 122002");
        gurugram.setType(LocationType.CUSTOMER);
        locationRepository.save(gurugram);

        Location noida = new Location("L002", "Sector 62 - Noida", 28.6276, 77.3694);
        noida.setAddress("Sector 62, Noida, Uttar Pradesh 201309");
        noida.setType(LocationType.CUSTOMER);
        locationRepository.save(noida);

        Location gurgaonSouth = new Location("L003", "MG Road - Gurugram", 28.4702, 77.0263);
        gurgaonSouth.setAddress("MG Road, Gurugram, Haryana 122001");
        gurgaonSouth.setType(LocationType.CUSTOMER);
        locationRepository.save(gurgaonSouth);

        Location faridabad = new Location("L004", "NIT - Faridabad", 28.3833, 77.3118);
        faridabad.setAddress("NIT Faridabad, Haryana 121001");
        faridabad.setType(LocationType.CUSTOMER);
        locationRepository.save(faridabad);

        Location ghaziabad = new Location("L005", "Indirapuram - Ghaziabad", 28.6411, 77.3667);
        ghaziabad.setAddress("Indirapuram, Ghaziabad, Uttar Pradesh 201014");
        ghaziabad.setType(LocationType.CUSTOMER);
        locationRepository.save(ghaziabad);

        Location southDelhi = new Location("L006", "Saket - South Delhi", 28.5245, 77.2066);
        southDelhi.setAddress("Saket, New Delhi, Delhi 110017");
        southDelhi.setType(LocationType.CUSTOMER);
        locationRepository.save(southDelhi);

        Location rohini = new Location("L007", "Rohini - North Delhi", 28.7041, 77.1025);
        rohini.setAddress("Rohini Sector 7, New Delhi, Delhi 110085");
        rohini.setType(LocationType.CUSTOMER);
        locationRepository.save(rohini);

        Location dwarka = new Location("L008", "Dwarka - West Delhi", 28.5930, 77.0470);
        dwarka.setAddress("Dwarka Sector 10, New Delhi, Delhi 110075");
        dwarka.setType(LocationType.CUSTOMER);
        locationRepository.save(dwarka);

        Location lajpatNagar = new Location("L009", "Lajpat Nagar - Delhi", 28.5700, 77.2400);
        lajpatNagar.setAddress("Lajpat Nagar, New Delhi, Delhi 110024");
        lajpatNagar.setType(LocationType.CUSTOMER);
        locationRepository.save(lajpatNagar);

        Location vasantKunj = new Location("L010", "Vasant Kunj - South Delhi", 28.5200, 77.1600);
        vasantKunj.setAddress("Vasant Kunj, New Delhi, Delhi 110070");
        vasantKunj.setType(LocationType.CUSTOMER);
        locationRepository.save(vasantKunj);

        logger.info("Seeded 11 locations (1 warehouse + 10 customer sites)");
    }

    private void seedVehicles() {
        Location warehouse = locationRepository.findByLocationId("W001").orElseThrow();

        Vehicle v1 = new Vehicle("V001", "Delhi Express - Truck", 200.0, 15.0, 12.0, warehouse);
        v1.setDriverName("Rajesh Kumar");
        v1.setDriverPhone("+91-9876543210");
        v1.setPlateNumber("DL-1CA-1234");
        v1.setAvailableFrom(LocalTime.of(8, 0));
        v1.setAvailableUntil(LocalTime.of(20, 0));
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle("V002", "NCR Swift - Van", 100.0, 8.0, 8.0, warehouse);
        v2.setDriverName("Amit Singh");
        v2.setDriverPhone("+91-9876543211");
        v2.setPlateNumber("HR-26-AZ-5678");
        v2.setAvailableFrom(LocalTime.of(9, 0));
        v2.setAvailableUntil(LocalTime.of(21, 0));
        vehicleRepository.save(v2);

        Vehicle v3 = new Vehicle("V003", "Last Mile - Bike", 30.0, 2.0, 4.0, warehouse);
        v3.setDriverName("Suresh Verma");
        v3.setDriverPhone("+91-9876543212");
        v3.setPlateNumber("UP-16-AB-9012");
        v3.setAvailableFrom(LocalTime.of(9, 0));
        v3.setAvailableUntil(LocalTime.of(22, 0));
        vehicleRepository.save(v3);

        logger.info("Seeded 3 vehicles");
    }

    private void seedDeliveries() {
        Location warehouse = locationRepository.findByLocationId("W001").orElseThrow();
        Location l001 = locationRepository.findByLocationId("L001").orElseThrow();
        Location l002 = locationRepository.findByLocationId("L002").orElseThrow();
        Location l003 = locationRepository.findByLocationId("L003").orElseThrow();
        Location l004 = locationRepository.findByLocationId("L004").orElseThrow();
        Location l005 = locationRepository.findByLocationId("L005").orElseThrow();
        Location l006 = locationRepository.findByLocationId("L006").orElseThrow();
        Location l007 = locationRepository.findByLocationId("L007").orElseThrow();
        Location l008 = locationRepository.findByLocationId("L008").orElseThrow();
        Location l009 = locationRepository.findByLocationId("L009").orElseThrow();
        Location l010 = locationRepository.findByLocationId("L010").orElseThrow();

        TimeWindow morningWindow = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(13, 0));
        TimeWindow afternoonWindow = new TimeWindow(LocalTime.of(13, 0), LocalTime.of(17, 0));
        TimeWindow eveningWindow = new TimeWindow(LocalTime.of(17, 0), LocalTime.of(21, 0));
        TimeWindow fullDayWindow = new TimeWindow(LocalTime.of(9, 0), LocalTime.of(21, 0));

        // High priority deliveries
        Delivery d1 = new Delivery("D001", warehouse, l001, 25.0, 1.5, 5);
        d1.setTimeWindow(morningWindow);
        d1.setCustomerPhone("+91-9811111111");
        d1.setNotes("Fragile - electronics");
        deliveryRepository.save(d1);

        Delivery d2 = new Delivery("D002", warehouse, l006, 18.0, 1.0, 5);
        d2.setTimeWindow(morningWindow);
        d2.setCustomerPhone("+91-9811111112");
        d2.setNotes("Medical supplies - urgent");
        deliveryRepository.save(d2);

        // Medium-high priority
        Delivery d3 = new Delivery("D003", warehouse, l003, 40.0, 3.0, 4);
        d3.setTimeWindow(afternoonWindow);
        d3.setCustomerPhone("+91-9811111113");
        d3.setNotes("Office supplies");
        deliveryRepository.save(d3);

        Delivery d4 = new Delivery("D004", warehouse, l008, 15.0, 1.2, 4);
        d4.setTimeWindow(morningWindow);
        d4.setCustomerPhone("+91-9811111114");
        d4.setNotes("Books and stationery");
        deliveryRepository.save(d4);

        Delivery d5 = new Delivery("D005", warehouse, l007, 22.0, 1.8, 4);
        d5.setTimeWindow(fullDayWindow);
        d5.setCustomerPhone("+91-9811111115");
        d5.setNotes("Clothing order");
        deliveryRepository.save(d5);

        // Medium priority
        Delivery d6 = new Delivery("D006", warehouse, l002, 35.0, 2.5, 3);
        d6.setTimeWindow(afternoonWindow);
        d6.setCustomerPhone("+91-9811111116");
        d6.setNotes("Household goods");
        deliveryRepository.save(d6);

        Delivery d7 = new Delivery("D007", warehouse, l009, 12.0, 0.8, 3);
        d7.setTimeWindow(eveningWindow);
        d7.setCustomerPhone("+91-9811111117");
        d7.setNotes("Food items");
        deliveryRepository.save(d7);

        Delivery d8 = new Delivery("D008", warehouse, l010, 8.0, 0.5, 3);
        d8.setTimeWindow(fullDayWindow);
        d8.setCustomerPhone("+91-9811111118");
        d8.setNotes("Small package");
        deliveryRepository.save(d8);

        // Lower priority
        Delivery d9 = new Delivery("D009", warehouse, l004, 60.0, 5.0, 2);
        d9.setTimeWindow(afternoonWindow);
        d9.setCustomerPhone("+91-9811111119");
        d9.setNotes("Construction materials");
        deliveryRepository.save(d9);

        Delivery d10 = new Delivery("D010", warehouse, l005, 28.0, 2.2, 2);
        d10.setTimeWindow(eveningWindow);
        d10.setCustomerPhone("+91-9811111120");
        d10.setNotes("Furniture part");
        deliveryRepository.save(d10);

        logger.info("Seeded 10 delivery orders across Delhi-NCR");
    }
}
