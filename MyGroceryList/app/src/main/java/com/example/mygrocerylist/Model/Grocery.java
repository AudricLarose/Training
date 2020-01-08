package com.example.mygrocerylist.Model;

public class Grocery {
    private String name;

    public Grocery(String name, String quality, String dateItemAdded, int id) {
        this.name = name;
        this.quality = quality;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public Grocery() {
    }

    private String quality;
    private String dateItemAdded;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
