package sber.school.ChargingNetwork.model.station;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * Сущность адреса зарядной станции
 *
 */
@Entity
@Table(name = "Addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;
    @NotNull
    @Size(min = 1)
    private String city;
    @NotNull
    @Size(min = 1)
    private String street;
    @NotNull
    @Size(min = 1)
    private int houseNumber;

    public Address() {
    }

    public Address(int addressId, String city, String street, int houseNumber) {
        this.addressId = addressId;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    @Override
    public String toString() {
        return "г. " + city +
                ", ул. " + street +
                ", д. " + houseNumber;
    }
}
