package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.models.dto.Client;
import com.nttdata.mpasive.utilities.util.IClientTypeUtils;
import com.nttdata.mpasive.utilities.util.IClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ClientUtilsImpl implements IClientUtils {

    private final IClientTypeUtils clientTypeUtils;

    @Override
    public Client clientClientServiceDTOToClient(ClientFromClientServiceResponse clientDTO) {
        return Client.builder()
                .id(clientDTO.getId())
                .clientType(clientTypeUtils.clientTypeClientServiceResponseDTOToClientType(clientDTO.getClientType()))
                .status(clientDTO.getStatus())
                .build();
    }

    @Override
    public ClientFromClientServiceResponse customerToCustomerCustomerServiceResponseDTO(Client customer) {
        return ClientFromClientServiceResponse.builder()
                .id(customer.getId())
                .clientType(clientTypeUtils.clientTypeToClientTypeClientServiceResponseDTO(customer.getClientType()))
                .status(customer.getStatus())
                .build();
    }
}
