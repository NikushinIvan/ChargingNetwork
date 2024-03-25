package sber.school.ChargingNetwork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sber.school.ChargingNetwork.dto.*;
import sber.school.ChargingNetwork.service.SessionService;
import sber.school.ChargingNetwork.service.StationService;

import java.util.NoSuchElementException;

/**
 *
 * REST контроллер, обрабатывающий запросы от зарядных станций
 *
 */
@RestController
@RequestMapping("charger/api")
public class ChargerController {

    private final StationService stationService;
    private final SessionService sessionService;

    public ChargerController(StationService stationService, SessionService sessionService) {
        this.stationService = stationService;
        this.sessionService = sessionService;
    }

    /**
     *
     * Метод обрабатывает запросы, содержащие информацию о состоянии станции
     *
     * @param stationState - Тело запроса, содержит состояние станции
     * @param id - Содержится в URL, ID станции
     * @return Ответ. Не содержит тела сообщения, при корректном запросе возвращает статус 200(OK),
     * иначе - 400(BAD_REQUEST)
     */
    @PostMapping("/{id}")
    public ResponseEntity<Void> setStationState(@RequestBody StationStateRequestDto stationState,
                                                @PathVariable int id) {
        return stationService.setStationState(id, stationState.getStationState());
    }

    /**
     *
     * Метод обрабатывает запросы на старт зарядной сессии
     *
     * @param request - тело запроса, содержит UID пользователя, инициирующего зарядную сессию
     * @param id - Содержится в URL, ID станции
     * @return Ответ. Статус ответа всегда 200(OK), тело ответа содержит статус подтверждения начала сессии
     * и ID сессии, если UID пользователя зарегестрирован.
     * ACCEPTED - UID принят, начало сессии
     * REJECTED - Ошибка, сессия отклонена
     *
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<StartSessionResponseDto> startSession(@RequestBody StartSessionRequestDto request,
                                                                @PathVariable int id) {
        var response = sessionService.startSession(request, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * Метод, обрабатывающий событие завершения зарядной сессии
     *
     * @param request - тело запроса обязательно содержит ID сессии (параметр chargeSessionId)
     *                и может содержать причину завершения сессии (параметр stopReason)
     * @return Ответ. Статус ответа всегда 200(OK), тело ответа содержит статус подтверждения завершения сессии
     * ACCEPTED - Сессия успешно завершена
     * REJECTED - Завершение сессии отклонено
     */
    @PostMapping("/{id}/stop")
    public ResponseEntity<StopSessionResponseDto> stopSession(@RequestBody StopSessionRequestDto request) {
        var response = sessionService.stopSession(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
