package sber.school.ChargingNetwork.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sber.school.ChargingNetwork.model.user.User;

public interface UserDao extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
