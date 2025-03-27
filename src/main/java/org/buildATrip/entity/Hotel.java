package org.buildATrip.entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @Column(name = "hotel_id", nullable = false)
    private String hotel_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "checkin_date", nullable = false)
    private LocalDate checkinDate;

    @Column(name = "checkout_date", nullable = false)
    private LocalDate checkoutDate;

    @Column(name = "longitude", nullable = false)
    private BigDecimal longitude;

    @Column(name = "latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "board_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    public Hotel(){}

    public Hotel(String name, BigDecimal price, LocalDate checkinDate, LocalDate checkoutDate, BigDecimal longitude, BigDecimal latitude, String address, BoardType boardType) {
        this.name = name;
        this.price = price;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.boardType = boardType;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public String getAddress() {
        return address;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setHotel_id(String hotel_id) {
        this.hotel_id = hotel_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCheckinDate(LocalDate checkinDate) {
        this.checkinDate = checkinDate;
    }

    public void setCheckoutDate(LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBoardType(BoardType boardType) {
        this.boardType = boardType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(hotel_id, hotel.hotel_id) && Objects.equals(name, hotel.name) && Objects.equals(price, hotel.price) && Objects.equals(checkinDate, hotel.checkinDate) && Objects.equals(checkoutDate, hotel.checkoutDate) && Objects.equals(longitude, hotel.longitude) && Objects.equals(latitude, hotel.latitude) && Objects.equals(address, hotel.address) && boardType == hotel.boardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotel_id, name, price, checkinDate, checkoutDate, longitude, latitude, address, boardType);
    }
}
