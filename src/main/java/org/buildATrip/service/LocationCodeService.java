package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.dao.LocationCodeRepository;
import org.buildATrip.entity.LocationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationCodeService {

    private final LocationCodeRepository locationCodeRepository;
    private final AmadeusService amadeusService;

    @Autowired
    public LocationCodeService(LocationCodeRepository locationCodeRepository, AmadeusService amadeusService) {
        this.locationCodeRepository = locationCodeRepository;
        this.amadeusService = amadeusService;
    }

    public List<LocationCode> getAllLocationCodes() {
        return locationCodeRepository.findAll();
    }

    public Optional<LocationCode> getLocationCodeById(String codeId) {
        return locationCodeRepository.findById(codeId);
    }

    public LocationCode saveLocationCode(LocationCode locationCode) {
        return locationCodeRepository.save(locationCode);
    }

    public void deleteLocationCode(String codeId) {
        locationCodeRepository.deleteById(codeId);
    }

    public LocationCode findByCityName(String cityName) {
        return locationCodeRepository.findByCityNameIgnoreCase(cityName);
    }

    /**
     * Find a location code by keyword, or create it if it doesn't exist
     * by calling the Amadeus API
     */
    public LocationCode findOrCreateLocationCode(String keyword) throws ResponseException {
        // First try to find it in the database
        Optional<LocationCode> existingLocation = locationCodeRepository.findById(keyword);
        if (existingLocation.isPresent()) {
            return existingLocation.get();
        }

        // If not found by code, try to find by city name
        LocationCode locationByCity = locationCodeRepository.findByCityNameIgnoreCase(keyword);
        if (locationByCity != null) {
            return locationByCity;
        }

        // If not found at all, call Amadeus to get it
        return amadeusService.getCityLocations(keyword);
    }
}