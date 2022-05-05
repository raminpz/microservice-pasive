package com.nttdata.mpasive.models.response;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PersonInfoClientServiceResponse {

    private String name;
    private String lastname;
    private String documentNumber;
    private String mobileNumber;
    private String email;
    private Date birthdate;
}
