package com.nttdata.mpasive.models.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PersonInfo {

    @NotBlank(message = "Field name must be required")
    private String name;
    @NotBlank(message = "Field lastname must be required")
    private String lastname;
    @NotBlank(message = "Field documentNumber must be required")
    private String documentNumber;
    @NotBlank(message = "Field mobileNumber must be required")
    private String mobileNumber;
    @NotBlank(message = "Field nationality must be required")
    private String nationality;
    private Address address; // current direction
    @NotBlank(message = "Field email must be required")
    private String email;
    private Date birthdate;
}
