package com.bigtree.customer.service;

import com.bigtree.customer.entity.Customer;
import com.bigtree.customer.entity.PasswordResetOtp;
import com.bigtree.customer.entity.Session;
import com.bigtree.customer.entity.CustomerAccount;
import com.bigtree.customer.error.ApiException;
import com.bigtree.customer.model.*;
import com.bigtree.customer.repository.PasswordResetOtpRepository;
import com.bigtree.customer.repository.SessionRepository;
import com.bigtree.customer.repository.CustomerAccountRepository;
import com.bigtree.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerAccountRepository customerAccountRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PasswordResetOtpRepository resetRepository;

    @Autowired
    EmailService emailService;

    public LoginResponse login(LoginRequest loginRequest) {
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail());
        LoginResponse response = null;
        if (customer != null) {
            CustomerAccount account = customerAccountRepository.getAccountByUserId(customer.getId());
            if (account != null) {
                if (account.getPassword().equals(loginRequest.getPassword())) {
                    final Session session = new Session();
                    session.setStart(LocalDateTime.now());
                    session.setCustomerId(customer.getId());
                    session.setSessionId(UUID.randomUUID());
                    Session sessionActive = sessionRepository.save(session);
                    response = LoginResponse.builder()
                            .success(true)
                            .customer(customer)
                            .session(sessionActive)
                            .message("Login Success")
                            .build();
                } else {
                    response = LoginResponse.builder().success(false).message("Username or Password not match").build();
                }
            }
        } else {
            response = LoginResponse.builder().success(false).message("Customer not found").build();
        }
        return response;
    }

    public void logout(LogoutRequest request) {
        Session session = sessionRepository.findByUserId(request.getCustomerId());
        if ( session != null){
            log.info("Session found. Logging out");
            session.setFinish(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    public void passwordResetInitiate(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if ( customer == null || customer.getId() == null){
            log.error("Customer not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
        PasswordResetOtp otp = PasswordResetOtp.builder()
                .otp(UUID.randomUUID())
                .customerId(customer.getId())
                .start(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        log.info("Generating otp {}", otp);
        PasswordResetOtp otpSaved = resetRepository.save(otp);
        if ( otpSaved != null && otpSaved.getId() != null){
            emailService.sendOTP(OTPEmail.builder().customerEmail(email).customerName(customer.getFullName()).otp(otpSaved.getOtp().toString()).passwordResetLink("http://localhost:4200/reset-password").build());
        }
    }

    public boolean isValid(String email, UUID otp) {
        Customer customer = customerRepository.findByEmail(email);
        if ( customer == null || customer.getId() == null){
            log.error("Customer not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
        PasswordResetOtp passwordResetOtp = resetRepository.findByCustomerIdAndOtp(customer.getId(), otp);
        if ( passwordResetOtp == null){
            return false;
        }
        if ( passwordResetOtp.getExpiresAt().isBefore(LocalDateTime.now())){
            return false;
        }
        return true;
    }

    public void passwordResetSubmit(PasswordResetSubmit req) {
        Customer customer = customerRepository.findByEmail(req.getEmail());
        if ( customer == null || customer.getId() == null){
            log.error("Customer not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Customer not found");
        }
        PasswordResetOtp passwordResetOtp = resetRepository.findByCustomerIdAndOtp(customer.getId(), req.getOtp());
        if ( passwordResetOtp != null && passwordResetOtp.getExpiresAt().isAfter(LocalDateTime.now())){
            CustomerAccount account = customerAccountRepository.getAccountByUserId(customer.getId());
            account.setPassword(req.getPassword());
            account.setPasswordChanged(LocalDateTime.now());
            customerAccountRepository.save(account);
            resetRepository.delete(passwordResetOtp);
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "OTP not found or expired");
        }
    }
}
