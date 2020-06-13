package com.vcit.usermanagement;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(Long id) {
		super("Could not find employee with id "+id);
	}
}
