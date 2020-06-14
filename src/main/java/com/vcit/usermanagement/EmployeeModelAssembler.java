package com.vcit.usermanagement;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * 
 * This simple interface has one method: toModel(). 
 * It is based on converting a non-model object (Employee) into a model-based object (EntityModel<Employee>)
 * @author ramuc
 *
 */

@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

  @Override
  public EntityModel<Employee> toModel(Employee employee) {

    return EntityModel.of(employee,
        linkTo(methodOn(HALEmployeeController.class).findOne(employee.getId())).withSelfRel(),
        linkTo(methodOn(HALEmployeeController.class).all()).withRel("halemployees"));
  }
}