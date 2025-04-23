package com.mobileprovider.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupDTO {
    private String email;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
}
