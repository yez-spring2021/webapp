package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.exceptions.UnAuthorizedException;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import com.zhenyuye.webapp.userDto.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final String AUTHORIZATION_PREFIX = "Basic ";
    private static final String COLON = ":";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
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
