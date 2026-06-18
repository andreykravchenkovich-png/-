package com.autodealer.models;

public class Part {
    private int id;
    private String name;
    private int price;
    private String category;
    private String description;
    private String imagePath;

    public Part(int id, String name, int price, String category, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getImagePath() { return imagePath; }
}
