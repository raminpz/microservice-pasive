package com.nttdata.mpasive.models.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreditCardCreditCardResponse {

    private String id;
    private String creditCardNumber;
    private String status;
    private Double creditLine;
    private Double availableAmount;
}
