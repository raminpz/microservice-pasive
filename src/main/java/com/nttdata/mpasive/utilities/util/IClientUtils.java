package com.nttdata.mpasive.utilities.util;

public interface IClientUtils {

    Client clientClientServiceDTOToClient(ClientFromClientServiceResponse customerDTO);
    ClientFromClientServiceResponse customerToCustomerCustomerServiceResponseDTO(Client customer);
}
