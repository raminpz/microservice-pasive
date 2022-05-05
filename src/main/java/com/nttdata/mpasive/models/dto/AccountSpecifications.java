package com.nttdata.mpasive.models.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountSpecifications {
    private Double maintenanceCommission;
    private Double minimumDailyAverage;

    private Double transactionCommission;
    private Integer maximumNumberOfTransaction;
    private Integer allowedDayForTransaction;
}
