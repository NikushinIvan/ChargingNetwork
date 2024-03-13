package sber.school.ChargingNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sber.school.ChargingNetwork.model.station.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findAll();

}
