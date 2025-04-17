package com.develhope.aquarium;

import com.develhope.aquarium.entities.Aquarium;
import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.services.AquariumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AquariumTest {

    private Aquarium aquarium;
    private Fish fish;

    @MockitoBean
    private AquariumService aquariumService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        aquarium = new Aquarium();
        aquarium.setId(1L);
        aquarium.setCapacity(10L);

        fish = new Fish();
        fish.setId(1L);
        fish.setName("David");
        fish.setSpecies(FishSpecies.GOLDFISH);

        fish.setAquarium(aquarium);
    }

    // Test POST request to create an aquarium
    @Test
    public void createAquarium() throws Exception {
        when(aquariumService.save(any(Aquarium.class))).thenReturn(aquarium);

        mockMvc.perform(post("/aquariums/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aquarium)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(aquarium.getId()));
    }

    // Test GET request to find all aquariums
    @Test
    public void findAll() throws Exception {
        when(aquariumService.findAll()).thenReturn(Collections.singletonList(aquarium));

        mockMvc.perform(get("/aquariums/find-all"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    // Test GET request to find an ability by id
    @Test
    public void findById() throws Exception {
        when(aquariumService.findById(anyLong())).thenReturn(Optional.of(aquarium));

        mockMvc.perform(get("/aquariums/find-by-id/" + aquarium.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aquarium.getId()));
    }

    // Test PUT request to update an aquarium
    @Test
    public void update() throws Exception {
        when(aquariumService.update(anyLong(), any(Aquarium.class))).thenReturn(Optional.of(aquarium));

        mockMvc.perform(put("/aquariums/update/" + aquarium.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(aquarium)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(aquarium.getId()));
    }

    // Test DELETE request to delete an aquarium by id
    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/aquariums/delete/" + aquarium.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Aquarium deleted"));
    }

    // Test POST request to create and add fish to aquarium
    @Test
    public void addFish() throws Exception {
        when(aquariumService.addFish(anyLong(), anyString(), any(FishSpecies.class))).thenReturn(fish);

        mockMvc.perform(post("/aquariums/" + aquarium.getId() + "/add-fish")
                    .param("fishName", fish.getName())
                    .param("fishSpecies", fish.getSpecies().toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(fish.getName()));
    }

    // Test PUT request to clean the aquarium
    @Test
    public void clean() throws Exception {
        when(aquariumService.clean(anyLong())).thenReturn(aquarium);

        mockMvc.perform(put("/aquariums" + aquarium.getId() + "/clean"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

