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
    @NotNull(message = "Email is required.")
    @Email(message = "Invalid Email Address.")
    private String email;
    @NotNull(message = "Password is required.")
    @ValidPassword
    private String password;
    @NotNull(message = "FirstName is required.")
    @JsonProperty("first_name")
    private String firstName;
    @NotNull(message = "LastName is required.")
    @JsonProperty("last_name")
    private String lastName;
}
