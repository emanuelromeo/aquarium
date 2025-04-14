package com.develhope.aquarium.controllers;

import com.develhope.aquarium.entities.Aquarium;
import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.exceptions.AquariumCapacityExceededException;
import com.develhope.aquarium.exceptions.AquariumNotFoundException;
import com.develhope.aquarium.services.AquariumService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/aquariums")
public class AquariumController {

    @Autowired
    private AquariumService aquariumService;

    @PostMapping("/create")
    public ResponseEntity<Aquarium> create(@RequestBody Aquarium aquarium) {
        Aquarium savedAquarium = aquariumService.save(aquarium);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAquarium);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Aquarium>> findAll() {
        List<Aquarium> aquariums = aquariumService.findAll();
        return ResponseEntity.ok(aquariums);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Aquarium> findById(@PathVariable Long id) {
        Optional<Aquarium> aquarium = aquariumService.findById(id);

        if (aquarium.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aquarium.get());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Aquarium> update(
            @PathVariable Long id,
            @RequestBody Aquarium updatedAquarium) {

        Optional<Aquarium> aquarium = aquariumService.update(id, updatedAquarium);

        if (aquarium.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(aquarium.get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        aquariumService.delete(id);
        return ResponseEntity.ok("Aquarium deleted");
    }

    @PostMapping("{aquariumId}/add-fish")
    public ResponseEntity<?> addFish (
            @PathVariable Long aquariumId,
            @RequestParam String fishName,
            @RequestParam FishSpecies fishSpecies) {

        try {

            Fish fish = aquariumService.addFish(aquariumId, fishName, fishSpecies);
            return ResponseEntity.status(HttpStatus.CREATED).body(fish);

        } catch (AquariumNotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (AquariumCapacityExceededException e) {

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }

    }

    @PutMapping("{aquariumId}/feed-fishes")
    public ResponseEntity<?> feedFishes (
            @PathVariable Long aquariumId,
            @RequestParam @Min(0) @Max(100) Integer foodQuantity) {

        try {

            Aquarium aquarium = aquariumService.feedFishes(aquariumId, foodQuantity);
            return ResponseEntity.ok(aquarium);

        } catch (AquariumNotFoundException e) {

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }

    @PutMapping("{aquariumId}/clean")
    public ResponseEntity<?> clean(@PathVariable Long aquariumId) {

        try {

            Aquarium aquarium = aquariumService.clean(aquariumId);
            return ResponseEntity.ok(aquarium);

        } catch (AquariumNotFoundException e) {

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        }
    }
}
