package com.zhenyuye.webapp.controllers;

import com.timgroup.statsd.StatsDClient;
import com.zhenyuye.webapp.dtos.userDto.UserData;
import com.zhenyuye.webapp.dtos.userDto.UserRegisterDTO;
import com.zhenyuye.webapp.dtos.userDto.UserUpdateDTO;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.services.impl.UserService;
import com.zhenyuye.webapp.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private static final String CREATE_USER_ENDPOINT = "v1.user.http.post";
    private static final String GET_USER_ENDPOINT = "v1.user.self.get";
    private static final String UPDATE_USER_ENDPOINT = "v1.user.self.put";
    private static final String COUNTER_POSTFIX = "_counter";
    private static final String TIMER_POSTFIX = "_timer";
    @Autowired
    private UserService userService;

    @Autowired
    private StatsDClient statsDClient;

    @PostMapping(value = "/v1/user")
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO, BindingResult result) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        statsDClient.incrementCounter(CREATE_USER_ENDPOINT + COUNTER_POSTFIX);
        ValidationUtil.verifyInput(result);
        User user = userService.createUser(userRegisterDTO);
        UserData userData = buildUserData(user);
        stopWatch.stop();
        statsDClient.recordExecutionTime(CREATE_USER_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return new ResponseEntity<>(userData, HttpStatus.CREATED);
    }

    @GetMapping("/v1/user/self")
    public ResponseEntity<UserData> getUser(@RequestHeader("authorization") String auth) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ;
        statsDClient.incrementCounter(GET_USER_ENDPOINT + COUNTER_POSTFIX);
        if (!auth.isEmpty()) {
            User user = userService.getUser(auth);
            UserData userData = buildUserData(user);
            stopWatch.stop();
            statsDClient.recordExecutionTime(GET_USER_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            stopWatch.stop();
            statsDClient.recordExecutionTime(GET_USER_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/v1/user/self")
    public ResponseEntity<UserData> updateUser(@RequestHeader("authorization") String auth, @Valid @RequestBody UserUpdateDTO userUpdateDTO, BindingResult result) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ;
        statsDClient.incrementCounter(UPDATE_USER_ENDPOINT + COUNTER_POSTFIX);
        ValidationUtil.verifyInput(result);
        if (!auth.isEmpty()) {
            User user = userService.updateUser(auth, userUpdateDTO);
            stopWatch.stop();
            statsDClient.recordExecutionTime(UPDATE_USER_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            stopWatch.stop();
            statsDClient.recordExecutionTime(UPDATE_USER_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private UserData buildUserData(User user) {
        return UserData.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accountCreated(user.getAccountCreated())
                .accountUpdated(user.getAccountUpdated())
                .build();
    }
}