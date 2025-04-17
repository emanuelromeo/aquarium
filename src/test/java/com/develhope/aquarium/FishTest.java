package com.develhope.aquarium;

import com.develhope.aquarium.entities.Fish;
import com.develhope.aquarium.enumerations.FishSpecies;
import com.develhope.aquarium.services.AquariumService;
import com.develhope.aquarium.services.FishService;
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
public class FishTest {

    private Fish fish;

    @MockitoBean
    private FishService fishService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        fish = new Fish();
        fish.setId(1L);
        fish.setName("David");
        fish.setSpecies(FishSpecies.GOLDFISH);
    }

    // Test POST request to create a fish
    @Test
    public void create() throws Exception {
        when(fishService.save(any(Fish.class))).thenReturn(fish);

        mockMvc.perform(post("/fishes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fish)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(fish.getName()));
    }

    // Test GET request to find all fishes
    @Test
    public void findAll() throws Exception {
        when(fishService.findAll()).thenReturn(Collections.singletonList(fish));

        mockMvc.perform(get("/fishes/find-all"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    // Test GET request to find a fish by id
    @Test
    public void findById() throws Exception {
        when(fishService.findById(anyLong())).thenReturn(Optional.of(fish));

        mockMvc.perform(get("/fishes/find-by-id/" + fish.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(fish.getName()));
    }

    // Test PUT request to update a fish
    @Test
    public void update() throws Exception {
        when(fishService.update(anyLong(), any(Fish.class))).thenReturn(Optional.of(fish));

        mockMvc.perform(put("/fishes/update/" + fish.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fish)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(fish.getName()));
    }

    // Test DELETE request to delete a fish by id
    @Test
    public void deleteById() throws Exception {

        mockMvc.perform(delete("/fishes/delete/" + fish.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Fish deleted"));
    }
}

