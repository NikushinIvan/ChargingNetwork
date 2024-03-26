package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.dto.StartSessionResponseDto;
import sber.school.ChargingNetwork.dto.StopSessionResponseDto;
import sber.school.ChargingNetwork.dto.StartSessionRequestDto;
import sber.school.ChargingNetwork.dto.StopSessionRequestDto;

/**
 *
 * Интерфейс содержит методы, необходимые для реализации основного функционала с зарядными сессиями. Представляет слой бизнес-логики
 *
 */
public interface SessionService {

    /**
     *
     * Метод осуществляет валидацию запроса на начало сессии, возвращает объект данных с информацией по обработанному запросу
     *
     * @param request Объект данных запроса станции на начало зарядной сессии, содержит UID пользователя
     * @param stationId Идентификатор станции, на котором будет запущена зарядная сессия
     * @return Объект данных ответа сервера на запрос начала зарядной сессии, содержит статус сессии.
     * Если сессия принята, сожержит идентификатор сессии
     */
    StartSessionResponseDto startSession(StartSessionRequestDto request, int stationId);

    /**
     *
     * Метод осуществляет валидацию запроса на завершение сессии, возвращает объект данных с информацией по обработанному запросу
     *
     * @param request Объект данных запроса станции на завершение зарядной сессии, содержит идентификатор завершаемой сессии.
     *                Может содержать причину завершения сессии (Осталовка пользователем, Аварийная остановка,
     *                Автомобиль заряжен и т.д.)
     * @return Объект данных ответа сервера на запрос завершения зарядной сессии, содержит статус завершения сессии.
     */
    StopSessionResponseDto stopSession(StopSessionRequestDto request);

}
