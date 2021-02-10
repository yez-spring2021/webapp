package com.zhenyuye.webapp.userDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserCredential {
    private String email;
    private String password;
}
