package com.bigtree.customer.service;

import com.bigtree.customer.entity.Contact;
import com.bigtree.customer.entity.Customer;
import com.bigtree.customer.entity.CustomerAccount;
import com.bigtree.customer.error.ApiException;
import com.bigtree.customer.model.CustomerRegistrationRequest;
import com.bigtree.customer.repository.CustomerAccountRepository;
import com.bigtree.customer.repository.CustomerRepository;
import com.bigtree.customer.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    CustomerRepository repository;

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    LoginService loginService;

    public List<Customer> getCustomers() {
        log.info("Fetching all customers");
        return repository.findAll();
    }

    public Customer updateCustomer(UUID id, Customer customer) {
        Optional<Customer> optional = repository.findById(id);
        if (optional.isPresent()) {
            log.info("Customer already exist. Updating");
            Customer exist = optional.get();
            if (!StringUtils.isEmpty(customer.getFirstName())) {
                exist.setFirstName(customer.getFirstName());
            }
            if (!StringUtils.isEmpty(customer.getLastName())) {
                exist.setLastName(customer.getLastName());
            }
            if (customer.getAddress() != null) {
                exist.setAddress(customer.getAddress());
            }
            Customer updated = repository.save(exist);
            if (updated != null && updated.getId() != null) {
                log.info("Customer updated {}", updated.getId());
            }
            return updated;
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not exist");
        }

    }

    public void deleteCustomer(UUID customerId) {
        Optional<Customer> optional = repository.findById(customerId);
        if (optional.isPresent()) {
            log.info("Customer already exist. Deleting customer");
            repository.deleteById(customerId);
        } else {
            log.error("Customer not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Customer getCustomer(UUID customerId) {
        Optional<Customer> optional = repository.findById(customerId);
        if (optional.isPresent()) {
            log.error("Customer found");
            return optional.get();
        } else {
            log.error("Customer not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
    }


    public boolean registerCustomer(CustomerRegistrationRequest req) {
        if (StringUtils.isEmpty(req.getEmail())) {
            log.error("Customer email is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer email is mandatory");
        }
        if (StringUtils.isEmpty(req.getMobile())) {
            log.error("Customer mobile is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer mobile is mandatory");
        }
        if (StringUtils.isEmpty(req.getPassword())) {
            log.error("Customer password is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer password is mandatory");
        }
        Customer customer = repository.findByEmail(req.getEmail());
        if (customer != null && customer.getId() != null) {
            log.error("Customer already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer already exist");
        }
        if (StringUtils.isEmpty(req.getFullName())) {
            req.setFullName(req.getFirstName() + " " + req.getLastName());
        }
        log.info("Registering an customer");
        Contact contact = Contact.builder()
                .email(req.getEmail()).mobile(req.getMobile())
                .build();
        Customer newCustomer = Customer.builder().fullName(req.getFullName()).firstName(req.getFirstName()).lastName(req.getLastName()).contact(contact).build();
        Customer saved = repository.save(newCustomer);
        if (saved != null && saved.getId() != null) {
            log.info("Customer created {}", saved.getId());
            CustomerAccount customerAccount = CustomerAccount.builder()
                    .customerId(saved.getId())
                    .password(req.getPassword())
                    .passwordChanged(LocalDateTime.now())
                    .build();
            CustomerAccount account = customerAccountRepository.save(customerAccount);
            if (account != null && account.getId() != null) {
                return true;
            }
        }
        return false;
    }

    public Customer findByEmail(String email) {
        log.info("Find customers with email {}", email);
        return repository.findByEmail(email);
    }
}
