package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.user.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {

    User saveUser(User user);
    List<User> getUsers();
    User getUserById(int id);
    void updateUser(int id, User user);
    void deleteUser(int id);
    List<User>getUsersGetWithRole(String roleName);
    User getUserByUid(String uid) throws NoSuchElementException;
    User getUserByFirstNameAndLastName(String firstName, String lastName);

}
