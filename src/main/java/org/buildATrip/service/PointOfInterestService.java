package org.buildATrip.service;

import org.buildATrip.entity.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface PointOfInterestService {
    public List<PointOfInterest> searchPointsOfInterest(
            String city,
            LocalDate startDate,
            LocalDate endDate);

    public PointOfInterest getPointOfInterestById(int id);

    public PointOfInterest createPointOfInterest(PointOfInterest pointOfInterest);

    public PointOfInterest updatePointOfInterest(int id, PointOfInterest pointOfInterest);

    public void deletePointOfInterest(int id);
}
