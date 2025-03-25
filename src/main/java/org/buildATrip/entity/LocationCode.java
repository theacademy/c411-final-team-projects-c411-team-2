package org.buildATrip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationcode")
public class LocationCode {

    @Id
    @Column(name = "code_id", length = 3, nullable = false)
    private String codeId;

    @Column(name = "city_name", length = 45, nullable = false)
    private String cityName;

    // Default constructor
    public LocationCode() {
    }

    // Constructor with all fields
    public LocationCode(String codeId, String cityName) {
        this.codeId = codeId;
        this.cityName = cityName;
    }

    // Getters and Setters
    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
