package com.nttdata.mpasive.utilities.util;

public interface ITransactionUtils {

    Transaction transactionDoTransactionRequestToTransaction(TransactionDoTransactionRequest operationDTO);
    Transaction operationCommissionResponseDTOToOperation(OperationCommissionResponse operationDTO);
    TransactionDoTransactionRequest operationToOperationDoOperationRequestDTO(Transaction operation);
    OperationCommissionResponse operationToOperationCommissionResponseDTO(Transaction operation);
}
