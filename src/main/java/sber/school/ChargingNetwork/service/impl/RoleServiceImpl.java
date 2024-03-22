package sber.school.ChargingNetwork.service.impl;

import org.springframework.stereotype.Service;
import sber.school.ChargingNetwork.model.user.Role;
import sber.school.ChargingNetwork.repository.RoleRepository;
import sber.school.ChargingNetwork.service.RoleService;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(int id) {
        var role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new NoSuchElementException("Роль не найдена");
        }
    }


    /**
     *
     * Метод предназначен для избежания дублирования ролей в UI
     *
     * @param userRoles - Множество ролей пользователя
     * @return - Список ролей, которые не установлены пользователю
     */
    @Override
    public Iterable<Role> excludeRepeatRoles(Set<Role> userRoles) {
        return StreamSupport.stream(roleRepository.findAll().spliterator(), false)
                .filter(role -> !userRoles.contains(role))
                .collect(Collectors.toList());
    }
}
