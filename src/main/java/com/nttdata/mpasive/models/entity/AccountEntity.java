package com.nttdata.mpasive.models.entity;

import com.nttdata.mpasive.models.dto.AccountSpecifications;
import com.nttdata.mpasive.models.dto.AccountType;
import com.nttdata.mpasive.models.dto.Client;
import com.nttdata.mpasive.models.dto.Transaction;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "accounts")
public class AccountEntity {

    @Id
    private String id;
    private String status; // blocked or active
    private String accountNumber; // number auto-generate
    private Client client; // client

    private AccountType accountType; // type of account
    private AccountSpecifications accountSpecifications;
    private Date creationDate; // date of creation
    private Date updateDate; // last transaction
    private Double currentBalance; // current balance
    private Integer freeTransactionPerMonth; // limit of transaction per month
    private ArrayList<Transaction> transactions; // transactions at the moment
}
