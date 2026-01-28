package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Permission;

public interface PermissionService {
    Permission createPermission(Permission permission);

    Permission updatePermission(Long id, Permission permission);

    void deletePermission(Long id);

    Optional<Permission> getPermissionById(Long id);

    Optional<Permission> getPermissionByName(String name);

    List<Permission> getAllPermissions();

    List<Permission> getPermissionsByModule(String module);
}