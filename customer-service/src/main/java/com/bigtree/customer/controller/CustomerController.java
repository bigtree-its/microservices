package com.bigtree.customer.controller;

import com.bigtree.customer.entity.Customer;
import com.bigtree.customer.error.ApiException;
import com.bigtree.customer.model.*;
import com.bigtree.customer.service.LoginService;
import com.bigtree.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    LoginService loginService;

    @GetMapping("/customers/v1")
    public ResponseEntity<List<Customer>> getAll(){
        log.info("Received request to get all customers");
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.ok().body(customers);
    }
    
    @GetMapping(value = "/customers/v1/{customerId}")
    public ResponseEntity<Customer> get(@PathVariable UUID customerId){
        log.info("Received request to get customer {}", customerId);
        Customer customer = customerService.getCustomer(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping(value = "/customers/v1/password-reset/initiate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> passwordResetInitiate(@Valid @RequestBody PasswordResetInitiate req){
        log.info("Received password reset initiate request for customer {}", req.getEmail());
        loginService.passwordResetInitiate(req.getEmail());
        ApiResponse apiResponse = ApiResponse.builder().message("An OTP sent to your registered email address. Please use that to reset your password").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping(value = "/customers/v1/password-reset/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> passwordResetSubmit(@Valid @RequestBody PasswordResetSubmit req){
        log.info("Received password reset request for customer {}", req.getEmail());
        loginService.passwordResetSubmit(req);
        ApiResponse apiResponse = ApiResponse.builder().message("Your password has been successfully changed.").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/customers/v1/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Received request to login for customer {}", loginRequest.getEmail());
        LoginResponse response = loginService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/customers/v1/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){
        log.info("Received request to logout customer {}", request.getCustomerId());
        loginService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/customers/v1/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("Received request to create new customer {}", customerRegistrationRequest);
        boolean success = customerService.registerCustomer(customerRegistrationRequest);
        if ( success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new ApiException(HttpStatus.BAD_REQUEST, "Customer registration failed");
    }

    @PostMapping(value = "/customers/v1", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer){
        log.info("Received request to create new customer {}", customer);
        Customer newCustomer = customerService.saveCustomer(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PutMapping(value = "/customers/v1/{customerId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> update(@PathVariable UUID customerId, @Valid @RequestBody Customer customer){
        log.info("Received request to update customer {}", customerId);
        Customer updated = customerService.updateCustomer(customerId, customer);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping(value = "/customers/v1/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID customerId){
        log.info("Received request to delete customer {}", customerId);
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
