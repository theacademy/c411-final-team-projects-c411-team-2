package org.buildATrip.repository;

import org.buildATrip.entity.LocationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCodeRepository extends JpaRepository<LocationCode, String> {

}
