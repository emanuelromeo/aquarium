package com.develhope.aquarium.services;

import com.develhope.aquarium.entities.Aquarium;
import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.exceptions.AquariumCapacityExceededException;
import com.develhope.aquarium.exceptions.AquariumNotFoundException;
import com.develhope.aquarium.repositories.AquariumRepository;
import com.develhope.aquarium.repositories.FishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AquariumService {

    @Autowired
    private AquariumRepository aquariumRepository;

    @Autowired
    private FishRepository fishRepository;

    public Aquarium save(Aquarium aquarium) {
        return aquariumRepository.save(aquarium);
    }

    public List<Aquarium> findAll() {
        return aquariumRepository.findAll();
    }

    public Fish addFish(Long aquariumId, String fishName, FishSpecies fishSpecies) throws AquariumNotFoundException, AquariumCapacityExceededException {

        // Find aquarium by id
        Optional<Aquarium> aquarium = aquariumRepository.findById(aquariumId);

        // Check if aquarium exists
        if (aquarium.isEmpty()) {
            throw new AquariumNotFoundException("Aquarium with id " + aquariumId + " not found");
        }

        // Check aquarium capacity
        if (aquarium.get().getFishes().size() >= aquarium.get().getCapacity()) {
            throw new AquariumCapacityExceededException("Aquarium is full");
        }

        // Create fish and put in aquarium
        Fish fish = new Fish(fishName, fishSpecies);
        fish.setAquarium(aquarium.get());

        return fishRepository.save(fish);

    }

    // Food quantity is a value between 0 (no food)
    // and 100 (enough food to feed all fishes if capacity were reached and they were all at hunger level 100).
    // Food is always equally spread between all fishes, even if a fish isn't hungry.
    // Exceeded food dirties the aquarium.
    public Aquarium feedFishes(Long aquariumId, Integer foodQuantity) throws AquariumNotFoundException {

        // Find aquarium by id
        Optional<Aquarium> aquarium = aquariumRepository.findById(aquariumId);

        // Check if aquarium exists
        if (aquarium.isEmpty()) {
            throw new AquariumNotFoundException("Aquarium with id " + aquariumId + " not found");
        }

        // Get fishes
        List<Fish> fishes = aquarium.get().getFishes();

        // Food quantity is related to the capacity of the aquarium.
        // If reached capacity, food quantity would be equivalent to hunger satisfied for fish,
        // or else it has to be proportioned to the number of fishes.

        // The formula to convert food quantity in hunger level is:
        // hunger = food * capacity / number of fishes

        int hungerSatisfiedForFish = (int) (foodQuantity * aquarium.get().getCapacity() / fishes.size());

        for (Fish fish : fishes) {
            if (fish.getHunger() < hungerSatisfiedForFish) {

                // Dirtiness of the aquarium is the sum of all wasted food.
                // It's equivalent to the sum of over-satisfied hunger of all the fishes
                // (which is proportioned to the number of fishes), divided by the capacity of the aquarium.

                // From the same formula used to calculate hunger satisfied for fish:
                // food = hunger * number of fishes / capacity

                // The formula is valid if all fishes has the same level of hunger,
                // otherwise it has to be adapted as follows:
                // food = sum of all hunger / capacity

                Integer wastedFood = (int) ((hungerSatisfiedForFish - fish.getHunger())/aquarium.get().getCapacity());
                aquarium.get().dirty(wastedFood);
            }

            // Feed fish and update it
            fish.feed(hungerSatisfiedForFish);
            fishRepository.save(fish);
        }

        // Update and return aquarium
        return aquariumRepository.save(aquarium.get());

    }
}
