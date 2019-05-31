package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.DeviceType;
import io.github.jhipster.application.repository.DeviceTypeRepository;
import io.github.jhipster.application.repository.search.DeviceTypeSearchRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.DeviceType}.
 */
@RestController
@RequestMapping("/api")
public class DeviceTypeResource {

    private final Logger log = LoggerFactory.getLogger(DeviceTypeResource.class);

    private static final String ENTITY_NAME = "jhipsterdeviceserviceDeviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceTypeRepository deviceTypeRepository;

    private final DeviceTypeSearchRepository deviceTypeSearchRepository;

    public DeviceTypeResource(DeviceTypeRepository deviceTypeRepository, DeviceTypeSearchRepository deviceTypeSearchRepository) {
        this.deviceTypeRepository = deviceTypeRepository;
        this.deviceTypeSearchRepository = deviceTypeSearchRepository;
    }

    /**
     * {@code POST  /device-types} : Create a new deviceType.
     *
     * @param deviceType the deviceType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceType, or with status {@code 400 (Bad Request)} if the deviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-types")
    public ResponseEntity<DeviceType> createDeviceType(@RequestBody DeviceType deviceType) throws URISyntaxException {
        log.debug("REST request to save DeviceType : {}", deviceType);
        if (deviceType.getId() != null) {
            throw new BadRequestAlertException("A new deviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceType result = deviceTypeRepository.save(deviceType);
        deviceTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/device-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-types} : Updates an existing deviceType.
     *
     * @param deviceType the deviceType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceType,
     * or with status {@code 400 (Bad Request)} if the deviceType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-types")
    public ResponseEntity<DeviceType> updateDeviceType(@RequestBody DeviceType deviceType) throws URISyntaxException {
        log.debug("REST request to update DeviceType : {}", deviceType);
        if (deviceType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceType result = deviceTypeRepository.save(deviceType);
        deviceTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deviceType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-types} : get all the deviceTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceTypes in body.
     */
    @GetMapping("/device-types")
    public List<DeviceType> getAllDeviceTypes() {
        log.debug("REST request to get all DeviceTypes");
        return deviceTypeRepository.findAll();
    }

    /**
     * {@code GET  /device-types/:id} : get the "id" deviceType.
     *
     * @param id the id of the deviceType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-types/{id}")
    public ResponseEntity<DeviceType> getDeviceType(@PathVariable Long id) {
        log.debug("REST request to get DeviceType : {}", id);
        Optional<DeviceType> deviceType = deviceTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deviceType);
    }

    /**
     * {@code DELETE  /device-types/:id} : delete the "id" deviceType.
     *
     * @param id the id of the deviceType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-types/{id}")
    public ResponseEntity<Void> deleteDeviceType(@PathVariable Long id) {
        log.debug("REST request to delete DeviceType : {}", id);
        deviceTypeRepository.deleteById(id);
        deviceTypeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/device-types?query=:query} : search for the deviceType corresponding
     * to the query.
     *
     * @param query the query of the deviceType search.
     * @return the result of the search.
     */
    @GetMapping("/_search/device-types")
    public List<DeviceType> searchDeviceTypes(@RequestParam String query) {
        log.debug("REST request to search DeviceTypes for query {}", query);
        return StreamSupport
            .stream(deviceTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
