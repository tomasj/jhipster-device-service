package io.github.jhipster.application.repository.search;

import io.github.jhipster.application.domain.DeviceGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DeviceGroup} entity.
 */
public interface DeviceGroupSearchRepository extends ElasticsearchRepository<DeviceGroup, Long> {
}
