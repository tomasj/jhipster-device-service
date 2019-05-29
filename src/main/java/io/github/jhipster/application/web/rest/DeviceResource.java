package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Device;
import io.github.jhipster.application.repository.DeviceRepository;
import io.github.jhipster.application.repository.search.DeviceSearchRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.Device}.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "jhipsterdeviceserviceDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceRepository deviceRepository;

    private final DeviceSearchRepository deviceSearchRepository;

    public DeviceResource(DeviceRepository deviceRepository, DeviceSearchRepository deviceSearchRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceSearchRepository = deviceSearchRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param device the device to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new device, or with status {@code 400 (Bad Request)} if the device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to save Device : {}", device);
        if (device.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Device result = deviceRepository.save(device);
        deviceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /devices} : Updates an existing device.
     *
     * @param device the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated device,
     * or with status {@code 400 (Bad Request)} if the device is not valid,
     * or with status {@code 500 (Internal Server Error)} if the device couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices")
    public ResponseEntity<Device> updateDevice(@RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to update Device : {}", device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Device result = deviceRepository.save(device);
        deviceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, device.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of devices in body.
     */
    @GetMapping("/devices")
    public List<Device> getAllDevices() {
        log.debug("REST request to get all Devices");
        return deviceRepository.findAll();
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the device to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the device, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Device> device = deviceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the device to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceRepository.deleteById(id);
        deviceSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/devices?query=:query} : search for the device corresponding
     * to the query.
     *
     * @param query the query of the device search.
     * @return the result of the search.
     */
    @GetMapping("/_search/devices")
    public List<Device> searchDevices(@RequestParam String query) {
        log.debug("REST request to search Devices for query {}", query);
        return StreamSupport
            .stream(deviceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
