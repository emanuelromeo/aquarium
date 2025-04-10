package com.develhope.aquarium.entities;

import com.develhope.aquarium.enumerations.FishSpecies;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "fishes")
public class Fish {

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
    private Integer hunger;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "health")
    private Integer health;

    @Min(value = 0)
    @Column(name = "age")
    private Long age;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "aquarium_id")
    private Aquarium aquarium;


    // Constructors

    public Fish() {
    }

    public Fish(String name, FishSpecies species) {
        this.name = name;
        this.species = species;
        this.hunger = 0;
        this.health = 100;
        this.age = 0L;
    }


    // Methods

    public void feed(Integer foodQuantity) {
        health = Math.min(100, health + foodQuantity);
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
