package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.station.Station;

/**
 *
 * Интерфейс для взаимодествия с сущностями Station. Расширяет интерфейс CrudRepository
 *
 */
public interface StationRepository extends CrudRepository<Station, Integer> {

    /**
     *
     * Метод поиска сущности Station по полю login
     *
     * @param login - Логин искомой зарядной станции
     * @return Объект зарядной стацнции
     */
    Station findByLogin(String login);

}
