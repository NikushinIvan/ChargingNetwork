package sber.school.ChargingNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sber.school.ChargingNetwork.model.station.Address;

/**
 *
 * Интерфейс для взаимодествия с сущностями Address. Расширяет интерфейс JpaRepository
 *
 */
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
