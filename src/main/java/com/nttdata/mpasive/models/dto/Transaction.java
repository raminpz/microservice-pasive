package com.nttdata.mpasive.models.dto;


import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Transaction {

    private String transactionNumber = UUID.randomUUID().toString();
    private Date time;
    private String type;
    private Double amount;
    private Double commission;
    private Double finalBalance;
}
