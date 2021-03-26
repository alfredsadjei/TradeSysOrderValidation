package com.OrderValidation.tradeSystem.services;


import com.OrderValidation.tradeSystem.DAOs.MarketDataRepo;
import com.OrderValidation.tradeSystem.DTOs.MarketData;
import com.order.validate.ProductOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class OrderValidationService {

    private String exchanges;
    //Get market data from the repo
    @Autowired
    MarketDataRepo marketDataRepo;


    public boolean validate(ProductOrder productOrder){

        System.out.println("Hi");
        boolean result = false;

        //Find market data on specific product
        MarketData orderMD1 = marketDataRepo.getExchange1DataRepository()
                .stream()
                .filter(md -> md.getTICKER().equalsIgnoreCase(productOrder.getProductName()))
                .findFirst()
                .get();


        MarketData orderMD2 = marketDataRepo.getExchange2DataRepository()
                .stream()
                .filter(md -> md.getTICKER().equalsIgnoreCase(productOrder.getProductName()))
                .findFirst()
                .get();

        //order val rules
        if (isValidOrder(productOrder,orderMD1) && isValidOrder(productOrder,orderMD2)){
            //valid on both exchanges
            this.exchanges = "both";
            result = true;
        }else if (!isValidOrder(productOrder,orderMD1) && isValidOrder(productOrder,orderMD2)){
            //valid on exchange 2
            this.exchanges = "ex2";
            result = true;
        }else if (isValidOrder(productOrder,orderMD1) && !isValidOrder(productOrder,orderMD2)){
            //valid on exchange 1
            this.exchanges = "ex1";
            result = true;
        }

        //if order passes all order val rules return true else return false
        return result;
    }

    private boolean isQuantityValid( ProductOrder order, MarketData md){
        boolean isValid;

        if(order.getSide().equalsIgnoreCase("BUY")){
            isValid = !(order.getQuantity() > md.getBUY_LIMIT());
        }else{
            isValid = !(order.getQuantity() > md.getSELL_LIMIT());
        }

        return isValid;
    }

    private boolean isPriceReasonable( ProductOrder order, MarketData md){
        boolean isReasonable;

        if(order.getSide().equalsIgnoreCase("BUY")){
            isReasonable = !((md.getLAST_TRADED_PRICE() - order.getPrice()) > md.getMAX_PRICE_SHIFT());
        }else{
            isReasonable = !((order.getPrice() - md.getLAST_TRADED_PRICE() ) > md.getMAX_PRICE_SHIFT());
        }

        return isReasonable;
    }

    private boolean hasEnoughResources(ProductOrder order){
        boolean enoughResources;

        if(order.getSide().equalsIgnoreCase("BUY")){
            enoughResources = order.getFunds() > (order.getQuantity() * order.getPrice());
        }else{
            enoughResources = order.getQuantityOwned() > order.getQuantity();
        }

        return enoughResources;
    }

    private boolean isValidOrder( ProductOrder order, MarketData md){
        return isPriceReasonable(order, md) && isQuantityValid(order, md) && hasEnoughResources(order);
    }

    public String getExchange() {
        return exchanges;
    }



}
