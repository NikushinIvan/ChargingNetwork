package sber.school.ChargingNetwork.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.dao.UserDao;
import sber.school.ChargingNetwork.model.user.User;
import sber.school.ChargingNetwork.repository.UserRepository;
import sber.school.ChargingNetwork.service.UserService;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDao userDao;

    public UserServiceImpl(UserRepository userRepository, UserDao userDao) {
        this.userRepository = userRepository;
        this.userDao = userDao;
    }


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
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
    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void updateUser(int id, User updatedUser) {
        updatedUser.setUserId(id);
        var user = userRepository.findById(id);
        updatedUser.setRoles(user.get().getRoles());
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
