package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.user.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {

    User saveUser(User user);
    List<User> getUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    void updateUser(int id, User user);
    void deleteUser(int id);
    List<User>getUsersGetWithRole(String roleName);
    User getByUid(String uid) throws NoSuchElementException;

}
