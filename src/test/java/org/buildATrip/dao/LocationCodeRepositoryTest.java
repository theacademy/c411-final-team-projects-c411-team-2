package org.buildATrip.dao;

import org.buildATrip.entity.LocationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class LocationCodeRepositoryTest {

    @Autowired
    private LocationCodeRepository locationCodeRepository;

    private LocationCode locationCode;

    @BeforeEach
    void setUp() {
        // Clean up any existing data
        locationCodeRepository.deleteAll();

        // Create a sample location code
        locationCode = new LocationCode("JFK", "New York");
        locationCodeRepository.save(locationCode);
    }

    @Test
    void testCreateLocationCode() {
        // Given a new location
        LocationCode newLocation = new LocationCode("LAX", "Los Angeles");

        // When saving the new location
        LocationCode savedLocation = locationCodeRepository.save(newLocation);

        // Then it should have the same properties
        assertThat(savedLocation.getCodeId()).isEqualTo("LAX");
        assertThat(savedLocation.getCityName()).isEqualTo("Los Angeles");
    }

    @Test
    void testFindById() {
        // When retrieving by its codeId
        LocationCode found = locationCodeRepository.findById(locationCode.getCodeId()).orElse(null);

        // Then the retrieved location should match
        assertThat(found).isNotNull();
        assertThat(found.getCityName()).isEqualTo("New York");
    }

    @Test
    void testFindByCityNameIgnoreCase() {
        // When searching by city name (case-insensitive)
        LocationCode found = locationCodeRepository.findByCityNameIgnoreCase("new york");

        // Then the correct LocationCode should be returned
        assertThat(found).isNotNull();
        assertThat(found.getCodeId()).isEqualTo("JFK");
    }

    @Test
    void testUpdateLocationCode() {
        // Given: update the city name
        locationCode.setCityName("NYC");

        // When saving the updated entity
        LocationCode updated = locationCodeRepository.save(locationCode);

        // Then the update should be persisted
        assertThat(updated.getCityName()).isEqualTo("NYC");
    }

    @Test
    void testDeleteLocationCode() {
        // When: delete the entity
        locationCodeRepository.delete(locationCode);

        // Then: the repository should not return the deleted entity
        assertThat(locationCodeRepository.findById(locationCode.getCodeId())).isEmpty();
    }
}
