package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.station.Station;

public interface StationRepository extends CrudRepository<Station, Integer> {
}
