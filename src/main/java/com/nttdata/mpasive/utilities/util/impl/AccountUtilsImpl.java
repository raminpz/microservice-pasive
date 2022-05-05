package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.models.dto.Client;
import com.nttdata.mpasive.models.entity.AccountEntity;
import com.nttdata.mpasive.models.request.AccountCreateRequest;
import com.nttdata.mpasive.models.request.AccountUpdateRequest;
import com.nttdata.mpasive.models.response.AccountFindBalancesResponse;
import com.nttdata.mpasive.utilities.util.IAccountTypeUtils;
import com.nttdata.mpasive.utilities.util.IAccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUtilsImpl implements IAccountUtils {

    private IAccountTypeUtils accountTypeUtils;

    public AccountEntity accountCreateRequestDTOToAccount(AccountCreateRequest accountDTO){
        return AccountEntity.builder()
                .client(Client.builder().id(accountDTO.getClientId()).build())
                .accountType(accountDTO.getAccountType())
                .accountSpecifications(accountDTO.getAccountSpecifications())
                .accountNumber(UUID.randomUUID().toString())
                .creationDate(accountDTO.getCreationDate())
                .updateDate(accountDTO.getUpdateDate())
                .build();
    }

    public AccountEntity accountUpdateRequestDTOToAccount(AccountUpdateRequest accountDTO){
        return AccountEntity.builder()
                .id(accountDTO.getId())
                .status(accountDTO.getStatus())
                .updateDate(accountDTO.getUpdateDate())
                .currentBalance(accountDTO.getCurrentBalance())
                .freeTransactionPerMonth(accountDTO.getFreeTransactionPerMonth())
                .build();
    }


    @Override
    public AccountEntity accountFindBalancesResponseDTOToAccount(AccountFindBalancesResponse accountDTO) {
        return AccountEntity.builder()
                .id(accountDTO.getId())
                .currentBalance(accountDTO.getCurrentBalance())
                .accountType(accountTypeUtils.accountTypeFindBalancesResponseDTOToAccountType(accountDTO.getAccountType()))
                .build();
    }

    @Override
    public AccountCreateRequest accountToAccountCreateRequestDTO(AccountEntity account) {
        return AccountCreateRequest.builder()
                .clientId(account.getClient().getId())
                .accountType(account.getAccountType())
                .accountSpecifications(account.getAccountSpecifications())
                .creationDate(account.getCreationDate())
                .creationDate(account.getCreationDate())
                .build();
    }

    @Override
    public AccountUpdateRequest accountToAccountUpdateRequestDTO(AccountEntity account) {
        return AccountUpdateRequest.builder()
                .id(account.getId())
                .updateDate(account.getUpdateDate())
                .currentBalance(account.getCurrentBalance())
                .freeTransactionPerMonth(account.getFreeTransactionPerMonth())
                .build();
    }


    @Override
    public AccountFindBalancesResponse accountToAccountFindBalancesResponseDTO(AccountEntity account) {
        return AccountFindBalancesResponse.builder()
                .id(account.getId())
                .currentBalance(account.getCurrentBalance())
                .accountType(accountTypeUtils.accountTypeToAccountTypeFindBalancesResponseDTO(account.getAccountType()))
                .build();
    }

    @Override
    public AccountEntity fillAccountWithAccountUpdateRequestDTO(AccountEntity account, AccountUpdateRequest accountDTO) {
        account.setId(accountDTO.getId());
        account.setStatus(accountDTO.getStatus());
        account.setUpdateDate(accountDTO.getUpdateDate());
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setFreeTransactionPerMonth(accountDTO.getFreeTransactionPerMonth());
        return account;
    }
}
