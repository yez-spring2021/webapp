package com.zhenyuye.webapp.dtos.userDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredential {
    private String email;
    private String password;
}
