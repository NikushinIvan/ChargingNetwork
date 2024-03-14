package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.chargeSession.ChargeSession;

public interface ChargeSessionRepository  extends CrudRepository<ChargeSession, Integer> {
}
