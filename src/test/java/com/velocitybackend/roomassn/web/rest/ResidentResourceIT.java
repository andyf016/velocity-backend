package com.velocitybackend.roomassn.web.rest;

import com.velocitybackend.roomassn.RoomassnApp;
import com.velocitybackend.roomassn.domain.Resident;
import com.velocitybackend.roomassn.domain.Room;
import com.velocitybackend.roomassn.repository.ResidentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResidentResource} REST controller.
 */
@SpringBootTest(classes = RoomassnApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResidentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResidentMockMvc;

    private Resident resident;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resident createEntity(EntityManager em) {
        Resident resident = new Resident()
            .name(DEFAULT_NAME);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        resident.setRoom(room);
        return resident;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resident createUpdatedEntity(EntityManager em) {
        Resident resident = new Resident()
            .name(UPDATED_NAME);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        resident.setRoom(room);
        return resident;
    }

    @BeforeEach
    public void initTest() {
        resident = createEntity(em);
    }

    @Test
    @Transactional
    public void createResident() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();
        // Create the Resident
        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resident)))
            .andExpect(status().isCreated());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate + 1);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createResidentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = residentRepository.findAll().size();

        // Create the Resident with an existing ID
        resident.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resident)))
            .andExpect(status().isBadRequest());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = residentRepository.findAll().size();
        // set the field null
        resident.setName(null);

        // Create the Resident, which fails.


        restResidentMockMvc.perform(post("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resident)))
            .andExpect(status().isBadRequest());

        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResidents() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get all the residentList
        restResidentMockMvc.perform(get("/api/residents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resident.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", resident.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resident.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingResident() throws Exception {
        // Get the resident
        restResidentMockMvc.perform(get("/api/residents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // Update the resident
        Resident updatedResident = residentRepository.findById(resident.getId()).get();
        // Disconnect from session so that the updates on updatedResident are not directly saved in db
        em.detach(updatedResident);
        updatedResident
            .name(UPDATED_NAME);

        restResidentMockMvc.perform(put("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedResident)))
            .andExpect(status().isOk());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate);
        Resident testResident = residentList.get(residentList.size() - 1);
        assertThat(testResident.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingResident() throws Exception {
        int databaseSizeBeforeUpdate = residentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResidentMockMvc.perform(put("/api/residents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resident)))
            .andExpect(status().isBadRequest());

        // Validate the Resident in the database
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResident() throws Exception {
        // Initialize the database
        residentRepository.saveAndFlush(resident);

        int databaseSizeBeforeDelete = residentRepository.findAll().size();

        // Delete the resident
        restResidentMockMvc.perform(delete("/api/residents/{id}", resident.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resident> residentList = residentRepository.findAll();
        assertThat(residentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
