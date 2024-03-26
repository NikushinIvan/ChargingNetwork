package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.station.Address;

import java.util.List;

/**
 *
 * Интерфейс содержит методы, необходимые для взаимодействия с объектами Address. Представляет слой бизнес-логики
 *
 */
public interface AddressService {

    /**
     * Метод возвращает список всех адресов, доступных в приложении
     *
     * @return Список всех адресов приложения. Может быть пустым
     */
    List<Address> getAllAddress();

}
