package sber.school.ChargingNetwork.service;

import org.springframework.http.ResponseEntity;
import sber.school.ChargingNetwork.dto.StationState;
import sber.school.ChargingNetwork.model.station.Station;

/**
 *
 * Интерфейс содержит методы, необходимые для взаимодействия с объектами Station. Представляет слой бизнес-логики
 *
 */
public interface StationService {

    /**
     *
     * Метод добавляет новую зарядную станцию в базу данных
     *
     * @param station Объект зарядной станции, котоный необходимо сохранить
     * @return Объект зарядной станции, сохраненненный в базе данных
     */
    Station saveStation(Station station);

    /**
     *
     * Метод возвращает список всех зарядных станций приложения
     *
     * @return Список всех зарядных станций приложения. Может быть пустым
     */
    Iterable<Station> getAllStations();

    /**
     *
     * Метод возвращает объект станции по ее идентификатору.
     * Если искомой станции нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param id Идентификатор станции
     * @return Объект станции
     */
    Station getStationById(int id);

    /**
     *
     * Метод обновляет данные станции в базе данных
     *
     * @param id Идентификатор станции
     * @param station Объект станции с новыми данными станции
     */
    void updateStation(int id, Station station);

    /**
     *
     * Метод удаляет станцию из базы данных
     *
     * @param id Идентификатор станции
     */
    void deleteStation(int id);

    /**
     *
     * Метод сохраняет текущее состояние зарядной станции.
     *
     * @param id Идентификатор станции
     * @param state Текущее состояние зарядной станции
     * @return Если состояние станции было успешно установлено, возвращает ResponseEntity со статусом 200(OK)
     * Если принято состояние {@link StationState#ERROR ERROR} или {@link StationState#DISCONNECT DISCONNECT},
     * диспетчеру станции отправляется уведомление о неисправности станции
     * Если принято некорректное состояние или указан неизвестный идентификатор станции, метод возвращает
     * ResponseEntity со статусом 400(BAD_REQUEST)
     */
    ResponseEntity<Void> setStationState(int id, StationState state);
}
