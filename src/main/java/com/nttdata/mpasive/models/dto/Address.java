package com.nttdata.mpasive.models.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {

    @NotBlank(message = "Field street must be required")
    private String street;
    @NotBlank(message = "Field city must be required")
    private String city;
    @NotBlank(message = "Field country must be required")
    private String country;
    @NotBlank(message = "Field extendedAddress must be required")
    private String extendedAddress;

}
