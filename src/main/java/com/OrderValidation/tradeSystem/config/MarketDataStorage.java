package com.OrderValidation.tradeSystem.config;


import com.OrderValidation.tradeSystem.DAOs.MarketDataRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



//Creating a bean to store market data received from the exchanges
@Configuration
public class MarketDataStorage {

    @Bean
    public MarketDataRepo getMarketDataRepo(){
        return new MarketDataRepo();
    }

}
