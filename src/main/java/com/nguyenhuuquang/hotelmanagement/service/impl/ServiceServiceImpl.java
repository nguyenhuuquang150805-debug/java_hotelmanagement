package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Service;
import com.nguyenhuuquang.hotelmanagement.repository.ServiceRepository;
import com.nguyenhuuquang.hotelmanagement.service.ServiceService;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    @Override
    @Transactional
    public Service updateService(Long id, Service service) {
        return serviceRepository.findById(id)
                .map(existing -> {
                    service.setId(id);
                    return serviceRepository.save(service);
                })
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public Optional<Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    @Override
    public List<Service> getServicesByCategory(String category) {
        return serviceRepository.findByCategory(category);
    }

    @Override
    public List<Service> getAvailableServices() {
        return serviceRepository.findByIsAvailable(true);
    }

    @Override
    public List<Service> searchServicesByName(String name) {
        return serviceRepository.findByServiceNameContaining(name);
    }
}