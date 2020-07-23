package com.codeacademy.pastryshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
@AllArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "You must enter a username!")
    @Size(min = 5, max = 20, message = "Username has to be between 5-20 characters long!")
    private String username;

    @NotBlank(message = "You must enter a password!")
    @Pattern(regexp = "((?=.*[@!#$%])(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,22})",
        message = "Password must have one uppercase character, one number and be at least 8 characters long!")
    private String password;

    @Size(min = 3, max = 15, message = "First name must be 3-15 characters long!")
    private String firstName;

    @Size(min = 3, max = 15, message = "Last name must be 3-15 characters long!")
    private String lastName;

    @NotBlank(message = "You must enter an email address!")
    @Email(message = "Enter a valid email address!")
    private String email;

    @NotBlank
    @Pattern(regexp="^[0-9]{10}", message="Phone number length must be 10!")
    private String phoneNumber;

    @Pattern(regexp ="^[1-9]{9}", message = "Bulstat must containt 9 numeric characters!")
    private String bulstat;
}
