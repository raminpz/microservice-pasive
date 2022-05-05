package com.nttdata.mpasive.models.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddressCustomerServiceResponse {

    private Integer number;
    private String street;
    private String city;
    private String country;
}
