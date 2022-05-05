package com.nttdata.mpasive.models.dto;


import lombok.*;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Client {

    private String id;
    private RedisProperties.ClientType clientType;
    private String status;
    private PersonInfo personInfo; // information personal
    private EnterpriseInfo enterpriseInfo;
}
