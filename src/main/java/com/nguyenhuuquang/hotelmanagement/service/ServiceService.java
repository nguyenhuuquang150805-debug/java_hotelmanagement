package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Service;

public interface ServiceService {

    Service createService(Service service);

    Service updateService(Long id, Service service);

    void deleteService(Long id);

    Optional<Service> getServiceById(Long id);

    Optional<Service> getServiceByName(String name);

    List<Service> getAllServices();

    List<Service> getActiveServices();

    Service updateServiceStatus(Long id, Boolean isActive);
}