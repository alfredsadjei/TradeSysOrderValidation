package com.OrderValidation.tradeSystem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.validate.ProductOrder;

public class OrderSerializer {
    private final ProductOrder productOrder;

    public OrderSerializer(ProductOrder productOrder){
        this.productOrder = productOrder;
    }

    public String serialize (){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(productOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
