package com.OrderValidation.tradeSystem.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/md")
public class OrderValRestController {


    @GetMapping(value = "/{ticker}")
    public ResponseEntity<?> getProductMarketData(@PathVariable("ticker") String ticker){

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getMarketData(){

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
