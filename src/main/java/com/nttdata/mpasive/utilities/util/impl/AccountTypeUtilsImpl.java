package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.models.dto.AccountType;
import com.nttdata.mpasive.models.response.AccountTypeFindBalancesResponse;
import com.nttdata.mpasive.utilities.util.IAccountTypeUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeUtilsImpl implements IAccountTypeUtils {

    @Override
    public AccountType accountTypeFindBalancesResponseDTOToAccountType(AccountTypeFindBalancesResponse accountTypeDTO) {
        return AccountType.builder()
                .description(accountTypeDTO.getDescription())
                .build();
    }

    @Override
    public AccountTypeFindBalancesResponse accountTypeToAccountTypeFindBalancesResponseDTO(AccountType accountType) {
        return AccountTypeFindBalancesResponse.builder()
                .description(accountType.getDescription())
                .build();
    }
}
