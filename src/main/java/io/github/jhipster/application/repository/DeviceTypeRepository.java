package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.DeviceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {

}
