package com.project.HeartConnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UserInputDto {
    @NotBlank(message = "Provide valid username, value shouldn't be empty or null")
    private String username;
    @NotNull(message = "Provide valid age, value shouldn't be empty or null")
    private Integer age;
    @NotBlank(message = "Provide valid email, value shouldn't be empty or null")
    private String email;
    @NotBlank(message = "Provide valid password, value shouldn't be empty or null")
    @Length(min = 4, max = 6)
    private String password;
}
