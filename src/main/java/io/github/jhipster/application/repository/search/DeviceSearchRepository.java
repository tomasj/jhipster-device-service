package io.github.jhipster.application.repository.search;

import io.github.jhipster.application.domain.Device;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Device} entity.
 */
public interface DeviceSearchRepository extends ElasticsearchRepository<Device, Long> {
}
