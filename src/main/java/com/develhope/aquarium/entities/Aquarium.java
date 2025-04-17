package com.develhope.aquarium.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
@Table(name = "aquariums")
public class Aquarium {

    private final Integer STARTING_CLEARNESS = 100;
    private final Integer STARTING_TEMPERATURE = 30;

    public final Integer LOW_CLEARNESS = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "capacity")
    private Long capacity;

    @Min(0)
    @Max(100)
    @Column(name = "clearness")
    private Integer clearness = STARTING_CLEARNESS;

    @Min(-273)
    @Column(name = "temperature")
    private Integer temperature = STARTING_TEMPERATURE;

    @OneToMany(mappedBy = "aquarium", cascade = CascadeType.ALL)
    private List<Fish> fishes;


    // Constructors

    public Aquarium() {
    }

    public Aquarium(Long id, Long capacity, Integer clearness, Integer temperature, List<Fish> fishes) {
        this.id = id;
        this.capacity = capacity;
        this.clearness = clearness;
        this.temperature = temperature;
        this.fishes = fishes;
    }


    // Methods

    /**
     * Reduces aquarium clearness by the given dirt quantity
     * which is expressed in percentage of max clearness of the aquarium.
     * @param dirtQuantity
     */
    public void dirty(Integer dirtQuantity) {
        clearness = Math.max(0, clearness - dirtQuantity);
    }

    /**
     * Set aquarium clearness to the max value.
     */
    public void clean() {
        clearness = 100;
    }

    /**
     * Adds the given fish to the aquarium
     * @param fish
     */
    public void addFish(Fish fish) {
        fishes.add(fish);
    }

    /**
     * Removes the given fish from the aquarium
     * @param fish
     */
    public void removeFish(Fish fish) {
        fishes.remove(fish);
    }


    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public @Min(0) @Max(100) Integer getClearness() {
        return clearness;
    }

    public void setClearness(@Min(0) @Max(100) Integer clearness) {
        this.clearness = clearness;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public List<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(List<Fish> fishes) {
        this.fishes = fishes;
    }
}
