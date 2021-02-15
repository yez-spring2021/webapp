package com.zhenyuye.webapp.dtos.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhenyuye.webapp.annotation.ValidPassword;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;

@Builder
@Data
public class UserUpdateDTO {
    @Null(message = "Email cannot be changed.")
    private String email;
    @Null(message = "Username cannot be changed.")
    private String username;
    @ValidPassword
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
}
