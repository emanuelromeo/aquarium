package com.develhope.aquarium.enumerations;

public enum FishSpecies {
    GOLDFISH("Goldfish"),
    CLOWNFISH("Clownfish");

    private final String description;

    FishSpecies(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
