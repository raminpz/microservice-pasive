package com.nttdata.mpasive.utilities.constants;

import lombok.Getter;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {

    // ## status
    @Value("${constants.status.blocked}")
    private String statusBlocked;
    @Value("${constants.status.active}")
    private String statusActive;

    // ## savings-account
    @Value("${constants.account.savings.description}")
    private String accountSavings;
    @Value("${constants.account.savings.type.normal}")
    private String accountSavingsNormal;
    @Value("${constants.account.savings.type.vip}")
    private String accountSavingsVip;

    // ## current-account
    @Value("${constants.account.current.description}")
    private String accountCurrent;
    @Value("${constants.account.current.type.normal}")
    private String accountCurrentNormal;
    @Value("${constants.account.current.type.pyme}")
    private String accountCurrentPyme;

    // ## long-term-account
    @Value("${constants.account.long-term.description}")
    private String accountLongTerm;
    @Value("${constants.account.long-term.type.normal}")
    private String accountLongTermNormal;

    // ## transactions available
    @Value("${constants.transaction.free.any-account}")
    private String transactionFreeAnyAccount;
    @Value("${constants.transaction.free.long-term-day}")
    private String transactionFreeLongTermDay;
    @Value("${constants.transaction.type.deposit}")
    private String transactionDeposit;
    @Value("${constants.transaction.type.withdrawal}")
    private String transactionWithdrawal;
    @Value("${constants.transaction.type.transfer-in}")
    private String transactionTransferIn;
    @Value("${constants.transaction.type.transfer-out}")
    private String transactionTransferOut;

    // ## client personal
    @Value("${constants.client.personal.description}")
    private String clientPersonal;
    @Value("${constants.client.personal.type.normal}")
    private String clientPersonalNormal;
    @Value("${constants.client.personal.type.vip}")
    private String clientPersonalVip;

    // ## client enterprise
    @Value("${constants.client.enterprise.description}")
    private String clientEnterprise;
    @Value("${constants.client.enterprise.type.normal}")
    private String clientEnterpriseNormal;
    @Value("${constants.client.enterprise.type.pyme}")
    private String clientEnterprisePyme;

    // ## service addresses
    @Value("${constants.services.prefix}")
    private String servicesPrefix;
    @Value("${constants.services.url.gateway}")
    private String servicesUrlGateway;
    @Value("${constants.services.url.client}")
    private String servicesUrlClient;
    @Value("${constants.services.url.passive}")
    private String servicesUrlPassive;
    @Value("${constants.services.url.active}")
    private String servicesUrlActive;
    @Value("${constants.services.path.client}")
    private String servicesPathClient;
    @Value("${constants.services.path.passive}")
    private String servicesPathPassive;
    @Value("${constants.services.path.active}")
    private String servicesPathActive;
}
