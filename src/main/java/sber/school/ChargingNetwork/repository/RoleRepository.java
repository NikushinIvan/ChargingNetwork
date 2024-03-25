package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.user.Role;

/**
 *
 * Интерфейс для взаимодествия с сущностями Role. Расширяет интерфейс CrudRepository
 *
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
