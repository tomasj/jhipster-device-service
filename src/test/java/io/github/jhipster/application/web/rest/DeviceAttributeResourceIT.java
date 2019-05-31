package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterdeviceserviceApp;
import io.github.jhipster.application.domain.DeviceAttribute;
import io.github.jhipster.application.repository.DeviceAttributeRepository;
import io.github.jhipster.application.repository.search.DeviceAttributeSearchRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.jhipster.application.domain.enumeration.DeviceAttributeType;
/**
 * Integration tests for the {@Link DeviceAttributeResource} REST controller.
 */
@SpringBootTest(classes = JhipsterdeviceserviceApp.class)
public class DeviceAttributeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DeviceAttributeType DEFAULT_TYPE = DeviceAttributeType.TEXT;
    private static final DeviceAttributeType UPDATED_TYPE = DeviceAttributeType.NUMBER;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private DeviceAttributeRepository deviceAttributeRepository;

    /**
     * This repository is mocked in the io.github.jhipster.application.repository.search test package.
     *
     * @see io.github.jhipster.application.repository.search.DeviceAttributeSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeviceAttributeSearchRepository mockDeviceAttributeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDeviceAttributeMockMvc;

    private DeviceAttribute deviceAttribute;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceAttributeResource deviceAttributeResource = new DeviceAttributeResource(deviceAttributeRepository, mockDeviceAttributeSearchRepository);
        this.restDeviceAttributeMockMvc = MockMvcBuilders.standaloneSetup(deviceAttributeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceAttribute createEntity(EntityManager em) {
        DeviceAttribute deviceAttribute = new DeviceAttribute()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        return deviceAttribute;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceAttribute createUpdatedEntity(EntityManager em) {
        DeviceAttribute deviceAttribute = new DeviceAttribute()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        return deviceAttribute;
    }

    @BeforeEach
    public void initTest() {
        deviceAttribute = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceAttribute() throws Exception {
        int databaseSizeBeforeCreate = deviceAttributeRepository.findAll().size();

        // Create the DeviceAttribute
        restDeviceAttributeMockMvc.perform(post("/api/device-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceAttribute)))
            .andExpect(status().isCreated());

        // Validate the DeviceAttribute in the database
        List<DeviceAttribute> deviceAttributeList = deviceAttributeRepository.findAll();
        assertThat(deviceAttributeList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceAttribute testDeviceAttribute = deviceAttributeList.get(deviceAttributeList.size() - 1);
        assertThat(testDeviceAttribute.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDeviceAttribute.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDeviceAttribute.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the DeviceAttribute in Elasticsearch
        verify(mockDeviceAttributeSearchRepository, times(1)).save(testDeviceAttribute);
    }

    @Test
    @Transactional
    public void createDeviceAttributeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceAttributeRepository.findAll().size();

        // Create the DeviceAttribute with an existing ID
        deviceAttribute.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceAttributeMockMvc.perform(post("/api/device-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceAttribute)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceAttribute in the database
        List<DeviceAttribute> deviceAttributeList = deviceAttributeRepository.findAll();
        assertThat(deviceAttributeList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeviceAttribute in Elasticsearch
        verify(mockDeviceAttributeSearchRepository, times(0)).save(deviceAttribute);
    }


    @Test
    @Transactional
    public void getAllDeviceAttributes() throws Exception {
        // Initialize the database
        deviceAttributeRepository.saveAndFlush(deviceAttribute);

        // Get all the deviceAttributeList
        restDeviceAttributeMockMvc.perform(get("/api/device-attributes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getDeviceAttribute() throws Exception {
        // Initialize the database
        deviceAttributeRepository.saveAndFlush(deviceAttribute);

        // Get the deviceAttribute
        restDeviceAttributeMockMvc.perform(get("/api/device-attributes/{id}", deviceAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deviceAttribute.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeviceAttribute() throws Exception {
        // Get the deviceAttribute
        restDeviceAttributeMockMvc.perform(get("/api/device-attributes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceAttribute() throws Exception {
        // Initialize the database
        deviceAttributeRepository.saveAndFlush(deviceAttribute);

        int databaseSizeBeforeUpdate = deviceAttributeRepository.findAll().size();

        // Update the deviceAttribute
        DeviceAttribute updatedDeviceAttribute = deviceAttributeRepository.findById(deviceAttribute.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceAttribute are not directly saved in db
        em.detach(updatedDeviceAttribute);
        updatedDeviceAttribute
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);

        restDeviceAttributeMockMvc.perform(put("/api/device-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceAttribute)))
            .andExpect(status().isOk());

        // Validate the DeviceAttribute in the database
        List<DeviceAttribute> deviceAttributeList = deviceAttributeRepository.findAll();
        assertThat(deviceAttributeList).hasSize(databaseSizeBeforeUpdate);
        DeviceAttribute testDeviceAttribute = deviceAttributeList.get(deviceAttributeList.size() - 1);
        assertThat(testDeviceAttribute.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDeviceAttribute.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDeviceAttribute.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the DeviceAttribute in Elasticsearch
        verify(mockDeviceAttributeSearchRepository, times(1)).save(testDeviceAttribute);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceAttribute() throws Exception {
        int databaseSizeBeforeUpdate = deviceAttributeRepository.findAll().size();

        // Create the DeviceAttribute

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceAttributeMockMvc.perform(put("/api/device-attributes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceAttribute)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceAttribute in the database
        List<DeviceAttribute> deviceAttributeList = deviceAttributeRepository.findAll();
        assertThat(deviceAttributeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeviceAttribute in Elasticsearch
        verify(mockDeviceAttributeSearchRepository, times(0)).save(deviceAttribute);
    }

    @Test
    @Transactional
    public void deleteDeviceAttribute() throws Exception {
        // Initialize the database
        deviceAttributeRepository.saveAndFlush(deviceAttribute);

        int databaseSizeBeforeDelete = deviceAttributeRepository.findAll().size();

        // Delete the deviceAttribute
        restDeviceAttributeMockMvc.perform(delete("/api/device-attributes/{id}", deviceAttribute.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<DeviceAttribute> deviceAttributeList = deviceAttributeRepository.findAll();
        assertThat(deviceAttributeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeviceAttribute in Elasticsearch
        verify(mockDeviceAttributeSearchRepository, times(1)).deleteById(deviceAttribute.getId());
    }

    @Test
    @Transactional
    public void searchDeviceAttribute() throws Exception {
        // Initialize the database
        deviceAttributeRepository.saveAndFlush(deviceAttribute);
        when(mockDeviceAttributeSearchRepository.search(queryStringQuery("id:" + deviceAttribute.getId())))
            .thenReturn(Collections.singletonList(deviceAttribute));
        // Search the deviceAttribute
        restDeviceAttributeMockMvc.perform(get("/api/_search/device-attributes?query=id:" + deviceAttribute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceAttribute.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceAttribute.class);
        DeviceAttribute deviceAttribute1 = new DeviceAttribute();
        deviceAttribute1.setId(1L);
        DeviceAttribute deviceAttribute2 = new DeviceAttribute();
        deviceAttribute2.setId(deviceAttribute1.getId());
        assertThat(deviceAttribute1).isEqualTo(deviceAttribute2);
        deviceAttribute2.setId(2L);
        assertThat(deviceAttribute1).isNotEqualTo(deviceAttribute2);
        deviceAttribute1.setId(null);
        assertThat(deviceAttribute1).isNotEqualTo(deviceAttribute2);
    }
}
