package org.buildATrip.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "itinerary")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itinerary_id", nullable = false)
    private Integer id;

    @Column(name = "num_adults", nullable = false)
    private Integer numAdults;

    @Column(name = "price_range_flight")
    private BigDecimal priceRangeFlight;

    @Column(name = "price_range_hotel")
    private BigDecimal priceRangeHotel;

    @Column(name = "price_range_activity")
    private BigDecimal priceRangeActivity;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToMany
    @JoinTable(
            name = "hotel_itinerary",
            joinColumns = {@JoinColumn(name = "itinerary_id")},
            inverseJoinColumns = {@JoinColumn(name = "hotel_id")}
    )
    private List<Hotel> hotelsList;

    @ManyToMany
    @JoinTable(
            name = "activity_itinerary",
            joinColumns = {@JoinColumn(name = "itinerary_id")},
            inverseJoinColumns = {@JoinColumn(name = "activity_id")}
    )
    private List<Activity> activitiesList;

    @ManyToMany
    @JoinTable(
            name = "itinerary_flight",
            joinColumns = {@JoinColumn(name = "itinerary_id")},
            inverseJoinColumns = {@JoinColumn(name = "flight_id")}
    )
    private List<Flight> flightsList;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Integer getId() {
        return id;
    }

    public Integer getNumAdults() {
        return numAdults;
    }

    public BigDecimal getPriceRangeFlight() {
        return priceRangeFlight;
    }

    public BigDecimal getPriceRangeHotel() {
        return priceRangeHotel;
    }

    public BigDecimal getPriceRangeActivity() {
        return priceRangeActivity;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<Hotel> getHotelsList() {
        return hotelsList;
    }

    public List<Activity> getActivitiesList() {
        return activitiesList;
    }

    public List<Flight> getFlightsList() {
        return flightsList;
    }

    public User getUser() {
        return user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumAdults(Integer numAdults) {
        this.numAdults = numAdults;
    }

    public void setPriceRangeFlight(BigDecimal priceRangeFlight) {
        this.priceRangeFlight = priceRangeFlight;
    }

    public void setPriceRangeHotel(BigDecimal priceRangeHotel) {
        this.priceRangeHotel = priceRangeHotel;
    }

    public void setPriceRangeActivity(BigDecimal priceRangeActivity) {
        this.priceRangeActivity = priceRangeActivity;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setHotelsList(List<Hotel> hotelsList) {
        this.hotelsList = hotelsList;
    }

    public void setActivitiesList(List<Activity> activitiesList) {
        this.activitiesList = activitiesList;
    }

    public void setFlightsList(List<Flight> flightsList) {
        this.flightsList = flightsList;
    }

        public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Itinerary itinerary = (Itinerary) o;
        return isConfirmed == itinerary.isConfirmed && Objects.equals(id, itinerary.id) && Objects.equals(numAdults, itinerary.numAdults) && Objects.equals(priceRangeFlight, itinerary.priceRangeFlight) && Objects.equals(priceRangeHotel, itinerary.priceRangeHotel) && Objects.equals(priceRangeActivity, itinerary.priceRangeActivity) && Objects.equals(totalPrice, itinerary.totalPrice) && Objects.equals(startDate, itinerary.startDate) && Objects.equals(endDate, itinerary.endDate) && Objects.equals(user, itinerary.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numAdults, priceRangeFlight, priceRangeHotel, priceRangeActivity, isConfirmed, totalPrice, startDate, endDate, user);
    }
}
