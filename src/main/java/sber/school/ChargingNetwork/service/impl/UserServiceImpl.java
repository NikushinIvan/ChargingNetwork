package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.repository.UserRepository;
import sber.school.ChargingNetwork.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public User getUserById(int id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }

    @Override
    public void updateUser(int id, User updatedUser) {
        updatedUser.setUserId(id);
        updatedUser.setPassword(userRepository.findById(id).get().getPassword());
        var user = userRepository.findById(id);
        updatedUser.setRoles(user.get().getRoles());
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersGetWithRole(String roleName) {
        return userRepository.findByRoles_RoleNameContaining(roleName);
    }

    @Override
    public User getUserByUid(String uid) {
        var user = userRepository.findByUid(uid);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }

    @Override
    public User getUserByFirstNameAndLastName(String firstName, String lastName) {
        var user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }

    @Override
    public User getUserByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }
}
