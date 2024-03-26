package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.station.Vendor;

import java.util.List;

/**
 *
 * Интерфейс содержит методы, необходимые для взаимодействия с объектами User. Представляет слой бизнес-логики
 *
 */
public interface VendorService {

    /**
     * Метод возвращает список всех поставщиков, доступных в приложении
     *
     * @return Список всех поставщиков приложения. Может быть пустым
     */
    List<Vendor> getAllVendors();

}
