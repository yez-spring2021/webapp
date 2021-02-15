package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.dtos.userDto.UserCredential;
import com.zhenyuye.webapp.dtos.userDto.UserRegisterDTO;
import com.zhenyuye.webapp.dtos.userDto.UserUpdateDTO;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.UserRepository;
import com.zhenyuye.webapp.services.impl.AuthService;
import com.zhenyuye.webapp.services.impl.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    private static final String TEST_USERNAME = "test@test.com";
    private static final String TEST_PASSWORD = "passwordTest-1";
    private static final String TEST_FIRSTNAME = "test";
    private static final String TEST_LASTNAME = "test";
    private static final String AUTH = "Basic: auth";
    private static final User USER = User.builder().username(TEST_USERNAME).password(TEST_PASSWORD).firstName(TEST_FIRSTNAME).lastName(TEST_LASTNAME).build();
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private AuthService authService;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUserTest() {
        final UserRegisterDTO userRegisterDTO = UserRegisterDTO.builder().username(TEST_USERNAME).password(TEST_PASSWORD).firstName(TEST_FIRSTNAME).lastName(TEST_LASTNAME).build();
        when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(USER);
        User createdUser = userService.createUser(userRegisterDTO);
        Assertions.assertEquals(createdUser.getUsername(), USER.getUsername());
    }

    @Test
    public void getUserTest() {
        // Arrange
        final UserCredential userCredential = Mockito.mock(UserCredential.class);
        when(authService.parseAuth(Mockito.anyString())).thenReturn(userCredential);
        when(userCredential.getEmail()).thenReturn(TEST_USERNAME);
        when(userCredential.getPassword()).thenReturn(TEST_PASSWORD);
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(USER);
        when(bCryptPasswordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        // Act
        User testUser = userService.getUser(AUTH);
        // Assert
        Assertions.assertEquals(USER.getUsername(), testUser.getUsername());
    }

    @Test
    public void updateUserTest(){
        // Arrange
        final UserCredential userCredential = Mockito.mock(UserCredential.class);
        final String postFix = "_update";
        final UserUpdateDTO updateDTO = UserUpdateDTO.builder().firstName(TEST_FIRSTNAME+postFix).lastName(TEST_LASTNAME+postFix).build();
        when(authService.parseAuth(Mockito.anyString())).thenReturn(userCredential);
        when(userCredential.getEmail()).thenReturn(TEST_USERNAME);
        when(userCredential.getPassword()).thenReturn(TEST_PASSWORD);
        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(USER);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(User.builder().firstName(TEST_FIRSTNAME+postFix).lastName(TEST_LASTNAME+postFix).build());

        // Act
        User updatedUser = userService.updateUser(AUTH, updateDTO);

        Assertions.assertEquals(TEST_FIRSTNAME+postFix,updatedUser.getFirstName());
        Assertions.assertEquals(TEST_LASTNAME+postFix,updatedUser.getLastName());

    }
}
