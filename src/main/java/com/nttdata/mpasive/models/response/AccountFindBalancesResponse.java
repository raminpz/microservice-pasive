package com.nttdata.mpasive.models.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountFindBalancesResponse {

    private String id;
    private AccountTypeFindBalancesResponse accountType;
    private Double currentBalance;
}
