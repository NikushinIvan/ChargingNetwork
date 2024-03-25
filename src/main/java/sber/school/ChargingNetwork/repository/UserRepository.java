package sber.school.ChargingNetwork.repository;

import org.springframework.data.repository.CrudRepository;
import sber.school.ChargingNetwork.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 *
 * Интерфейс для взаимодествия с сущностями User. Расширяет интерфейс CrudRepository
 *
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     *
     * Метод поиска сущности User по полю username
     *
     * @param username - Имя пользователя искомого пользователя
     * @return Объект пользователя
     */
    User findByUsername(String username);

    /**
     *
     * Метод поиска сущностей User с требуемой ролью
     *
     * @param roleName - Название требуемой роли
     * @return Список пользователей, содержащих необходимую роль
     */
    List<User> findByRoles_RoleNameContaining(String roleName);

    /**
     *
     * Метод поиска сущности User по полю uid
     *
     * @param uid - Уникальный идентификатор RFID карты пользователя
     * @return Если пользователь существует, возвращает Optional.of(user), иначе Optional.empty()
     */
    Optional<User> findByUid(String uid);

    /**
     *
     * Метод поиска сущности User по имени и фамилии
     *
     * @param firstName - Имя искомого пользователя
     * @param lastName - Фамилия искомого пользователя
     * @return Если пользователь существует, возвращает Optional.of(user), иначе Optional.empty()
     */
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

}
