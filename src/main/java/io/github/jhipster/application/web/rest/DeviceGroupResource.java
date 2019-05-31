package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.DeviceGroup;
import io.github.jhipster.application.repository.DeviceGroupRepository;
import io.github.jhipster.application.repository.search.DeviceGroupSearchRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.DeviceGroup}.
 */
@RestController
@RequestMapping("/api")
public class DeviceGroupResource {

    private final Logger log = LoggerFactory.getLogger(DeviceGroupResource.class);

    private static final String ENTITY_NAME = "jhipsterdeviceserviceDeviceGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceGroupRepository deviceGroupRepository;

    private final DeviceGroupSearchRepository deviceGroupSearchRepository;

    public DeviceGroupResource(DeviceGroupRepository deviceGroupRepository, DeviceGroupSearchRepository deviceGroupSearchRepository) {
        this.deviceGroupRepository = deviceGroupRepository;
        this.deviceGroupSearchRepository = deviceGroupSearchRepository;
    }

    /**
     * {@code POST  /device-groups} : Create a new deviceGroup.
     *
     * @param deviceGroup the deviceGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceGroup, or with status {@code 400 (Bad Request)} if the deviceGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-groups")
    public ResponseEntity<DeviceGroup> createDeviceGroup(@RequestBody DeviceGroup deviceGroup) throws URISyntaxException {
        log.debug("REST request to save DeviceGroup : {}", deviceGroup);
        if (deviceGroup.getId() != null) {
            throw new BadRequestAlertException("A new deviceGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceGroup result = deviceGroupRepository.save(deviceGroup);
        deviceGroupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/device-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-groups} : Updates an existing deviceGroup.
     *
     * @param deviceGroup the deviceGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceGroup,
     * or with status {@code 400 (Bad Request)} if the deviceGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-groups")
    public ResponseEntity<DeviceGroup> updateDeviceGroup(@RequestBody DeviceGroup deviceGroup) throws URISyntaxException {
        log.debug("REST request to update DeviceGroup : {}", deviceGroup);
        if (deviceGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceGroup result = deviceGroupRepository.save(deviceGroup);
        deviceGroupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deviceGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-groups} : get all the deviceGroups.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceGroups in body.
     */
    @GetMapping("/device-groups")
    public List<DeviceGroup> getAllDeviceGroups(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DeviceGroups");
        return deviceGroupRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /device-groups/:id} : get the "id" deviceGroup.
     *
     * @param id the id of the deviceGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-groups/{id}")
    public ResponseEntity<DeviceGroup> getDeviceGroup(@PathVariable Long id) {
        log.debug("REST request to get DeviceGroup : {}", id);
        Optional<DeviceGroup> deviceGroup = deviceGroupRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(deviceGroup);
    }

    /**
     * {@code DELETE  /device-groups/:id} : delete the "id" deviceGroup.
     *
     * @param id the id of the deviceGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-groups/{id}")
    public ResponseEntity<Void> deleteDeviceGroup(@PathVariable Long id) {
        log.debug("REST request to delete DeviceGroup : {}", id);
        deviceGroupRepository.deleteById(id);
        deviceGroupSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/device-groups?query=:query} : search for the deviceGroup corresponding
     * to the query.
     *
     * @param query the query of the deviceGroup search.
     * @return the result of the search.
     */
    @GetMapping("/_search/device-groups")
    public List<DeviceGroup> searchDeviceGroups(@RequestParam String query) {
        log.debug("REST request to search DeviceGroups for query {}", query);
        return StreamSupport
            .stream(deviceGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
