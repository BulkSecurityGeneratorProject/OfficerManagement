package com.app.web.rest;

import com.app.domain.Diary;
import com.app.domain.Officer;
import com.app.repository.DiaryRepository;
import com.app.repository.OfficerRepository;
import com.app.service.OfficerService;
import com.app.service.dto.OfficerDTO;
import com.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.app.domain.Officer}.
 */
@RestController
@RequestMapping("/api")
public class OfficerResource {

    private final Logger log = LoggerFactory.getLogger(OfficerResource.class);

    private static final String ENTITY_NAME = "officer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfficerRepository officerRepository;
    private final OfficerService officerService;
    private final DiaryRepository diaryRepository;

    public OfficerResource(OfficerRepository officerRepository, OfficerService officerService, DiaryRepository diaryRepository) {
        this.officerRepository = officerRepository;
        this.officerService = officerService;
        this.diaryRepository = diaryRepository;
    }

    /**
     * {@code POST  /officers} : Create a new officer.
     *
     * @param officer the officer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     * with body the new officer, or with status {@code 400 (Bad Request)} if
     * the officer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/officers")
    public ResponseEntity<Officer> createOfficer(@RequestBody Officer officer) throws URISyntaxException {
        log.debug("REST request to save Officer : {}", officer);
        if (officer.getId() != null) {
            throw new BadRequestAlertException("A new officer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Officer result = officerService.save(officer);
        if (result != null) {
            // create diary
            Diary diary = new Diary();
            diary.setContent("Create officer");
            diary.setTime(ZonedDateTime.now());
            diaryRepository.save(diary);
            //
        }
        return ResponseEntity.created(new URI("/api/officers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /officers} : Updates an existing officer.
     *
     * @param officer the officer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the updated officer, or with status {@code 400 (Bad Request)} if the
     * officer is not valid, or with status {@code 500 (Internal Server Error)}
     * if the officer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/officers")
    public ResponseEntity<Officer> updateOfficer(@RequestBody Officer officer) throws URISyntaxException {
        log.debug("REST request to update Officer : {}", officer);
        if (officer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Officer result = officerService.save(officer);
        if (result != null) {
            // create diary
            Diary diary = new Diary();
            diary.setContent("Update officer");
            diary.setTime(ZonedDateTime.now());
            diaryRepository.save(diary);
            //
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, officer.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /officers} : get all the officers.
     *
     * @param eagerload flag to eager load entities from relationships (This is
     * applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
     * list of officers in body.
     */
    @GetMapping("/officers")
    public List<OfficerDTO> getAllOfficers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Officers");
        return officerService.findAll();
    }

    /**
     * {@code GET  /officers/:id} : get the "id" officer.
     *
     * @param id the id of the officer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the officer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/officers/{id}")
    public ResponseEntity<OfficerDTO> getOfficer(@PathVariable Long id) {
        log.debug("REST request to get Officer : {}", id);
        OfficerDTO officer = officerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(officer));
    }

    /**
     * {@code DELETE  /officers/:id} : delete the "id" officer.
     *
     * @param id the id of the officer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/officers/{id}")
    public ResponseEntity<Void> deleteOfficer(@PathVariable Long id) {
        log.debug("REST request to delete Officer : {}", id);
        officerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code Get  /officers-by-unit/:key} : find officers by unit
     *
     * @param key : unit's name or part of unit's name
     * @return list of officers in body.
     */
    @GetMapping("/officers-by-unit/{key}")
    public List<OfficerDTO> findAllByUnit(@PathVariable(name = "key") String key) {
        log.debug("REST request to get all Officers by Unit");
        return officerService.findAllByUnit(key);
    }
}
