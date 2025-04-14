package com.develhope.aquarium.controllers;

import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.services.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
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

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Fish> findById(
            @PathVariable Long id) {

        Optional<Fish> fish = fishService.findById(id);

        if (fish.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fish.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Fish> update(
            @PathVariable Long id,
            @RequestBody Fish updatedFish) {

        Optional<Fish> fish = fishService.update(id, updatedFish);

        if (fish.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(fish.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        fishService.deleteById(id);
        return ResponseEntity.ok("Fish deleted");
    }

}
