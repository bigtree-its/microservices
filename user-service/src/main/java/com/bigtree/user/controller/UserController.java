package com.bigtree.user.controller;

import com.bigtree.user.entity.User;
import com.bigtree.user.error.ApiException;
import com.bigtree.user.model.*;
import com.bigtree.user.service.LoginService;
import com.bigtree.user.service.UserService;
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
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @GetMapping("/users/v1")
    public ResponseEntity<List<User>> getAll(){
        log.info("Received request to get all users");
        List<User> users = userService.getUsers();
        return ResponseEntity.ok().body(users);
    }
    
    @GetMapping(value = "/users/v1/{userId}")
    public ResponseEntity<User> get(@PathVariable UUID userId){
        log.info("Received request to get user {}", userId);
        User user = userService.getUser(userId);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(value = "/users/v1/password-reset/initiate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> passwordResetInitiate(@Valid @RequestBody PasswordResetInitiate req){
        log.info("Received password reset initiate request for user {}", req.getEmail());
        loginService.passwordResetInitiate(req.getEmail());
        ApiResponse apiResponse = ApiResponse.builder().message("An OTP sent to your registered email address. Please use that to reset your password").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping(value = "/users/v1/password-reset/submit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> passwordResetSubmit(@Valid @RequestBody PasswordResetSubmit req){
        log.info("Received password reset request for user {}", req.getEmail());
        loginService.passwordResetSubmit(req);
        ApiResponse apiResponse = ApiResponse.builder().message("Your password has been successfully changed.").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/users/v1/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("Received request to login for user {}", loginRequest.getEmail());
        LoginResponse response = loginService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/users/v1/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request){
        log.info("Received request to logout user {}", request.getUserId());
        loginService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/users/v1/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest){
        log.info("Received request to create new user {}", userRegistrationRequest);
        boolean success = userService.registerUser(userRegistrationRequest);
        if ( success){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        throw new ApiException(HttpStatus.BAD_REQUEST, "User registration failed");
    }

    @PostMapping(value = "/users/v1", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        log.info("Received request to create new user {}", user);
        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/v1/{userId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> update(@PathVariable UUID userId, @Valid @RequestBody User user){
        log.info("Received request to update user {}", userId);
        User updated = userService.updateUser(userId, user);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping(value = "/users/v1/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId){
        log.info("Received request to delete user {}", userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
