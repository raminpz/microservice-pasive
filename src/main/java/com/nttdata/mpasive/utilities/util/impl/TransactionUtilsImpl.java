package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.models.dto.Transaction;
import com.nttdata.mpasive.models.request.TransactionDoTransactionRequest;
import com.nttdata.mpasive.utilities.util.ITransactionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
public class TransactionUtilsImpl implements ITransactionUtils {

    @Override
    public Transaction transactionDoTransactionRequestToTransaction(TransactionDoTransactionRequest transactionDTO) {
        return Transaction.builder()
                .operationNumber(UUID.randomUUID().toString())
                .type(transactionDTO.getType())
                .amount(transactionDTO.getAmount())
                .build();
    }

    @Override
    public Transaction operationCommissionResponseDTOToOperation(OperationCommissionResponse operationDTO) {
        return Transaction.builder()
                .operationNumber(operationDTO.getOperationNumber())
                .amount(operationDTO.getAmount())
                .time(operationDTO.getTime())
                .commission(operationDTO.getCommission())
                .build();
    }

    @Override
    public TransactionDoTransactionRequest operationToOperationDoOperationRequestDTO(Transaction operation) {
        return TransactionDoTransactionRequest.builder()
                .type(operation.getType())
                .amount(operation.getAmount())
                .build();
    }

    @Override
    public OperationCommissionResponse operationToOperationCommissionResponseDTO(Transaction operation) {
        return OperationCommissionResponse.builder()
                .operationNumber(operation.getOperationNumber())
                .amount(operation.getAmount())
                .time(operation.getTime())
                .commission(operation.getCommission())
                .build();
    }
}
