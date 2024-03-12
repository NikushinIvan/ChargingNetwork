package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.user.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
