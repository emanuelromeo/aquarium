package com.develhope.aquarium.entities;

import com.develhope.aquarium.enumerations.FishSpecies;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "fishes")
public class Fish {

    private final Integer STARTING_HEALTH = 100;
    private final Integer STARTING_HUNGER = 0;
    private final Long STARTING_AGE = 0L;

    private final Integer LOW_HUNGER = 30;
    private final Integer HIGH_HUNGER = 70;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "species")
    @Enumerated(value = EnumType.STRING)
    private FishSpecies species;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "hunger")
    private Integer hunger = STARTING_HUNGER;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "health")
    private Integer health = STARTING_HEALTH;

    @Min(value = 0)
    @Column(name = "age")
    private Long age = STARTING_AGE;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aquarium_id")
    private Aquarium aquarium;


    // Constructors

    public Fish() {
    }

    public Fish(String name, FishSpecies species) {
        this.name = name;
        this.species = species;
    }


    // Methods

    /**
     * Decrease fish hunger by the given quantity of food, which is expressed in percentage of the max hunger of the fish.
     * @param foodQuantity
     */
    public void feed(Integer foodQuantity) {
        hunger = Math.max(0, hunger - foodQuantity);
    }

    /**
     * Increases fish health if hunger is low, or decrease health if hunger is high.
     */
    public void updateHealth() {

        if (hunger <= LOW_HUNGER) {
            health = Math.min(100, health + 1);
        }

        if (hunger > HIGH_HUNGER) {
            health = Math.max(0, health - 1);
        }

    }

    /**
     * Decreases fish health by 1.
     */
    public void decreaseHealth() {
        health = Math.max(0, health - 1);
    }

    /**
     * Increases fish hunger by 1.
     */
    public void increaseHunger() {
        hunger = Math.min(100, hunger + 1);
    }

    /**
     * Increases fish age by 1.
     */
    public void increaseAge() {
        age++;
    }


    // Getter and Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FishSpecies getSpecies() {
        return species;
    }

    public void setSpecies(FishSpecies species) {
        this.species = species;
    }

    public @Min(value = 0) @Max(value = 100) Integer getHunger() {
        return hunger;
    }

    public void setHunger(@Min(value = 0) @Max(value = 100) Integer hunger) {
        this.hunger = hunger;
    }

    public @Min(value = 0) @Max(value = 100) Integer getHealth() {
        return health;
    }

    public void setHealth(@Min(value = 0) @Max(value = 100) Integer health) {
        this.health = health;
    }

    public @Min(value = 0) Long getAge() {
        return age;
    }

    public void setAge(@Min(value = 0) Long age) {
        this.age = age;
    }

    public Aquarium getAquarium() {
        return aquarium;
    }

    public void setAquarium(Aquarium aquarium) {
        this.aquarium = aquarium;
    }
}
