package com.nttdata.mpasive.utilities.util.impl;

import com.nttdata.mpasive.models.dto.ClientType;
import com.nttdata.mpasive.models.response.ClientTypeClientServiceResponse;
import com.nttdata.mpasive.utilities.util.IClientTypeUtils;
import org.springframework.stereotype.Component;


@Component
public class ClientTypeUtilsImpl implements IClientTypeUtils {

    @Override
    public ClientTypeClientServiceResponse clientTypeToClientTypeClientServiceResponseDTO(ClientType clientType) {
        return ClientTypeClientServiceResponse.builder()
                .description(clientType.getDescription())
                .type(clientType.getType())
                .build();
    }

    @Override
    public ClientType clientTypeClientServiceResponseDTOToClientType(ClientTypeClientServiceResponse clientTypeDTO) {
        return ClientType.builder()
                .description(clientTypeDTO.getDescription())
                .type(clientTypeDTO.getType())
                .build();
    }
}
