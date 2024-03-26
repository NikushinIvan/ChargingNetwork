package sber.school.ChargingNetwork.service;

import sber.school.ChargingNetwork.model.user.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * Интерфейс содержит методы, необходимые для взаимодействия с объектами User. Представляет слой бизнес-логики
 *
 */
public interface UserService {

    /**
     *
     * Метод добавляет нового пользователя в базу данных
     *
     * @param user Объект пользователя, котоный необходимо сохранить
     * @return Объект пользователя, сохраненненный в базе данных
     */
    User saveUser(User user);

    /**
     *
     * Метод возвращает список всех пользователей приложения
     *
     * @return Список всех доступных пользователей приложения. Может быть пустым
     */
    List<User> getUsers();

    /**
     *
     * Метод возвращает объект пользователя по его идентификатору.
     * Если искомого пользователя нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param id Идентификатор пользователя
     * @return Объект пользователя
     */
    User getUserById(int id);

    /**
     *
     * Метод обновляет данные пользователя в базе данных
     *
     * @param id Идентификатор пользователя
     * @param user Объект пользователя с новыми данными станции
     */
    void updateUser(int id, User user);

    /**
     *
     * Метод удаляет пользователя из базы данных
     *
     * @param id Идентификатор пользователя
     */
    void deleteUser(int id);

    /**
     *
     * Метод возвращает список пользователей, которые обладают необходимой ролью
     *
     * @param roleName Название роли, по которой производится поиск пользователей
     * @return Список пользователей, обладающих необходимой ролью. Может быть пустым
     */
    List<User>getUsersGetWithRole(String roleName);

    /**
     *
     * Метод возвращает объект пользователя по идентификатору его RFID карты.
     * Если искомого пользователя нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param uid Идентификатор RFID карты пользователя
     * @return Объект пользователя
     */
    User getUserByUid(String uid);

    /**
     *
     * Метод возвращает объект пользователя по его имени и фамилии.
     * Если искомого пользователя нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param firstName Имя пользователя
     * @param lastName Фамилия пользователя
     * @return Объект пользователя
     */
    User getUserByFirstNameAndLastName(String firstName, String lastName);

    /**
     *
     * Метод возвращает объект пользователя по его логину.
     * Если искомого пользователя нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param username Логин пользователя
     * @return Объект пользователя
     */
    User getUserByUsername(String username);
}
