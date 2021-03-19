package com.OrderValidation.tradeSystem.config;

import com.OrderValidation.tradeSystem.OrderValRedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfiguration {

    @Bean
    public OrderValRedisClient redisClientFactory(){
        return new OrderValRedisClient("redis-17849.c59.eu-west-1-2.ec2.cloud.redislabs.com",17849);
    }
}
