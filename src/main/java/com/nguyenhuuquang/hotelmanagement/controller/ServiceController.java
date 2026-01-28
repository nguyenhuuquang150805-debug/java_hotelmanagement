package com.nguyenhuuquang.hotelmanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.Service;
import com.nguyenhuuquang.hotelmanagement.service.ServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
        private final ServiceService serviceService;

        @PostMapping
        public ResponseEntity<EntityModel<Service>> createService(@RequestBody Service service) {
                Service createdService = serviceService.createService(service);
                EntityModel<Service> entityModel = toModel(createdService);
                return ResponseEntity
                                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                                .body(entityModel);
        }

        @PutMapping("/{id}")
        public ResponseEntity<EntityModel<Service>> updateService(@PathVariable Long id, @RequestBody Service service) {
                Service updatedService = serviceService.updateService(id, service);
                return ResponseEntity.ok(toModel(updatedService));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteService(@PathVariable Long id) {
                serviceService.deleteService(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<EntityModel<Service>> getServiceById(@PathVariable Long id) {
                return serviceService.getServiceById(id)
                                .map(service -> ResponseEntity.ok(toModel(service)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<CollectionModel<EntityModel<Service>>> getAllServices() {
                List<EntityModel<Service>> services = serviceService.getAllServices().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Service>> collectionModel = CollectionModel.of(services,
                                linkTo(methodOn(ServiceController.class).getAllServices()).withSelfRel(),
                                linkTo(methodOn(ServiceController.class).getAvailableServices()).withRel("available"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/category/{category}")
        public ResponseEntity<CollectionModel<EntityModel<Service>>> getServicesByCategory(
                        @PathVariable String category) {
                List<EntityModel<Service>> services = serviceService.getServicesByCategory(category).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Service>> collectionModel = CollectionModel.of(services,
                                linkTo(methodOn(ServiceController.class).getServicesByCategory(category)).withSelfRel(),
                                linkTo(methodOn(ServiceController.class).getAllServices()).withRel("all-services"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/available")
        public ResponseEntity<CollectionModel<EntityModel<Service>>> getAvailableServices() {
                List<EntityModel<Service>> services = serviceService.getAvailableServices().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Service>> collectionModel = CollectionModel.of(services,
                                linkTo(methodOn(ServiceController.class).getAvailableServices()).withSelfRel(),
                                linkTo(methodOn(ServiceController.class).getAllServices()).withRel("all-services"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/search")
        public ResponseEntity<CollectionModel<EntityModel<Service>>> searchServices(@RequestParam String name) {
                List<EntityModel<Service>> services = serviceService.searchServicesByName(name).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Service>> collectionModel = CollectionModel.of(services,
                                linkTo(methodOn(ServiceController.class).searchServices(name)).withSelfRel(),
                                linkTo(methodOn(ServiceController.class).getAllServices()).withRel("all-services"));

                return ResponseEntity.ok(collectionModel);
        }

        private EntityModel<Service> toModel(Service service) {
                EntityModel<Service> entityModel = EntityModel.of(service);

                entityModel.add(linkTo(methodOn(ServiceController.class).getServiceById(service.getId()))
                                .withSelfRel());
                entityModel.add(linkTo(methodOn(ServiceController.class).getAllServices()).withRel("services"));
                entityModel
                                .add(linkTo(methodOn(ServiceController.class).updateService(service.getId(), null))
                                                .withRel("update"));
                entityModel.add(linkTo(methodOn(ServiceController.class).deleteService(service.getId()))
                                .withRel("delete"));

                if (service.getCategory() != null) {
                        entityModel.add(linkTo(methodOn(ServiceController.class)
                                        .getServicesByCategory(service.getCategory())).withRel("category"));
                }

                return entityModel;
        }
}