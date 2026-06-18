package com.autodealer.models;

public class TestDrive {
    private int id;
    private String carName;
    private String clientName;
    private String phone;
    private String date;
    private String time;

    public TestDrive(int id, String carName, String clientName, String phone, String date, String time) {
        this.id = id;
        this.carName = carName;
        this.clientName = clientName;
        this.phone = phone;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }
    public String getCarName() { return carName; }
    public String getClientName() { return clientName; }
    public String getPhone() { return phone; }
    public String getDate() { return date; }
    public String getTime() { return time; }
}
