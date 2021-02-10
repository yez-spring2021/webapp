package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.exceptions.UnAuthorizedException;
import com.zhenyuye.webapp.exceptions.ValidationException;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import com.zhenyuye.webapp.userDto.UserCredential;
import com.zhenyuye.webapp.userDto.UserRegisterDTO;
import com.zhenyuye.webapp.userDto.UserUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public User createUser(UserRegisterDTO registerDTO) {
        boolean hasUser = userRepository.existsByEmail(registerDTO.getUsername());
        if (hasUser) {
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
        return user;
    }

    public User getUser(String auth) {
        UserCredential userCredential = authService.parseAuth(auth);
        User user = userRepository.findByEmail(userCredential.getEmail());
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
        User user  = this.userRepository.findByEmail(userCredential.getEmail());
        if(updateDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(updateDTO.getPassword()));
            update =true;
        }
        if(updateDTO.getFirstName()!=null) {
            user.setFirstName(updateDTO.getFirstName());
            update = true;
        }
        if(updateDTO.getLastName()!=null) {
            user.setLastName(updateDTO.getLastName());
            update = true;
        }
        if(update) {
            user = userRepository.save(user);
        }
        return user;
    }
}
