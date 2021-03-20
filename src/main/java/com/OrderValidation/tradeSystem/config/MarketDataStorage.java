package com.OrderValidation.tradeSystem.config;


import com.OrderValidation.tradeSystem.DAOs.MarketDataRepo;
import com.OrderValidation.tradeSystem.DTOs.MarketData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


//Creating a bean to store market data received from the exchanges
@Configuration
public class MarketDataStorage {

    //TODO: populate list with initial market data
    // since marketData generated on exchange update
    // List<MarketData> initData;

    @Bean
    public MarketDataRepo getMarketDataRepo(){

        return new MarketDataRepo();
    }

}
