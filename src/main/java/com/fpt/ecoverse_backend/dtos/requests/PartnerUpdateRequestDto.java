package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerUpdateRequestDto {

    @JsonProperty("organization_name")
    private String organizationName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("contact_person")
    private String contactPerson;

    @JsonProperty("avatar")
    private MultipartFile avatar;
}

