package com.develhope.aquarium.services;

import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.repositories.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FishService {

    @Autowired
    private FishRepository fishRepository;

    public Fish save(Fish fish) {
        return fishRepository.save(fish);
    }

    public Optional<Fish> findById(Long id) {
        return fishRepository.findById(id);
    }

    public List<Fish> findAll() {
        return fishRepository.findAll();
    }

    public Optional<Fish> update(Long id, Fish updatedFish) {
        Optional<Fish> fish = fishRepository.findById(id);

        if (fish.isPresent()) {
            fish.get().setName(updatedFish.getName());
            fish.get().setSpecies(updatedFish.getSpecies());
            fish.get().setHunger(updatedFish.getHunger());
            fish.get().setHealth(updatedFish.getHealth());
            fish.get().setAge(updatedFish.getAge());

            Fish savedFish = fishRepository.save(fish.get());
            return Optional.of(savedFish);
        }

        return Optional.empty();
    }

    public void deleteById(Long id) {
        fishRepository.deleteById(id);
    }
}
