package com.OrderValidation.tradeSystem.services;


import com.OrderValidation.tradeSystem.DAOs.MarketDataRepo;
import com.OrderValidation.tradeSystem.DTOs.MarketData;
import com.order.validate.PostOrderResponse;
import com.order.validate.ProductOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
public class OrderValidationService {

    //Get market data from the repo
    @Autowired
    MarketDataRepo marketDataRepo;

    public boolean validate(ProductOrder productOrder){

        boolean result;
        //Find market data on specific product
        MarketData orderMD = marketDataRepo.getDataRepository()
                .stream().filter(md -> md.getTICKER().equals(productOrder.getProductName())).findFirst().get();


        //order val rules
        if (productOrder.getSide().equalsIgnoreCase("BUY")){

            //TODO: first make call to db to check if client has enough funds to buy

            if (productOrder.getQuantity() > orderMD.getBUY_LIMIT() ||
                    (orderMD.getLAST_TRADED_PRICE() - productOrder.getPrice()) > orderMD.getMAX_PRICE_SHIFT()){

                result = false;
            }else{
                result = true;
            }

        }else{

            //TODO: first make call to db and check of client has enough of that product to sell

            if (productOrder.getQuantity() > orderMD.getSELL_LIMIT() ||
                    (productOrder.getPrice() - orderMD.getLAST_TRADED_PRICE() ) > orderMD.getMAX_PRICE_SHIFT()){
                result = false;
            }else{
                result = true;
            }
        }



        //if order passes all order val rules return true else return false
        //returning true for testing purposes
        //TODO: return result;
        return true;
    }

}
