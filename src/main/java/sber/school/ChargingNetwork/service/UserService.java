package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.user.User;

public interface UserService {

    User createUser(User user);
    Iterable<User> getUsers();
    User getUserById(int id);
    User getUserByUsername(String username);
    void updateUser(int id, User user);
    void deleteUser(int id);

}
