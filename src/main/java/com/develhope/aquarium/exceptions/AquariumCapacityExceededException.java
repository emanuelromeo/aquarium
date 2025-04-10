package com.develhope.aquarium.exceptions;

public class AquariumCapacityExceededException extends RuntimeException {
    public AquariumCapacityExceededException(String message) {
        super(message);
    }
}
