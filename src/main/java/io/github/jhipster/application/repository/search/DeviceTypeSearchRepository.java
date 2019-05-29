package io.github.jhipster.application.repository.search;

import io.github.jhipster.application.domain.DeviceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DeviceType} entity.
 */
public interface DeviceTypeSearchRepository extends ElasticsearchRepository<DeviceType, Long> {
}
