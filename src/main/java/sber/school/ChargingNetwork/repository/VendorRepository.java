package sber.school.ChargingNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sber.school.ChargingNetwork.model.station.Vendor;

import java.util.List;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {

    List<Vendor> findAll();

}
