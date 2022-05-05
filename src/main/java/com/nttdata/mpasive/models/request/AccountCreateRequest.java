package com.nttdata.mpasive.models.request;


import com.nttdata.mpasive.models.dto.AccountSpecifications;
import com.nttdata.mpasive.models.dto.AccountType;
import com.nttdata.mpasive.models.dto.PersonInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountCreateRequest {

    private String clientId;
    private AccountType accountType;
    private AccountSpecifications accountSpecifications;
    private ArrayList<PersonInfo> holders;
    private ArrayList<PersonInfo> signers;
}
