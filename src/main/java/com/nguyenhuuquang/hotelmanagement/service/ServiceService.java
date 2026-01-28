package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Service;

public interface ServiceService {
    Service createService(Service service);

    Service updateService(Long id, Service service);

    void deleteService(Long id);

    Optional<Service> getServiceById(Long id);

    List<Service> getAllServices();

    List<Service> getServicesByCategory(String category);

    List<Service> getAvailableServices();

    List<Service> searchServicesByName(String name);
}