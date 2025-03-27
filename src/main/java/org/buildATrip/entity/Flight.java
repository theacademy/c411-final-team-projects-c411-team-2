package org.buildATrip.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "price", precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private LocalTime duration;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "is_nonstop", nullable = false)
    private Boolean isNonstop;

    @Column(name = "next_flight_id")
    private Integer nextFlightId;

    @ManyToOne
    @JoinColumn(name = "origin_code", nullable = false)
    private LocationCode originCode;

    @ManyToOne
    @JoinColumn(name = "dest_code", nullable = false)
    private LocationCode destinationCode;

    @ManyToMany(mappedBy = "flightsList", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Itinerary> itineraryList;

    // Default constructor
    public Flight() {
    }

    // Constructor with all fields
    public Flight(Integer flightId, BigDecimal price, LocalTime duration, LocalDate date,
                  LocalTime departureTime, Boolean isNonstop, Integer nextFlightId,
                  LocationCode originCode, LocationCode destinationCode) {
        this.flightId = flightId;
        this.price = price;
        this.duration = duration;
        this.date = date;
        this.departureTime = departureTime;
        this.isNonstop = isNonstop;
        this.nextFlightId = nextFlightId;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
    }

    // Getters and Setters
    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Boolean getIsNonstop() {
        return isNonstop;
    }

    public void setIsNonstop(Boolean isNonstop) {
        this.isNonstop = isNonstop;
    }

    public Integer getNextFlightId() {
        return nextFlightId;
    }

    public void setNextFlightId(Integer nextFlightId) {
        this.nextFlightId = nextFlightId;
    }

    public LocationCode getOriginCode() {
        return originCode;
    }

    public void setOriginCode(LocationCode originCode) {
        this.originCode = originCode;
    }

    public LocationCode getDestinationCode() {
        return destinationCode;
    }

    public void setDestinationCode(LocationCode destinationCode) {
        this.destinationCode = destinationCode;
    }

    public List<Itinerary> getItineraryList() {
        return itineraryList;
    }

    public void setItineraryList(List<Itinerary> itineraryList) {
        this.itineraryList = itineraryList;
    }

    // Helper method to calculate arrival time
    public LocalTime getArrivalTime() {
        if (departureTime != null && duration != null) {
            return departureTime.plusHours(duration.getHour())
                    .plusMinutes(duration.getMinute());
        }
        return null;
    }


}
