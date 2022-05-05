package com.nttdata.mpasive.utilities.util;

public interface IAccountTypeUtils {

    AccountType accountTypeFindBalancesResponseDTOToAccountType(AccountTypeFindBalancesResponse accountTypeDTO);
    AccountTypeFindBalancesResponse accountTypeToAccountTypeFindBalancesResponseDTO(AccountType accountType);
}
