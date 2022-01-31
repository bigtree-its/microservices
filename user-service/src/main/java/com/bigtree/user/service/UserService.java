package com.bigtree.user.service;

import com.bigtree.user.entity.Session;
import com.bigtree.user.entity.User;
import com.bigtree.user.entity.UserAccount;
import com.bigtree.user.error.ApiException;
import com.bigtree.user.model.LoginRequest;
import com.bigtree.user.model.LoginResponse;
import com.bigtree.user.model.UserRegistrationRequest;
import com.bigtree.user.repository.SessionRepository;
import com.bigtree.user.repository.UserAccountRepository;
import com.bigtree.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    LoginService loginService;

    @Transactional
    public User saveUser(User user){
        log.info("Saving new user");
        User exist = repository.findByEmail(user.getEmail());
        if ( exist != null && exist.getId() != null){
            log.error("User already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User already exist");
        }
        User save = repository.save(user);
        if ( save != null && save.getId() != null){
            log.info("User created {}", save.getId());
        }
        return save;
    }

    public List<User> getUsers(){
        log.info("Fetching all users");
        return repository.findAll();
    }

    public User updateUser(UUID id, User user) {
        Optional<User> optional = repository.findById(id);
        if ( optional.isPresent()){
           log.info("User already exist. Updating");
            User exist = optional.get();
            if (StringUtils.hasLength(user.getFirstName())){
                exist.setFirstName(user.getFirstName());
            }
            if (StringUtils.hasLength(user.getLastName())){
                exist.setLastName(user.getLastName());
            }
            if (StringUtils.hasLength(user.getMobile())){
                exist.setMobile(user.getMobile());
            }
            if (StringUtils.hasLength(user.getEmail())){
                exist.setEmail(user.getEmail());
            }
            if (user.getAddress() != null){
                exist.setAddress(user.getAddress());
            }
            User updated = repository.save(exist);
            if ( updated != null && updated.getId() != null){
                log.info("User updated {}", updated.getId());
            }
            return updated;
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not exist");
        }

    }

    public void deleteUser(UUID userId) {
        Optional<User> optional = repository.findById(userId);
        if ( optional.isPresent()) {
            log.info("User already exist. Deleting user");
            repository.deleteById(userId);
        }else{
            log.error("User not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    public User getUser(UUID userId) {
        Optional<User> optional = repository.findById(userId);
        if ( optional.isPresent()) {
            log.error("User found");
            return optional.get();
        }else{
            log.error("User not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }


    public boolean registerUser(UserRegistrationRequest req) {
        if (!StringUtils.hasLength(req.getEmail())){
            log.error("User email is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User email is mandatory");
        }
        if (!StringUtils.hasLength(req.getMobile())){
            log.error("User mobile is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User mobile is mandatory");
        }
        if (!StringUtils.hasLength(req.getPassword())){
            log.error("User password is mandatory");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User password is mandatory");
        }
        User user = repository.findByEmail(req.getEmail());
        if ( user != null && user.getId() != null){
            log.error("User already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User already exist");
        }
        log.info("Registering an user");
        User newUser = User.builder().email(req.getEmail()).firstName(req.getFirstName()).lastName(req.getLastName()).mobile(req.getMobile()).build();
        User saved = repository.save(newUser);
        if ( saved != null && saved.getId() != null){
            log.info("User created {}", saved.getId());
            UserAccount userAccount = UserAccount.builder()
                    .userId(saved.getId())
                    .password(req.getPassword())
                    .passwordChanged(LocalDateTime.now())
                    .build();
            UserAccount account = userAccountRepository.save(userAccount);
            if (account != null && account.getId() != null){
               return true;
            }
        }
        return false;
    }
}
