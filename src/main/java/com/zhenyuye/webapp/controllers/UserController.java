package com.zhenyuye.webapp.controllers;

import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.services.impl.UserService;
import com.zhenyuye.webapp.dtos.userDto.UserData;
import com.zhenyuye.webapp.dtos.userDto.UserRegisterDTO;
import com.zhenyuye.webapp.dtos.userDto.UserUpdateDTO;
import com.zhenyuye.webapp.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/v1/user")
    public ResponseEntity<UserData> createUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO, BindingResult result) {
        ValidationUtil.verifyInput(result);
        User user = userService.createUser(userRegisterDTO);
        UserData userData = buildUserData(user);
        return new ResponseEntity<>(userData, HttpStatus.CREATED);
    }

    @GetMapping("/v1/user/self")
    public ResponseEntity<UserData> getUser(@RequestHeader("authorization") String auth) {
        if (!auth.isEmpty()) {
            User user = userService.getUser(auth);
            UserData userData = buildUserData(user);
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/v1/user/self")
    public ResponseEntity<UserData> updateUser(@RequestHeader("authorization") String auth, @Valid @RequestBody UserUpdateDTO userUpdateDTO, BindingResult result) {
        ValidationUtil.verifyInput(result);
        if (!auth.isEmpty()) {
            User user= userService.updateUser(auth, userUpdateDTO);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
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