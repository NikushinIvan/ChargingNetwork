package sber.school.ChargingNetwork.service;


import sber.school.ChargingNetwork.model.user.Role;

import java.util.Set;

/**
 *
 * Интерфейс содержит методы, необходимые для взаимодействия с объектами Role. Представляет слой бизнес-логики
 *
 */
public interface RoleService {

    /**
     *
     * Метод возвращает список всех ролей приложения
     *
     * @return Список всех ролей приложения. Может быть пустым
     */
    Iterable<Role> getAllRoles();

    /**
     * Метод возвращает объект роли по его идентификатору.
     * Если искомой роли нет в базе данных, метод выбрасывает исключение {@link java.util.NoSuchElementException NoSuchElementException}
     *
     * @param id Идентификатор роли
     * @return Объект роли
     */
    Role getRoleById(int id);

    /**
     *
     * Метод возвращает список всех доступных ролей, исключая роли, указанные в параметре
     *
     * @param userRoles Список ролей, которые нужно исключить из результирующего списка
     * @return Список всех доступных ролей, исключая роли, указанные в параметре. Может быть пустым
     */
    Iterable<Role> excludeRepeatRoles(Set<Role> userRoles);

}
