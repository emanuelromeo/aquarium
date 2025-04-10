package com.develhope.aquarium.controllers;

import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.services.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fishes")
public class FishController {

    @Autowired
    private FishService fishService;

    @PostMapping("/create")
    public ResponseEntity<Fish> create(@RequestBody Fish fish) {
        Fish savedFish = fishService.save(fish);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFish);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Fish>> findAll() {
        List<Fish> fishes = fishService.findAll();
        return ResponseEntity.ok(fishes);
    }

}
