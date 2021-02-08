package com.zhenyuye.webapp.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhenyuye.webapp.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@Data
public class UserRegisterDTO {
    @NotNull(message = "Username is required.")
    @Email(message = "username should be valid email address.")
    private String username;
    @NotNull(message = "Password is required.")
    @ValidPassword
    private String password;
    @NotNull(message = "first_name is required.")
    @JsonProperty("first_name")
    private String firstName;
    @NotNull(message = "last_name is required.")
    @JsonProperty("last_name")
    private String lastName;
}
