package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterdeviceserviceApp;
import io.github.jhipster.application.domain.DeviceGroup;
import io.github.jhipster.application.repository.DeviceGroupRepository;
import io.github.jhipster.application.repository.search.DeviceGroupSearchRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link DeviceGroupResource} REST controller.
 */
@SpringBootTest(classes = JhipsterdeviceserviceApp.class)
public class DeviceGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private DeviceGroupRepository deviceGroupRepository;

    @Mock
    private DeviceGroupRepository deviceGroupRepositoryMock;

    /**
     * This repository is mocked in the io.github.jhipster.application.repository.search test package.
     *
     * @see io.github.jhipster.application.repository.search.DeviceGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeviceGroupSearchRepository mockDeviceGroupSearchRepository;

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

    private MockMvc restDeviceGroupMockMvc;

    private DeviceGroup deviceGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DeviceGroupResource deviceGroupResource = new DeviceGroupResource(deviceGroupRepository, mockDeviceGroupSearchRepository);
        this.restDeviceGroupMockMvc = MockMvcBuilders.standaloneSetup(deviceGroupResource)
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
    public static DeviceGroup createEntity(EntityManager em) {
        DeviceGroup deviceGroup = new DeviceGroup()
            .name(DEFAULT_NAME);
        return deviceGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceGroup createUpdatedEntity(EntityManager em) {
        DeviceGroup deviceGroup = new DeviceGroup()
            .name(UPDATED_NAME);
        return deviceGroup;
    }

    @BeforeEach
    public void initTest() {
        deviceGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceGroup() throws Exception {
        int databaseSizeBeforeCreate = deviceGroupRepository.findAll().size();

        // Create the DeviceGroup
        restDeviceGroupMockMvc.perform(post("/api/device-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceGroup)))
            .andExpect(status().isCreated());

        // Validate the DeviceGroup in the database
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAll();
        assertThat(deviceGroupList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceGroup testDeviceGroup = deviceGroupList.get(deviceGroupList.size() - 1);
        assertThat(testDeviceGroup.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the DeviceGroup in Elasticsearch
        verify(mockDeviceGroupSearchRepository, times(1)).save(testDeviceGroup);
    }

    @Test
    @Transactional
    public void createDeviceGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceGroupRepository.findAll().size();

        // Create the DeviceGroup with an existing ID
        deviceGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceGroupMockMvc.perform(post("/api/device-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceGroup)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceGroup in the database
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAll();
        assertThat(deviceGroupList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeviceGroup in Elasticsearch
        verify(mockDeviceGroupSearchRepository, times(0)).save(deviceGroup);
    }


    @Test
    @Transactional
    public void getAllDeviceGroups() throws Exception {
        // Initialize the database
        deviceGroupRepository.saveAndFlush(deviceGroup);

        // Get all the deviceGroupList
        restDeviceGroupMockMvc.perform(get("/api/device-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDeviceGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        DeviceGroupResource deviceGroupResource = new DeviceGroupResource(deviceGroupRepositoryMock, mockDeviceGroupSearchRepository);
        when(deviceGroupRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDeviceGroupMockMvc = MockMvcBuilders.standaloneSetup(deviceGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDeviceGroupMockMvc.perform(get("/api/device-groups?eagerload=true"))
        .andExpect(status().isOk());

        verify(deviceGroupRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDeviceGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DeviceGroupResource deviceGroupResource = new DeviceGroupResource(deviceGroupRepositoryMock, mockDeviceGroupSearchRepository);
            when(deviceGroupRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDeviceGroupMockMvc = MockMvcBuilders.standaloneSetup(deviceGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDeviceGroupMockMvc.perform(get("/api/device-groups?eagerload=true"))
        .andExpect(status().isOk());

            verify(deviceGroupRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDeviceGroup() throws Exception {
        // Initialize the database
        deviceGroupRepository.saveAndFlush(deviceGroup);

        // Get the deviceGroup
        restDeviceGroupMockMvc.perform(get("/api/device-groups/{id}", deviceGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(deviceGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDeviceGroup() throws Exception {
        // Get the deviceGroup
        restDeviceGroupMockMvc.perform(get("/api/device-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceGroup() throws Exception {
        // Initialize the database
        deviceGroupRepository.saveAndFlush(deviceGroup);

        int databaseSizeBeforeUpdate = deviceGroupRepository.findAll().size();

        // Update the deviceGroup
        DeviceGroup updatedDeviceGroup = deviceGroupRepository.findById(deviceGroup.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceGroup are not directly saved in db
        em.detach(updatedDeviceGroup);
        updatedDeviceGroup
            .name(UPDATED_NAME);

        restDeviceGroupMockMvc.perform(put("/api/device-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceGroup)))
            .andExpect(status().isOk());

        // Validate the DeviceGroup in the database
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAll();
        assertThat(deviceGroupList).hasSize(databaseSizeBeforeUpdate);
        DeviceGroup testDeviceGroup = deviceGroupList.get(deviceGroupList.size() - 1);
        assertThat(testDeviceGroup.getName()).isEqualTo(UPDATED_NAME);

        // Validate the DeviceGroup in Elasticsearch
        verify(mockDeviceGroupSearchRepository, times(1)).save(testDeviceGroup);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceGroup() throws Exception {
        int databaseSizeBeforeUpdate = deviceGroupRepository.findAll().size();

        // Create the DeviceGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceGroupMockMvc.perform(put("/api/device-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(deviceGroup)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceGroup in the database
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAll();
        assertThat(deviceGroupList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeviceGroup in Elasticsearch
        verify(mockDeviceGroupSearchRepository, times(0)).save(deviceGroup);
    }

    @Test
    @Transactional
    public void deleteDeviceGroup() throws Exception {
        // Initialize the database
        deviceGroupRepository.saveAndFlush(deviceGroup);

        int databaseSizeBeforeDelete = deviceGroupRepository.findAll().size();

        // Delete the deviceGroup
        restDeviceGroupMockMvc.perform(delete("/api/device-groups/{id}", deviceGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<DeviceGroup> deviceGroupList = deviceGroupRepository.findAll();
        assertThat(deviceGroupList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeviceGroup in Elasticsearch
        verify(mockDeviceGroupSearchRepository, times(1)).deleteById(deviceGroup.getId());
    }

    @Test
    @Transactional
    public void searchDeviceGroup() throws Exception {
        // Initialize the database
        deviceGroupRepository.saveAndFlush(deviceGroup);
        when(mockDeviceGroupSearchRepository.search(queryStringQuery("id:" + deviceGroup.getId())))
            .thenReturn(Collections.singletonList(deviceGroup));
        // Search the deviceGroup
        restDeviceGroupMockMvc.perform(get("/api/_search/device-groups?query=id:" + deviceGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceGroup.class);
        DeviceGroup deviceGroup1 = new DeviceGroup();
        deviceGroup1.setId(1L);
        DeviceGroup deviceGroup2 = new DeviceGroup();
        deviceGroup2.setId(deviceGroup1.getId());
        assertThat(deviceGroup1).isEqualTo(deviceGroup2);
        deviceGroup2.setId(2L);
        assertThat(deviceGroup1).isNotEqualTo(deviceGroup2);
        deviceGroup1.setId(null);
        assertThat(deviceGroup1).isNotEqualTo(deviceGroup2);
    }
}
