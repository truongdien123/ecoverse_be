package com.fpt.ecoverse_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentExcelRowDto {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
}
