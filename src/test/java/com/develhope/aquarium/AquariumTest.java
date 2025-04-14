package com.develhope.aquarium;

import com.develhope.aquarium.entities.Aquarium;
import com.develhope.aquarium.entities.Fish;
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

    @Test
    public void deleteById() throws Exception {
        mockMvc.perform(delete("/aquariums/delete/" + aquarium.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Aquarium deleted"));
    }
}

