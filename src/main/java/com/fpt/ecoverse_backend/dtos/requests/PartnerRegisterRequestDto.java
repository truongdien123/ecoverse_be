package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@AllArgsConstructor
@NoArgsConstructor
public class PartnerRegisterRequestDto {

    @NotBlank(message = "Organization name can't be null")
    @JsonProperty("organization_name")
    private String organizationName;

    @NotBlank(message = "Email can't be null")
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password can't be null")
    @Size(min = 8, message = "Password contain at least 8 characters")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "Contact person can't be null")
    @JsonProperty("contact_person")
    private String contactPerson;

    @NotBlank(message = "Phone number can't be null")
    @Size(min = 10, message = "Phone number contain at least 10 characters")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Address can't be null")
    @JsonProperty("address")
    private String address;

    @JsonProperty("avatar")
    private MultipartFile avatar;


}
