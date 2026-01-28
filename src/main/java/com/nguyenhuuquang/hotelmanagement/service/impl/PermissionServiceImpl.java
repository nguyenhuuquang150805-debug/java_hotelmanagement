package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Permission;
import com.nguyenhuuquang.hotelmanagement.repository.PermissionRepository;
import com.nguyenhuuquang.hotelmanagement.service.PermissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission updatePermission(Long id, Permission permission) {
        return permissionRepository.findById(id)
                .map(existing -> {
                    permission.setId(id);
                    return permissionRepository.save(permission);
                })
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> getPermissionsByModule(String module) {
        return permissionRepository.findByModule(module);
    }
}