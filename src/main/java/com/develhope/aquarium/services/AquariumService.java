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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AquariumService {

    @Autowired
    private AquariumRepository aquariumRepository;

    @Autowired
    private FishRepository fishRepository;

    /**
     * Saves the given aquarium in the database.
     * @param aquarium
     * @return the saved aquarium.
     */
    public Aquarium save(Aquarium aquarium) {
        return aquariumRepository.save(aquarium);
    }

    /**
     * Finds all aquariums in database.
     * @return the list of found aquariums.
     */
    public List<Aquarium> findAll() {
        return aquariumRepository.findAll();
    }

    /**
     * Finds the aquarium with the given id.
     * @param id
     * @return an optional containing the found aquarium.
     */
    public Optional<Aquarium> findById(Long id) {
        return aquariumRepository.findById(id);
    }

    /**
     * Updates the aquarium with the given id with values from the given updated aquarium
     * @param id
     * @param updatedAquarium
     * @return an optional containing the updated aquarium
     */
    public Optional<Aquarium> update(Long id, Aquarium updatedAquarium) {

        Optional<Aquarium> aquarium = aquariumRepository.findById(id);

        if (aquarium.isEmpty()) {
            return Optional.empty();
        }

        aquarium.get().setCapacity(updatedAquarium.getCapacity());
        aquarium.get().setClearness(updatedAquarium.getClearness());
        aquarium.get().setTemperature(updatedAquarium.getTemperature());

        Aquarium savedAquarium = aquariumRepository.save(aquarium.get());
        return  Optional.of(savedAquarium);
    }

    /**
     * Deletes the aquarium with the given id
     * @param id
     */
    public void delete(Long id) {
        aquariumRepository.deleteById(id);
    }

    /**
     * Saves and put a new fish into the aquarium with the given id.
     * @param aquariumId
     * @param fishName
     * @param fishSpecies
     * @return the saved fish.
     * @throws AquariumNotFoundException
     * @throws AquariumCapacityExceededException
     */
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




    /**
     * Gives the given food amount to all fishes of the aquarium with the given id.
     * Note:
     * Food quantity is a value between 0 (no food)
     * and 100 (enough food to feed all fishes if capacity were reached, and they were all at hunger level 100).
     * Food is always equally spread between all fishes, even if a fish isn't hungry.
     * Exceeded food dirties the aquarium.
     * @param aquariumId
     * @param foodQuantity
     * @return the updated aquarium.
     * @throws AquariumNotFoundException
     */
    public Aquarium feedFishes(Long aquariumId, Integer foodQuantity) throws AquariumNotFoundException {

        // Find aquarium by id
        Optional<Aquarium> aquarium = aquariumRepository.findById(aquariumId);

        // Check if aquarium exists
        if (aquarium.isEmpty()) {
            throw new AquariumNotFoundException("Aquarium with id " + aquariumId + " not found");
        }

        // Get fishes
        List<Fish> fishes = new ArrayList<>(aquarium.get().getFishes());

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

    /**
     * Increase hunger and updates health of all fishes and decrease the clearness of all aquariums.
     */
    @Transactional
    public void updateStats() {

        // Find all aquarium
        List<Aquarium> aquariums = aquariumRepository.findAll();

        for (Aquarium aquarium : aquariums) {

            // Decrease aquarium clearness
            aquarium.dirty(1);

            // Clone fish list
            List<Fish> fishes = new ArrayList<>(aquarium.getFishes());

            for (Fish fish : fishes) {

                // Increase fish hunger
                fish.increaseHunger();

                // Update fish health
                fish.updateHealth();

                // If aquarium is dirty, decrease fish health
                if (aquarium.getClearness() <= aquarium.LOW_CLEARNESS) {
                    fish.decreaseHealth();
                }

                if (fish.getHealth() <= 0) {

                    // If fish died delete it ...
                    aquarium.removeFish(fish);
                    fish.setAquarium(null);
                    fishRepository.delete(fish);

                } else {

                    // ... or else save it
                    fishRepository.save(fish);

                }
            }

            // Save aquarium updates
            aquariumRepository.save(aquarium);
        }
    }

    /**
     * Increases all fishes age.
     */
    @Transactional
    public void updateFishesAge() {

        // Find all aquarium
        List<Aquarium> aquariums = aquariumRepository.findAll();

        for (Aquarium aquarium : aquariums) {

            // Get fishes for any aquarium
            List<Fish> fishes = new ArrayList<>(aquarium.getFishes());

            for (Fish fish : fishes) {

                // Increase fish age and save
                fish.increaseAge();
                fishRepository.save(fish);

            }
        }
    }


    /**
     * Cleans the aquarium with the given id.
     * @param id
     * @return the updated aquarium.
     * @throws AquariumNotFoundException
     */
    public Aquarium clean(Long id) throws AquariumNotFoundException {
        Optional<Aquarium> aquarium = aquariumRepository.findById(id);

        if (aquarium.isEmpty()) {
            throw new AquariumNotFoundException("Aquarium with id " + id + " not found");
        }

        aquarium.get().clean();
        return aquariumRepository.save(aquarium.get());
    }
}
