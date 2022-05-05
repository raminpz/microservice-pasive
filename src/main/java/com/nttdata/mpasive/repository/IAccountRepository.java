package com.nttdata.mpasive.repository;

import com.nttdata.mpasive.models.entity.AccountEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IAccountRepository extends ReactiveMongoRepository<AccountEntity, String> {


    Flux<AccountEntity> findAccountsByClientId(String id);
}
