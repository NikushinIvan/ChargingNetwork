package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.user.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}
