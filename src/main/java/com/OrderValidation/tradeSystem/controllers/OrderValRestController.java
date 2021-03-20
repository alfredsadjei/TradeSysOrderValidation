package com.OrderValidation.tradeSystem.controllers;

import com.OrderValidation.tradeSystem.DTOs.MarketData;
import com.OrderValidation.tradeSystem.DAOs.MarketDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/md")
public class OrderValRestController {

    @Autowired
    MarketDataRepo marketDataRepo;

    //Endpoint that receives market data updates
    @PostMapping()
    public ResponseEntity<List<MarketData>> getMarketData(@RequestBody List<MarketData> marketData){

        //Sets the new market data whenever an update occurs
        marketDataRepo.setDataRepository(marketData);

        return new ResponseEntity<>(marketData,HttpStatus.OK);

    }
}
