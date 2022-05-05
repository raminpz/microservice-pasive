package com.nttdata.mpasive.utilities.util;

public interface IAccountUtils {

    AccountEntity accountCreateRequestDTOToAccount(CreateAccountRequest accountDTO);
    AccountEntity accountUpdateRequestDTOToAccount(UpdateAccountRequest accountDTO);
    AccountEntity accountFindBalancesResponseDTOToAccount(AccountFindBalancesResponse accountDTO);
    CreateAccountRequest accountToAccountCreateRequestDTO(AccountEntity account);
    UpdateAccountRequest accountToAccountUpdateRequestDTO(AccountEntity account);
    AccountFindBalancesResponse accountToAccountFindBalancesResponseDTO(AccountEntity account);
    AccountEntity fillAccountWithAccountUpdateRequestDTO(AccountEntity account, UpdateAccountRequest accountDTO);
}
