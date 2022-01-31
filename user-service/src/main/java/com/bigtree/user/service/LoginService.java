package com.bigtree.user.service;

import com.bigtree.user.entity.PasswordResetOtp;
import com.bigtree.user.entity.Session;
import com.bigtree.user.entity.User;
import com.bigtree.user.entity.UserAccount;
import com.bigtree.user.error.ApiException;
import com.bigtree.user.model.*;
import com.bigtree.user.repository.PasswordResetOtpRepository;
import com.bigtree.user.repository.SessionRepository;
import com.bigtree.user.repository.UserAccountRepository;
import com.bigtree.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class LoginService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    PasswordResetOtpRepository resetRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        LoginResponse response = null;
        if (user != null) {
            UserAccount account = userAccountRepository.getAccountByUserId(user.getId());
            if (account != null) {
                if (account.getPassword().equals(loginRequest.getPassword())) {
                    final Session session = new Session();
                    session.setStart(LocalDateTime.now());
                    session.setUserId(user.getId());
                    session.setSessionId(UUID.randomUUID());
                    Session sessionActive = sessionRepository.save(session);
                    response = LoginResponse.builder()
                            .success(true)
                            .user(user)
                            .session(sessionActive)
                            .message("Login Success")
                            .build();
                } else {
                    response = LoginResponse.builder().success(false).message("Username or Password not match").build();
                }
            }
        } else {
            response = LoginResponse.builder().success(false).message("User not found").build();
        }
        return response;
    }

    public void logout(LogoutRequest request) {
        Session session = sessionRepository.findByUserId(request.getUserId());
        if ( session != null){
            log.info("Session found. Logging out");
            session.setFinish(LocalDateTime.now());
            sessionRepository.save(session);
        }
    }

    public void passwordResetInitiate(String email) {
        User user = userRepository.findByEmail(email);
        if ( user == null || user.getId() == null){
            log.error("User not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not found");
        }
        PasswordResetOtp otp = PasswordResetOtp.builder()
                .otp(UUID.randomUUID())
                .userId(user.getId())
                .start(LocalDateTime.now())
                .build();
        log.info("Generating otp {}", otp);
        resetRepository.save(otp);
    }

    public void passwordResetSubmit(PasswordResetSubmit req) {
        User user = userRepository.findByEmail(req.getEmail());
        if ( user == null || user.getId() == null){
            log.error("User not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "User not found");
        }
        PasswordResetOtp resetOtp = resetRepository.findByUserId(user.getId());
        if ( resetOtp.getOtp().equals(req.getOtp()) ){
            UserAccount account = userAccountRepository.getAccountByUserId(user.getId());
            account.setPassword(req.getPassword());
            account.setPasswordChanged(LocalDateTime.now());
            userAccountRepository.save(account);
            resetRepository.delete(resetOtp);
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "OTP not found or not matched");
        }
    }
}
