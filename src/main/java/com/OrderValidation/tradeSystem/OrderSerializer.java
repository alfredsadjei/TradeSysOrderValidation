package com.OrderValidation.tradeSystem;

import com.order.validate.ProductOrder;

public class OrderSerializer {
    private final ProductOrder productOrder;

    public OrderSerializer(ProductOrder productOrder){
        this.productOrder = productOrder;
    }

    public String serialize (){
        return "{" +
                "id: "+this.productOrder.getID()+","+
                "name: "+this.productOrder.getProductName()+","+
                "price: "+this.productOrder.getPrice()+","+
                "quantity: "+this.productOrder.getQuantity()+","+
                "side: "+this.productOrder.getSide()+","+
                "date: "+this.productOrder.getDate()+
                "}";
    }
}
