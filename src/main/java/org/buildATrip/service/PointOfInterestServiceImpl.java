package org.buildATrip.service;

import org.buildATrip.entity.PointOfInterest;

import java.time.LocalDate;
import java.util.List;

public class PointOfInterestServiceImpl implements PointOfInterestService{
    @Override
    public List<PointOfInterest> searchPointsOfInterest(String city, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public PointOfInterest getPointOfInterestById(int id) {
        return null;
    }

    @Override
    public PointOfInterest createPointOfInterest(PointOfInterest pointOfInterest) {
        return null;
    }

    @Override
    public PointOfInterest updatePointOfInterest(int id, PointOfInterest pointOfInterest) {
        return null;
    }

    @Override
    public void deletePointOfInterest(int id) {

    }
}
