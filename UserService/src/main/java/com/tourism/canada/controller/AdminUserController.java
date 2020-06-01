package com.tourism.canada.controller;

import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.tourism.canada.entities.Role;
import com.tourism.canada.entities.User;
import com.tourism.canada.service.CustomerManagementService;

@RestController
@RequestMapping(value="/api/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminUserController {
	
	@Autowired
	private CustomerManagementService customerManagementService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@RequestMapping(value="/{customerId}")
	public Optional<User> getCustomerById(@PathVariable("customerId")Integer customerId) {
		return customerManagementService.getCustomerById(customerId);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping(value="/allcustomer")
	public Iterable<User> getAllBookedTickets(){
		return customerManagementService.getAllCustomers();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value="/{customerId}")
	public void delete(@PathVariable("customerId") Integer customerId) {
		customerManagementService.deleteCustomer(customerId);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping(value="/{customerId}")
	public User updateCustomer(@RequestBody User customer) {
		return customerManagementService.updateCustomer(customer);
	}
	
	@PostMapping(value = "/user/register" , consumes = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<?> addUser(@Valid @RequestBody User customer)
	{
		//System.out.println(customer);
		customer.setCustomerType(new Role(1, "ADMIN"));
		User user = customerManagementService.addUser(customer);
		
		URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/consumer/users/profile").build().toUri();
		return ResponseEntity.created(location).allow(HttpMethod.GET).body(user);
	}

}
