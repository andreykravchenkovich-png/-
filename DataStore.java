package com.autodealer.database;

import com.autodealer.models.Car;
import com.autodealer.models.Part;
import com.autodealer.models.TestDrive;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    private List<Car> cars;
    private List<Part> parts;
    private List<TestDrive> testDrives;
    private int carIdCounter;
    private int partIdCounter;
    private int testDriveIdCounter;

    private DataStore() {
        cars = new ArrayList<>();
        parts = new ArrayList<>();
        testDrives = new ArrayList<>();
        carIdCounter = 1;
        partIdCounter = 1;
        testDriveIdCounter = 1;
        initTestData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initTestData() {
        // Автомобили с путями к картинкам (в папке resources/images/)
        addCar("Toyota", "Camry", 2024, 3500000, "2.5 л", "Автомат", "Белый", "В наличии", "/images/cars/toyota_camry.jpg");
        addCar("BMW", "X5", 2024, 6500000, "3.0 л", "Автомат", "Черный", "В наличии", "/images/cars/bmw_x5.jpg");
        addCar("Mercedes", "E-Class", 2024, 5000000, "2.0 л", "Автомат", "Серебристый", "Забронирован", "/images/cars/mercedes_e.jpg");
        addCar("Audi", "A4", 2023, 3800000, "2.0 л", "Автомат", "Синий", "В наличии", "/images/cars/audi_a4.jpg");
        addCar("Hyundai", "Santa Fe", 2023, 3200000, "2.2 л дизель", "Автомат", "Серый", "В наличии", "/images/cars/hyundai_santafe.jpg");
        addCar("Kia", "Sportage", 2024, 3400000, "2.0 л", "Автомат", "Красный", "В наличии", "/images/cars/kia_sportage.jpg");
        addCar("Lada", "Vesta", 2023, 1500000, "1.6 л", "Механика", "Белый", "В наличии", "/images/cars/lada_vesta.jpg");
        addCar("Tesla", "Model Y", 2024, 6000000, "Электро", "Автомат", "Белый", "В наличии", "/images/cars/tesla_model_y.jpg");
        addCar("Porsche", "Cayenne", 2024, 11000000, "3.0 л V6", "Автомат", "Черный", "Забронирован", "/images/cars/porsche_cayenne.jpg");
        addCar("Land Rover", "Range Rover", 2024, 15000000, "5.0 л V8", "Автомат", "Серебристый", "В наличии", "/images/cars/range_rover.jpg");

        // Запчасти
        addPart("Моторное масло 5W-30", 1300, "Масла", "Синтетическое моторное масло", "/images/parts/oil.jpg");
        addPart("Свечи зажигания", 1500, "Электрика", "Комплект свечей зажигания", "/images/parts/spark_plugs.jpg");
        addPart("Багажник на крышу", 3500, "Аксессуары", "Универсальный багажник", "/images/parts/roof_rack.jpg");
        addPart("Стекло лобовое", 2700, "Стекла", "Лобовое стекло с обогревом", "/images/parts/windshield.jpg");
        addPart("Тормозные колодки", 800, "Тормозная система", "Комплект передних колодок", "/images/parts/brake_pads.jpg");
        addPart("Фильтр масляный", 400, "Фильтры", "Масляный фильтр", "/images/parts/oil_filter.jpg");
        addPart("Аккумулятор", 4500, "Электрика", "Аккумулятор 60 Ач", "/images/parts/battery.jpg");
        addPart("Диски колесные", 8000, "Колеса", "Легкосплавные диски R17", "/images/parts/wheels.jpg");
    }

    public void addCar(String brand, String model, int year, int price,
                       String engine, String transmission, String color, String status, String imagePath) {
        Car car = new Car(carIdCounter++, brand, model, year, price,
                engine, transmission, color, status, imagePath);
        cars.add(car);
    }

    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }

    public List<Car> getAvailableCars() {
        List<Car> available = new ArrayList<>();
        for (Car car : cars) {
            if (car.getStatus().equals("В наличии")) {
                available.add(car);
            }
        }
        return available;
    }

    public Car getCarById(int id) {
        for (Car car : cars) {
            if (car.getId() == id) {
                return car;
            }
        }
        return null;
    }

    public boolean updateCarStatus(int id, String status) {
        Car car = getCarById(id);
        if (car != null) {
            car.setStatus(status);
            return true;
        }
        return false;
    }

    public void addPart(String name, int price, String category, String description, String imagePath) {
        Part part = new Part(partIdCounter++, name, price, category, description, imagePath);
        parts.add(part);
    }

    public List<Part> getAllParts() {
        return new ArrayList<>(parts);
    }

    public List<Part> getPartsByCategory(String category) {
        List<Part> result = new ArrayList<>();
        for (Part part : parts) {
            if (part.getCategory().equals(category)) {
                result.add(part);
            }
        }
        return result;
    }

    public void addTestDrive(String carName, String clientName, String phone, String date, String time) {
        TestDrive td = new TestDrive(testDriveIdCounter++, carName, clientName, phone, date, time);
        testDrives.add(td);
    }

    public List<TestDrive> getAllTestDrives() {
        return new ArrayList<>(testDrives);
    }

    public void addOrder(int id, String fullName, String name, String phone, String email, String payment) {
    }
}
