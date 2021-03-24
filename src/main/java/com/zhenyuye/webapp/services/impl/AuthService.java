package com.zhenyuye.webapp.services.impl;

import com.timgroup.statsd.StatsDClient;
import com.zhenyuye.webapp.exceptions.UnAuthorizedException;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import com.zhenyuye.webapp.dtos.userDto.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Base64;

@Service
public class AuthService {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final String AUTHORIZATION_PREFIX = "Basic ";
    private static final String COLON = ":";
    private static final String TIMER_POSTFIX = "_timer";
    private static final String DB_USERS_FIND_BY_USERNAME = "db.users.findUserByUsername";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private StatsDClient statsDClient;
    public User getUserByUsername(String username) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        User user = userRepository.findUserByUsername(username);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_USERS_FIND_BY_USERNAME + "_timer", stopWatch.getLastTaskTimeMillis());
        if(user == null) {
            throw new UsernameNotFoundException("User does not exist.");
        }
        return user;
    }

    public void verifyPassword(String password, User user) {
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credential.");
        }
    }

    public UserCredential parseAuth(String auth) {
        try {
            auth = auth.replace(AUTHORIZATION_PREFIX, "");
            String[] userInfo = new String(DECODER.decode(auth.getBytes())).split(COLON);
            return UserCredential.builder()
                    .email(userInfo[0])
                    .password(userInfo[1])
                    .build();
        }catch (Exception e) {
            throw new UnAuthorizedException("Bad credential.");
        }
    }
}
