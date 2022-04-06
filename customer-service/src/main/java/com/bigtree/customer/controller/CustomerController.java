package com.bigtree.customer.controller;

import com.bigtree.customer.entity.Customer;
import com.bigtree.customer.error.ApiException;
import com.bigtree.customer.model.*;
import com.bigtree.customer.service.LoginService;
import com.bigtree.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    LoginService loginService;

    @GetMapping(value = "/customers/{customerId}")
    public ResponseEntity<Customer> get(@PathVariable UUID customerId){
        log.info("Received request to get customer {}", customerId);
        Customer customer = customerService.getCustomer(customerId);
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(value = "/customers")
    public ResponseEntity<List<Customer>> findByEmail(@RequestParam(value = "email", required = false) String email){
        if (StringUtils.isEmpty(email)){
            log.info("Received request to get all customers");
            List<Customer> customers = customerService.getCustomers();
            return ResponseEntity.ok().body(customers);
        }else{
            log.info("Received request to get customer {}", email);
            Customer customer = customerService.findByEmail(email);
            List<Customer> list = new ArrayList<>();
            if ( customer == null){
                log.info("No customers found");
            }else{
                list.add(customer);
            }
            return ResponseEntity.ok().body(list);
        }
    }

       @GetMapping(value = "/customers/password-reset/validate",produces = "application/json")
    public ResponseEntity<BooleanResponse> isOtpValid(@RequestParam(value = "email", required = true) String email, @RequestParam(value = "otp", required = true) String otp){
        log.info("Received request to validate otp {} for {} ", otp, email);
        boolean valid = loginService.isValid(email, UUID.fromString(otp));
           BooleanResponse booleanResponse = BooleanResponse.builder().value(valid).build();
           return new ResponseEntity<>(booleanResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/customers/password-reset/initiate", produces = "application/json")
    public ResponseEntity<BooleanResponse> passwordResetInitiate(@RequestParam(value = "email", required = true) String email){
        log.info("Received password reset initiate request for customer {}", email);
        loginService.passwordResetInitiate(email);
        BooleanResponse booleanResponse = BooleanResponse.builder().value(true).build();
        return new ResponseEntity<>(booleanResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/customers/password-reset/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BooleanResponse> passwordResetSubmit(@Valid @RequestBody PasswordResetSubmit req){
        log.info("Received password reset request ");
        loginService.passwordResetSubmit(req);
        BooleanResponse booleanResponse = BooleanResponse.builder().value(true).build();
        return new ResponseEntity<>(booleanResponse, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @PostMapping(value = "/customers/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Received request to login for customer {}", loginRequest.getEmail());
        LoginResponse response = loginService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @PostMapping(value = "/customers/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){
        log.info("Received request to logout customer {}", request.getCustomerId());
        loginService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @PostMapping(value = "/customers/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("Received request to create new customer {}", customerRegistrationRequest);
        boolean success = customerService.registerCustomer(customerRegistrationRequest);
        if ( success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new ApiException(HttpStatus.BAD_REQUEST, "Customer registration failed");
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @PutMapping(value = "/customers/{customerId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Customer> update(@PathVariable UUID customerId, @Valid @RequestBody Customer customer){
        log.info("Received request to update customer {}", customerId);
        Customer updated = customerService.updateCustomer(customerId, customer);
        return ResponseEntity.ok().body(updated);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @DeleteMapping(value = "/customers/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable UUID customerId){
        log.info("Received request to delete customer {}", customerId);
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @CrossOrigin(origins = {"http://localhost:4200", "*"})
    @DeleteMapping(value = "/customers")
    public ResponseEntity<Void> deleteAll(){
        log.info("Received request to delete all customers");
        customerService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
