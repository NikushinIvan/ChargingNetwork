package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.chargeSession.ChargeSession;

/**
 *
 * Интерфейс для взаимодествия с сущностями ChargeSession. Расширяет интерфейс CrudRepository
 *
 */
public interface ChargeSessionRepository  extends CrudRepository<ChargeSession, Integer> {
}
