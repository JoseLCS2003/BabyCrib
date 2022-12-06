package com.example.babycrib.Modelos;

public class Cuna {
    private final String name,description;

    public Cuna(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
