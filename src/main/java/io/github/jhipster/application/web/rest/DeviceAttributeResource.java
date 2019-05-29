package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.DeviceAttribute;
import io.github.jhipster.application.repository.DeviceAttributeRepository;
import io.github.jhipster.application.repository.search.DeviceAttributeSearchRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.DeviceAttribute}.
 */
@RestController
@RequestMapping("/api")
public class DeviceAttributeResource {

    private final Logger log = LoggerFactory.getLogger(DeviceAttributeResource.class);

    private static final String ENTITY_NAME = "jhipsterdeviceserviceDeviceAttribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceAttributeRepository deviceAttributeRepository;

    private final DeviceAttributeSearchRepository deviceAttributeSearchRepository;

    public DeviceAttributeResource(DeviceAttributeRepository deviceAttributeRepository, DeviceAttributeSearchRepository deviceAttributeSearchRepository) {
        this.deviceAttributeRepository = deviceAttributeRepository;
        this.deviceAttributeSearchRepository = deviceAttributeSearchRepository;
    }

    /**
     * {@code POST  /device-attributes} : Create a new deviceAttribute.
     *
     * @param deviceAttribute the deviceAttribute to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceAttribute, or with status {@code 400 (Bad Request)} if the deviceAttribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-attributes")
    public ResponseEntity<DeviceAttribute> createDeviceAttribute(@RequestBody DeviceAttribute deviceAttribute) throws URISyntaxException {
        log.debug("REST request to save DeviceAttribute : {}", deviceAttribute);
        if (deviceAttribute.getId() != null) {
            throw new BadRequestAlertException("A new deviceAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceAttribute result = deviceAttributeRepository.save(deviceAttribute);
        deviceAttributeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/device-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-attributes} : Updates an existing deviceAttribute.
     *
     * @param deviceAttribute the deviceAttribute to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceAttribute,
     * or with status {@code 400 (Bad Request)} if the deviceAttribute is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceAttribute couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-attributes")
    public ResponseEntity<DeviceAttribute> updateDeviceAttribute(@RequestBody DeviceAttribute deviceAttribute) throws URISyntaxException {
        log.debug("REST request to update DeviceAttribute : {}", deviceAttribute);
        if (deviceAttribute.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceAttribute result = deviceAttributeRepository.save(deviceAttribute);
        deviceAttributeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deviceAttribute.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-attributes} : get all the deviceAttributes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceAttributes in body.
     */
    @GetMapping("/device-attributes")
    public List<DeviceAttribute> getAllDeviceAttributes() {
        log.debug("REST request to get all DeviceAttributes");
        return deviceAttributeRepository.findAll();
    }

    /**
     * {@code GET  /device-attributes/:id} : get the "id" deviceAttribute.
     *
     * @param id the id of the deviceAttribute to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceAttribute, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-attributes/{id}")
    public ResponseEntity<DeviceAttribute> getDeviceAttribute(@PathVariable Long id) {
        log.debug("REST request to get DeviceAttribute : {}", id);
        Optional<DeviceAttribute> deviceAttribute = deviceAttributeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deviceAttribute);
    }

    /**
     * {@code DELETE  /device-attributes/:id} : delete the "id" deviceAttribute.
     *
     * @param id the id of the deviceAttribute to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-attributes/{id}")
    public ResponseEntity<Void> deleteDeviceAttribute(@PathVariable Long id) {
        log.debug("REST request to delete DeviceAttribute : {}", id);
        deviceAttributeRepository.deleteById(id);
        deviceAttributeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/device-attributes?query=:query} : search for the deviceAttribute corresponding
     * to the query.
     *
     * @param query the query of the deviceAttribute search.
     * @return the result of the search.
     */
    @GetMapping("/_search/device-attributes")
    public List<DeviceAttribute> searchDeviceAttributes(@RequestParam String query) {
        log.debug("REST request to search DeviceAttributes for query {}", query);
        return StreamSupport
            .stream(deviceAttributeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
