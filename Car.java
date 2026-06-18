package com.autodealer.models;

public class Car {
    private int id;
    private String brand;
    private String model;
    private int year;
    private int price;
    private String engine;
    private String transmission;
    private String color;
    private String status;
    private String imagePath; // Путь к картинке

    public Car(int id, String brand, String model, int year, int price,
               String engine, String transmission, String color, String status, String imagePath) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.engine = engine;
        this.transmission = transmission;
        this.color = color;
        this.status = status;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public int getPrice() { return price; }
    public String getEngine() { return engine; }
    public String getTransmission() { return transmission; }
    public String getColor() { return color; }
    public String getStatus() { return status; }
    public String getImagePath() { return imagePath; }
    public String getFullName() { return brand + " " + model; }
    public void setStatus(String status) { this.status = status; }
}
