package com.fpt.ecoverse_backend.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRegisterRequestDto {

    @NotBlank(message = "Organization name can't be null")
    private String organizationName;

    @NotBlank(message = "Email can't be null")
    @Email
    private String email;

    @NotBlank(message = "Password can't be null")
    @Size(min = 8, message = "Password contain at least 8 characters")
    private String password;

    @NotBlank(message = "Contact person can't be null")
    private String contactPerson;

    @NotBlank(message = "Phone number can't be null")
    @Size(min = 10, message = "Phone number contain at least 10 characters")
    private String phoneNumber;

    @NotBlank(message = "Address can't be null")
    private String address;

    private MultipartFile avatar;

}
