package com.nttdata.mpasive.service;

import com.nttdata.mpasive.models.dto.Client;
import com.nttdata.mpasive.models.dto.Transaction;
import com.nttdata.mpasive.models.entity.AccountEntity;
import com.nttdata.mpasive.models.request.AccountCreateRequest;
import com.nttdata.mpasive.models.request.AccountDoTransactionRequest;
import com.nttdata.mpasive.models.request.AccountUpdateRequest;
import com.nttdata.mpasive.models.request.TransactionDoTransactionRequest;
import com.nttdata.mpasive.models.response.AccountFindBalancesResponse;
import com.nttdata.mpasive.models.response.ClientClientServiceResponse;
import com.nttdata.mpasive.models.response.CreditCardCreditCardResponse;
import com.nttdata.mpasive.repository.IAccountRepository;
import com.nttdata.mpasive.utilities.constants.Constants;
import com.nttdata.mpasive.utilities.errors.*;
import com.nttdata.mpasive.utilities.util.IAccountSpecificationsUtils;
import com.nttdata.mpasive.utilities.util.IAccountUtils;
import com.nttdata.mpasive.utilities.util.IClientUtils;
import com.nttdata.mpasive.utilities.util.ITransactionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;



@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements IAccountService{

    private final IAccountRepository accountRepository;
    private final IAccountUtils accountUtils;
    private final IClientUtils clientUtils;
    private final IAccountSpecificationsUtils accountSpecificationsUtils;
    private final ITransactionUtils transactionUtils;
    private final Constants constants;

    @Autowired
    ReactiveCircuitBreaker clientsServiceReactiveCircuitBreaker;


    @Override
    public Mono<AccountEntity> create(AccountCreateRequest account) {
        if (account.getClientId() == null)
            return Mono.error(new IllegalArgumentException("No id specified for account client"));

        if (!account.getClientId().isBlank()) {
            Mono<AccountEntity> createdAccount = findByIdClientService(account.getClientId())
                    .flatMap(retrievedClient -> accountToCreateGenericValidation(account, retrievedClient))
                    .flatMap(genericValidatedClient -> {
                        if (genericValidatedClient.getClientType().getDescription().contentEquals(constants.getClientPersonal())) {
                            return accountToCreatePersonalClientValidation(genericValidatedClient);
                        } else if (genericValidatedClient.getClientType().getDescription().contentEquals(constants.getClientEnterprise())) {
                            return accountToCreateEnterpriseClientValidation(account, genericValidatedClient);
                        } else {
                            return Mono.error(new EnterpriseLogicException("Not supported client type"));
                        }
                    })
                    .flatMap(validatedClient -> {
                        Client client = clientUtils.clientClientServiceDTOToClient(validatedClient);
                        AccountEntity accountToCreate = accountUtils.accountCreateRequestDTOToAccount(account, client);

                        if (accountToCreate.getAccountType().getDescription().contentEquals(constants.getAccountLongTerm()))
                            accountToCreate.getAccountSpecifications().setAllowedDayForTransaction(Integer.valueOf(constants.getTransactionFreeLongTermDay()));

                        return accountRepository.insert(accountToCreate);
                    })
                    .switchIfEmpty(Mono.error(new NoSuchElementException("Client does not exist")));

            return createdAccount;
        } else {
            return Mono.error(new IllegalArgumentException("Account does not contain client id"));
        }
    }

    @Override
    public Flux<AccountEntity> findAll() {
        Flux<AccountEntity> retrievedAccount = accountRepository.findAll();
        return retrievedAccount;
    }

    @Override
    public Mono<AccountEntity> findById(String id) {
        log.info("Start of operation to find an account by id");

        log.info("Retrieving account with id: [{}]", id.toUpperCase());
        Mono<AccountEntity> retrievedAccount = accountRepository.findById(id);
        log.info("Account with id: [{}] was retrieved successfully", id);

        log.info("End of operation to find an account by id");
        return retrievedAccount;
    }

    @Override
    public Flux<AccountEntity> findByClientId(String id) {
        Flux<AccountEntity> retrievedAccount = accountRepository.findAccountsByClientId(id);
        return retrievedAccount;
    }

    @Override
    public Mono<AccountEntity> findByAccountNumber(String number) {
        return accountRepository.findAll()
                .filter(accountEntity -> accountEntity.getAccountNumber().contentEquals(number))
                .take(1).single();
    }

    @Override
    public Mono<AccountEntity> update(AccountUpdateRequest accountDTO) {

        Mono<AccountEntity> updatedAccount = findById(accountDTO.getId())
                .flatMap(retrievedAccount -> accountToUpdateValidation(accountDTO, retrievedAccount))
                .flatMap(validatedAccount -> {
                    AccountEntity accountToUpdate = accountUtils.fillAccountWithAccountUpdateRequestDTO(validatedAccount, accountDTO);

                    Mono<AccountEntity> nestedUpdatedAccount = accountRepository.save(accountToUpdate);

                    return nestedUpdatedAccount;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        return updatedAccount;
    }

    @Override
    public Mono<AccountEntity> removeById(String id) {
        Mono<AccountEntity> removedAccount = findById(id)
                .flatMap(retrievedAccount -> accountRepository.deleteById(retrievedAccount.getId()).thenReturn(retrievedAccount));

        return removedAccount;
    }


    // client transactions ( Deposit - Withdrawal - Transfer in - Transfer out )
    @Override
    public Mono<AccountEntity> doTransaction(AccountDoTransactionRequest accountDoTransaction) {
        Mono<AccountEntity> updatedAccount = findById(accountDoTransaction.getId())
                .flatMap(retrievedAccount -> {
                    log.info("Validating transaction");
                    return transactionValidation(accountDoTransaction, retrievedAccount);
                })
                .flatMap(validatedAccount -> storeTransactionIntoAccount(accountDoTransaction, validatedAccount))
                .flatMap(transformedAccount -> {
                    if (accountDoTransaction.getTransaction().getType().contentEquals(constants.getTransactionTransferOut())) {
                        // Creates the operation for the transfer in
                        AccountDoTransactionRequest targetAccountTransaction = AccountDoTransactionRequest.builder()
                                .id(accountDoTransaction.getTargetId())
                                .transaction(TransactionDoTransactionRequest.builder()
                                        .amount(accountDoTransaction.getTransaction().getAmount())
                                        .type(constants.getTransactionTransferIn())
                                        .build())
                                .build();
                        log.info("Sending transaction to receiver account with id: [{}]", accountDoTransaction.getTargetId());
                        return doTransaction(targetAccountTransaction)
                                .flatMap(transferInAccount -> accountRepository.save(transformedAccount));
                    } else {
                        log.info("Saving transaction into account: [{}]", transformedAccount.toString());
                        return accountRepository.save(transformedAccount);
                    }
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        log.info("End to save a new account transaction for the account with id: [{}]", accountDoTransaction.getId());
        return updatedAccount;
    }

    // transactions in a certain account
    @Override
    public Flux<Transaction> findTransactionsByAccountId(String id) {
        Flux<Transaction> retrievedTransactions = findById(id)
                .filter(retrievedAccount -> retrievedAccount.getTransactions() != null)
                .flux()
                .flatMap(retrievedAccount -> Flux.fromIterable(retrievedAccount.getTransactions()));
        return retrievedTransactions;
    }

    // balance of certain account
    @Override
    public Mono<AccountFindBalancesResponse> findBalancesByAccountId(String numberAccount) {

        Mono<AccountFindBalancesResponse> retrievedBalances = findByAccountNumber(numberAccount)
                .flatMap(retrieveAccount ->{
                    AccountFindBalancesResponse accountFindBalancesResponse = accountUtils.accountToAccountFindBalancesResponseDTO(retrieveAccount);
                    return Mono.just(accountFindBalancesResponse);
                });

        return retrievedBalances;
    }

    // External validations
    @Override
    public Mono<ClientClientServiceResponse> findByIdClientService(String id) {

        String url = constants.getServicesPrefix() + constants.getServicesUrlGateway()
                + constants.getServicesPathClient() + "/getById/" + id;

        return clientsServiceReactiveCircuitBreaker.run(
                WebClient.create(url)
                        .get()
                        .retrieve()
                        .bodyToMono(ClientClientServiceResponse.class),
                throwable -> {
                    // return Mono.error(new CircuitBreakerException("CLIENT-SERVICE NO AVAILABLE"));
                    return Mono.just(new ClientClientServiceResponse());
                });

    }

    // #############################################################

    private Mono<AccountEntity> transactionValidation(AccountDoTransactionRequest accountToUpdateTransaction, AccountEntity accountInDatabase) {
        log.info("Account exists in database");

        if (accountInDatabase.getStatus().contentEquals(constants.getStatusBlocked())) {
            log.warn("Account have blocked status");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new ElementBlockedException("The account have blocked status"));
        }

        if (accountToUpdateTransaction.getTransaction().getType().contentEquals(constants.getTransactionWithdrawal()) ||
                accountToUpdateTransaction.getTransaction().getType().contentEquals(constants.getTransactionTransferOut())) {

            Double amountToTake = accountToUpdateTransaction.getTransaction().getAmount();
            if ( accountInDatabase.getTransactions().size() > (accountInDatabase.getAccountSpecifications().getMaximumNumberOfTransaction() + accountInDatabase.getFreeTransactionPerMonth())) {
                amountToTake = accountSpecificationsUtils.roundDouble(accountSpecificationsUtils.applyInterests(amountToTake, accountInDatabase.getAccountSpecifications().getTransactionCommission()), 2);
            }

            if (accountInDatabase.getCurrentBalance() < amountToTake) {
                log.info("Account has insufficient funds");
                log.warn("Proceeding to abort do transaction");
                return Mono.error(new IllegalArgumentException("The account has insufficient funds"));
            }
        }

        if (accountInDatabase.getAccountType().getDescription().contentEquals(constants.getAccountLongTerm()) &&
                !accountInDatabase.getAccountSpecifications().getAllowedDayForTransaction().equals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {
            log.info("Allowed day [{}] for transactions in this account does not match with current day of the month [{}]",
                    accountInDatabase.getAccountSpecifications().getAllowedDayForTransaction(),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            log.warn("Proceeding to abort do transaction");
            return Mono.error(new AccountLoginException("Allowed day for transactions in this account does not match with current day of the month"));
        }

        log.info("Transaction successfully validated");
        return Mono.just(accountInDatabase);
    }

    private Mono<AccountEntity> storeTransactionIntoAccount(AccountDoTransactionRequest accountDTO, AccountEntity accountInDatabase) {
        Double commission = 0.0;

        if (!accountDTO.getTransaction().getType().contentEquals(constants.getTransactionTransferIn())) {
            commission = accountSpecificationsUtils.roundDouble(accountSpecificationsUtils.calculateCommission(accountDTO.getTransaction().getAmount(), accountInDatabase.getAccountSpecifications().getTransactionCommission()), 2);
        }

        // Calculates the new current balance
        Double newCurrentBalance = accountSpecificationsUtils.roundDouble((accountDTO.getTransaction().getType().contentEquals(constants.getTransactionDeposit()) ||
                accountDTO.getTransaction().getType().contentEquals(constants.getTransactionTransferIn())) ?
                accountInDatabase.getCurrentBalance() + accountDTO.getTransaction().getAmount() - commission :
                accountInDatabase.getCurrentBalance() - accountDTO.getTransaction().getAmount() - commission, 2);
        accountInDatabase.setCurrentBalance(newCurrentBalance);

        // Creates the new transaction
        Transaction transaction = transactionUtils.transactionDoTransactionRequestToTransaction(accountDTO.getTransaction());
        transaction.setTime(new Date());
        transaction.setCommission(commission);
        transaction.setFinalBalance(newCurrentBalance);

        ArrayList<Transaction> transactions = accountInDatabase.getTransactions() == null ? new ArrayList<>() : accountInDatabase.getTransactions();
        transactions.add(transaction);

        accountInDatabase.setTransactions(transactions);

        return Mono.just(accountInDatabase);
    }

    private Mono<ClientClientServiceResponse> accountToCreateGenericValidation(AccountCreateRequest accountToCreate, ClientClientServiceResponse clientServiceResponse) {

        if (clientServiceResponse.getId() == null)
            return Mono.error(new ClientNotFoundException("The client id is null or does not exist"));

        // Check the status of the client
        if (clientServiceResponse.getStatus().contentEquals(constants.getStatusBlocked()))
            return Mono.error(new ElementBlockedException("The client have blocked status"));

        return Mono.just(clientServiceResponse);
    }

    /**
     * Validation of the account personal with respect to the client obtained
     *
     * @param clientServiceResponse: client obtained from the DB
     * @return Mono<ClientFromClientServiceResponse> if the validation is correct
     */
    private Mono<ClientClientServiceResponse> accountToCreatePersonalClientValidation(ClientClientServiceResponse clientServiceResponse) {

        if (clientServiceResponse.getClientType().getType().contentEquals(constants.getClientPersonalNormal())) {
            log.warn("Standard clients can not create vip accounts");
            log.warn("Proceeding to abort create account");
            return findByClientId(clientServiceResponse.getId())
                    .filter(retrievedAccount -> retrievedAccount.getStatus().contentEquals(constants.getStatusActive()))
                    .hasElements()
                    .flatMap(haveAnAccount -> {
                        if (Boolean.TRUE.equals(haveAnAccount))
                            return Mono.error(new PersonalLogicException("Client personal have more than one account"));
                        else
                            return Mono.just(clientServiceResponse);
                    });
        } else if (clientServiceResponse.getClientType().getType().contentEquals(constants.getClientPersonalVip())) {
            return findByIdCreditCardService(clientServiceResponse);
        }
        return Mono.error(new PersonalLogicException("Client personal have more than one account"));
    }

    public Mono<ClientClientServiceResponse> findByIdCreditCardService(ClientClientServiceResponse clientServiceResponse) {

        String url = constants.getServicesPrefix() + constants.getServicesUrlGateway()
                + constants.getServicesPathCreditCard() + "/findClientById/" + clientServiceResponse.getId();

        return clientsServiceReactiveCircuitBreaker.run(
                WebClient.create(url)
                        .get()
                        .retrieve()
                        .bodyToFlux(CreditCardCreditCardResponse.class)
                        .hasElements()
                        .flatMap(haveAnCreditCard -> {
                            if (Boolean.TRUE.equals(haveAnCreditCard))
                                return Mono.just(clientServiceResponse);
                            else
                                return Mono.error(new CreditCardException("Client personal VIP don't have a credit card"));
                        }),
                throwable -> {
                    // return Mono.error(new CircuitBreakerException("CLIENT-SERVICE NO AVAILABLE"));
                    return Mono.error(new CreditCardException("Client personal VIP don't have a credit card"));
                });

    }

    /**
     * Validation of the account enterprise with respect to the client obtained
     *
     * @param clientServiceResponse: client obtained from the DB
     * @return Mono<ClientFromClientServiceResponse> if the validation is correct
     */
    private Mono<ClientClientServiceResponse> accountToCreateEnterpriseClientValidation(AccountCreateRequest accountCreateRequest,ClientClientServiceResponse clientServiceResponse) {

        System.out.println(accountCreateRequest.toString());
        System.out.println(clientServiceResponse.toString());

        if (!accountCreateRequest.getAccountType().getDescription().contentEquals(constants.getAccountCurrent()))
            return Mono.error(new EnterpriseLogicException("Account is not of current type"));

        if ( clientServiceResponse.getClientType().getType().contentEquals(constants.getClientEnterpriseNormal()))
            return Mono.just(clientServiceResponse);

        if ( clientServiceResponse.getClientType().getType().contentEquals(constants.getClientEnterprisePyme()))
            return findByIdCreditCardService(clientServiceResponse);

        return Mono.error(new EnterpriseLogicException("No supported"));

    }

    private Mono<AccountEntity> accountToUpdateValidation(AccountUpdateRequest accountForUpdate, AccountEntity accountInDatabase) {
        log.info("Account exists in database");

        if (accountInDatabase.getClient().getStatus().contentEquals(constants.getStatusBlocked())) {
            return Mono.error(new ElementBlockedException("The client have blocked status"));
        }

        log.info("Account successfully validated");
        return Mono.just(accountInDatabase);
    }

    @Override
    public Flux<ClientClientServiceResponse> clientTest(){
        String url = constants.getServicesPrefix() + constants.getServicesUrlGateway()
                + constants.getServicesPathClient() + "/findAll";

        return clientsServiceReactiveCircuitBreaker.run(
                WebClient.create(url)
                        .get()
                        .retrieve()
                        .bodyToFlux(ClientClientServiceResponse.class),

                throwable -> {
                    // return Mono.error(new CircuitBreakerException("CLIENT-SERVICE NO AVAILABLE"));
                    return Flux.just(new ClientClientServiceResponse());
                });

    }

    //endregion
}
