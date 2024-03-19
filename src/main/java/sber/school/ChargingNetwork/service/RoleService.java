package sber.school.ChargingNetwork.service;


import sber.school.ChargingNetwork.model.user.Role;

import java.util.Set;

public interface RoleService {

    Iterable<Role> getAllRoles();
    Role getRoleById(int id);
    Iterable<Role> excludeRepeatRoles(Set<Role> userRoles);

}
