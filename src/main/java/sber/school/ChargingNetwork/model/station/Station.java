package sber.school.ChargingNetwork.model.station;

import sber.school.ChargingNetwork.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationId;
    @NotNull
    private String stationName;
    @NotNull
    private String login;
    @NotNull
    private String password;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private String stationState;

    public Station() {
    }

    public Station(int stationId, String stationName, String login, String password) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.login = login;
        this.password = password;
    }

    public Station(int stationId, String stationName, Vendor vendor, Address address) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.vendor = vendor;
        this.address = address;
    }

    public Station(int stationId, User manager, Vendor vendor, Address address, String stationName, String stationState) {
        this.stationId = stationId;
        this.manager = manager;
        this.vendor = vendor;
        this.address = address;
        this.stationName = stationName;
        this.stationState = stationState;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationState() {
        return stationState;
    }

    public void setStationState(String stationState) {
        this.stationState = stationState;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
