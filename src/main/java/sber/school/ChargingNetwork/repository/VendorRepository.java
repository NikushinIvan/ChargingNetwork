package sber.school.ChargingNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sber.school.ChargingNetwork.model.station.Vendor;

/**
 *
 * Интерфейс для взаимодествия с сущностями Vendor. Расширяет интерфейс JpaRepository
 *
 */
public interface VendorRepository extends JpaRepository<Vendor, Integer> {

}
