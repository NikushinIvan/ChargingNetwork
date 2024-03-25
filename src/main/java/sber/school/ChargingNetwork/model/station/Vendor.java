package sber.school.ChargingNetwork.model.station;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * Сущность компании-поставщика зарядной станции
 *
 */
@Entity
@Table(name = "Vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vendorId;
    @NotNull
    private String companyName;
    @NotNull
    private String phoneSupportService;

    public Vendor() {
    }

    public Vendor(String companyName, String phoneSupportService) {
        this.companyName = companyName;
        this.phoneSupportService = phoneSupportService;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneSupportService() {
        return phoneSupportService;
    }

    public void setPhoneSupportService(String phoneSupportService) {
        this.phoneSupportService = phoneSupportService;
    }
}
