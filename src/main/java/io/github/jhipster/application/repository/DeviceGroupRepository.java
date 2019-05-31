package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.DeviceGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the DeviceGroup entity.
 */
@Repository
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {

    @Query(value = "select distinct deviceGroup from DeviceGroup deviceGroup left join fetch deviceGroup.devices",
        countQuery = "select count(distinct deviceGroup) from DeviceGroup deviceGroup")
    Page<DeviceGroup> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct deviceGroup from DeviceGroup deviceGroup left join fetch deviceGroup.devices")
    List<DeviceGroup> findAllWithEagerRelationships();

    @Query("select deviceGroup from DeviceGroup deviceGroup left join fetch deviceGroup.devices where deviceGroup.id =:id")
    Optional<DeviceGroup> findOneWithEagerRelationships(@Param("id") Long id);

}
