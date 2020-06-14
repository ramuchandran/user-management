package com.vcit.usermanagement;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hypertext Application Language (HAL) : represeting the same resuorce with links to resources.
 * @author ramuc
 *
 */
@RestController
public class HALEmployeeController {

	private final EmployeeRepository repository;
	private final EmployeeModelAssembler assembler;

	public HALEmployeeController(EmployeeRepository repository,EmployeeModelAssembler assembler) {
		this.repository= repository;
		this.assembler = assembler;
	}

	@GetMapping("/halemployees")
	CollectionModel<EntityModel<Employee>> all(){
		List<EntityModel<Employee>> employees = repository.findAll().stream()
				//			      .map(employee ->  assembler.toModel(employee)) //or use method reference
				.map(assembler::toModel)
				.collect(Collectors.toList());
		return CollectionModel.of(employees, linkTo(methodOn(HALEmployeeController.class).all()).withSelfRel());
	}

	@PostMapping("/halemployees")
	ResponseEntity<EntityModel<Employee>> createEmployee(@RequestBody Employee newEmployee) {
		Employee employee = repository.save(newEmployee);
		EntityModel<Employee> model = assembler.toModel(employee);
		ResponseEntity<EntityModel<Employee>> responseEntity = ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
		return responseEntity;
	}

	@GetMapping("/halemployees/{id}")
	EntityModel<Employee> findOne(@PathVariable Long id) {
		Employee employee= repository.findById(id)
				.orElseThrow(()->new EmployeeNotFoundException(id));
		//		EntityModel<Employee> entityModel = EntityModel.of(employee,
		//				linkTo(methodOn(HALEmployeeController.class).findOne(id)).withSelfRel(),
		//				linkTo(methodOn(HALEmployeeController.class).all()).withRel("employees"));
		//		replaced with assembler as below

		EntityModel<Employee> entityModel = assembler.toModel(employee);
		return entityModel;		
	}


	@PutMapping("/halemployees/{id}")
	ResponseEntity updateEmployee(@RequestBody Employee newEmployee,@PathVariable Long id) {

		Employee updatedEmployee= repository.findById(id)
				.map(employee ->{
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(()->{
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});
		
		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		  return ResponseEntity
		      .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) 
		      .body(entityModel);
	}

	@DeleteMapping("/halemployees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
