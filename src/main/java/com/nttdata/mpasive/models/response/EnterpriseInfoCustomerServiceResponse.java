package com.nttdata.mpasive.models.response;


import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EnterpriseInfoCustomerServiceResponse {

    private String name;
    private String ruc;
    private ArrayList<PersonInfoClientServiceResponse> representatives;
}
