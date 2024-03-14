package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    List<User> findByRoles_RoleNameContaining(String roleName);

    Optional<User> findByUid(String uid);

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

}
