package com.velocitybackend.roomassn.web.rest;

import com.velocitybackend.roomassn.domain.Resident;
import com.velocitybackend.roomassn.repository.ResidentRepository;
import com.velocitybackend.roomassn.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.velocitybackend.roomassn.domain.Resident}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResidentResource {

    private final Logger log = LoggerFactory.getLogger(ResidentResource.class);

    private static final String ENTITY_NAME = "resident";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResidentRepository residentRepository;

    public ResidentResource(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    /**
     * {@code POST  /residents} : Create a new resident.
     *
     * @param resident the resident to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resident, or with status {@code 400 (Bad Request)} if the resident has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/residents")
    public ResponseEntity<Resident> createResident(@Valid @RequestBody Resident resident) throws URISyntaxException {
        log.debug("REST request to save Resident : {}", resident);
        if (resident.getId() != null) {
            throw new BadRequestAlertException("A new resident cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resident result = residentRepository.save(resident);
        return ResponseEntity.created(new URI("/api/residents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /residents} : Updates an existing resident.
     *
     * @param resident the resident to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resident,
     * or with status {@code 400 (Bad Request)} if the resident is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resident couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/residents")
    public ResponseEntity<Resident> updateResident(@Valid @RequestBody Resident resident) throws URISyntaxException {
        log.debug("REST request to update Resident : {}", resident);
        if (resident.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Resident result = residentRepository.save(resident);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resident.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /residents} : get all the residents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of residents in body.
     */
    @GetMapping("/residents")
    public ResponseEntity<List<Resident>> getAllResidents(Pageable pageable) {
        log.debug("REST request to get a page of Residents");
        Page<Resident> page = residentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /residents/:id} : get the "id" resident.
     *
     * @param id the id of the resident to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resident, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/residents/{id}")
    public ResponseEntity<Resident> getResident(@PathVariable Long id) {
        log.debug("REST request to get Resident : {}", id);
        Optional<Resident> resident = residentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resident);
    }

    /**
     * {@code DELETE  /residents/:id} : delete the "id" resident.
     *
     * @param id the id of the resident to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/residents/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable Long id) {
        log.debug("REST request to delete Resident : {}", id);
        residentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
