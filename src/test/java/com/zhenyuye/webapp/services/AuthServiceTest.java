package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.dtos.userDto.UserCredential;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import com.zhenyuye.webapp.services.impl.AuthService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Base64;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthServiceTest {
    private static final String TEST_USERNAME = "test@test.com";
    private static final String TEST_PASSWORD = "passwordTest-1";
    private static final String TEST_FIRSTNAME = "test";
    private static final String TEST_LASTNAME = "test";
    private static final String AUTH = "Basic test@test.com:passwordTest-1";
    private static final User USER = User.builder().username(TEST_USERNAME).password(TEST_PASSWORD).firstName(TEST_FIRSTNAME).lastName(TEST_LASTNAME).build();
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private AuthService authService;

    @Test
    public void getUserByUsernameTest() {
        // Arrange
        when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(USER);

        User user = authService.getUserByUsername(TEST_USERNAME);

        Assertions.assertEquals(TEST_USERNAME, user.getUsername());
    }

    @Test
    public void verifyPasswordTest() {
        // Arrange
        when(bCryptPasswordEncoder.matches(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        authService.verifyPassword(TEST_PASSWORD, USER);

        verify(bCryptPasswordEncoder, times(1)).matches(Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void parseAuthTest() {
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedAuth = encoder.encodeToString((TEST_USERNAME+":"+TEST_PASSWORD).getBytes());
        UserCredential userCredential = authService.parseAuth(encodedAuth);
        Assertions.assertEquals(TEST_USERNAME, userCredential.getEmail());
        Assertions.assertEquals(TEST_PASSWORD, userCredential.getPassword());
    }

}
