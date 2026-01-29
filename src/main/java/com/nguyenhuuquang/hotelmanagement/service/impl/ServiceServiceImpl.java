package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Service;
import com.nguyenhuuquang.hotelmanagement.exception.ResourceNotFoundException;
import com.nguyenhuuquang.hotelmanagement.repository.ServiceRepository;
import com.nguyenhuuquang.hotelmanagement.service.ServiceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public Service createService(Service service) {
        log.info("Creating service: {}", service.getName());

        if (serviceRepository.existsByName(service.getName())) {
            throw new IllegalArgumentException("Service with name '" + service.getName() + "' already exists");
        }

        if (service.getIsActive() == null) {
            service.setIsActive(true);
        }

        Service saved = serviceRepository.save(service);
        log.info("Service created with ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Service updateService(Long id, Service service) {
        log.info("Updating service with ID: {}", id);

        Service existing = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));

        if (!existing.getName().equals(service.getName()) &&
                serviceRepository.existsByName(service.getName())) {
            throw new IllegalArgumentException("Service with name '" + service.getName() + "' already exists");
        }

        existing.setName(service.getName());
        existing.setDescription(service.getDescription());
        existing.setPrice(service.getPrice());
        existing.setImageUrl(service.getImageUrl());

        if (service.getIsActive() != null) {
            existing.setIsActive(service.getIsActive());
        }

        Service updated = serviceRepository.save(existing);
        log.info("Service updated successfully");
        return updated;
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        log.info("Deleting service with ID: {}", id);

        if (!serviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Service not found with ID: " + id);
        }

        serviceRepository.deleteById(id);
        log.info("Service deleted successfully");
    }

    @Override
    public Optional<Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public Optional<Service> getServiceByName(String name) {
        return serviceRepository.findByName(name);
    }

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public List<Service> getActiveServices() {
        return serviceRepository.findByIsActive(true);
    }

    @Override
    @Transactional
    public Service updateServiceStatus(Long id, Boolean isActive) {
        log.info("Updating service {} status to {}", id, isActive);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + id));

        service.setIsActive(isActive);
        return serviceRepository.save(service);
    }
}