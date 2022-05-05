package com.nttdata.mpasive.utilities.util;

public interface IClientTypeUtils {

    ClientTypeClientServiceResponse clientTypeToClientTypeClientServiceResponseDTO(ClientType customerType);
    ClientType clientTypeClientServiceResponseDTOToClientType(ClientTypeClientServiceResponse customerTypeDTO);
}
