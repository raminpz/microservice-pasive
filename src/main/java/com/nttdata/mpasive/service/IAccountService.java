package com.nttdata.mpasive.service;

import com.nttdata.mpasive.models.dto.Transaction;
import com.nttdata.mpasive.models.entity.AccountEntity;
import com.nttdata.mpasive.models.request.AccountDoTransactionRequest;
import com.nttdata.mpasive.models.request.AccountCreateRequest;
import com.nttdata.mpasive.models.request.AccountUpdateRequest;
import com.nttdata.mpasive.models.response.AccountFindBalancesResponse;
import com.nttdata.mpasive.models.response.ClientClientServiceResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountService {

    Mono<AccountEntity> create(AccountCreateRequest accountDTO);
    Mono<AccountEntity> findById(String id);
    Flux<AccountEntity> findAll();
    Mono<AccountEntity> update(AccountUpdateRequest accountDTO);
    Mono<AccountEntity> removeById(String id);
    Mono<ClientClientServiceResponse> findByIdClientService(String id);
    Flux<AccountEntity> findByClientId(String id);
    Mono<AccountEntity> findByAccountNumber(String number);

    Mono<AccountEntity> doTransaction(AccountDoTransactionRequest accountDTO);

    Flux<Transaction> findTransactionsByAccountId(String id) ;

    Mono<AccountFindBalancesResponse> findBalancesByAccountId(String id);
    Flux<ClientClientServiceResponse> clientTest();
}
