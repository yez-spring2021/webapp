package com.zhenyuye.webapp.services.impl;

import com.timgroup.statsd.StatsDClient;
import com.zhenyuye.webapp.dtos.userDto.UserCredential;
import com.zhenyuye.webapp.dtos.userDto.UserRegisterDTO;
import com.zhenyuye.webapp.dtos.userDto.UserUpdateDTO;
import com.zhenyuye.webapp.exceptions.UnAuthorizedException;
import com.zhenyuye.webapp.exceptions.ValidationException;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;


@Service
public class UserService {
    private static final String TIMER_POSTFIX = "_timer";
    private static final String USER_DB_QUERY_INSERT = "db.users.createUser";
    private static final String USER_DB_QUERY_GET_USER_BY_EMAIL = "db.users.getUser";
    private static final String USER_DB_QUERY_UPDATE_USER = "db.users.updateUser";
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private StatsDClient statsDClient;

    @Transactional
    public User createUser(UserRegisterDTO registerDTO) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean hasUser = userRepository.existsByEmail(registerDTO.getUsername());
        if (hasUser) {
            stopWatch.stop();
            statsDClient.recordExecutionTime(USER_DB_QUERY_INSERT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            throw new ValidationException("This email has been registered.");
        }
        User user = User.builder()
                .email(registerDTO.getUsername())
                .password(bCryptPasswordEncoder.encode(registerDTO.getPassword()))
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .username(registerDTO.getUsername())
                .build();
        user = userRepository.save(user);
        stopWatch.stop();
        statsDClient.recordExecutionTime(USER_DB_QUERY_INSERT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return user;
    }

    public User getUser(String auth) {
        UserCredential userCredential = authService.parseAuth(auth);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        User user = userRepository.findByEmail(userCredential.getEmail());
        stopWatch.stop();
        statsDClient.recordExecutionTime(USER_DB_QUERY_GET_USER_BY_EMAIL + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        if (user == null) {
            throw new ValidationException("User does not exist", HttpStatus.NOT_FOUND);
        } else {
            if (bCryptPasswordEncoder.matches(userCredential.getPassword(), user.getPassword())) {
                return user;
            } else {
                throw new UnAuthorizedException("Invalid credentials");
            }
        }
    }

    @Transactional
    public User updateUser(String auth, UserUpdateDTO updateDTO) {
        UserCredential userCredential = authService.parseAuth(auth);

        boolean update = false;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        User user = this.userRepository.findByEmail(userCredential.getEmail());
        if (updateDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
            update = true;
        }
        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName());
            update = true;
        }
        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName());
            update = true;
        }
        if (update) {
            user = userRepository.save(user);
        }
        stopWatch.stop();
        statsDClient.recordExecutionTime(USER_DB_QUERY_UPDATE_USER + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return user;
    }
}
