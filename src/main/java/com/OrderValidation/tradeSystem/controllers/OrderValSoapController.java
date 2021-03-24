package com.OrderValidation.tradeSystem.controllers;


import com.OrderValidation.tradeSystem.OrderValidation;
import com.OrderValidation.tradeSystem.utils.OrderSerializer;
import com.OrderValidation.tradeSystem.utils.OrderValRedisClient;
import com.OrderValidation.tradeSystem.exceptions.RedisConnectionFailedException;
import com.OrderValidation.tradeSystem.services.OrderValidationService;
import com.order.validate.ObjectFactory;
import com.order.validate.PostOrderResponse;
import com.order.validate.ProductOrder;
import com.order.validate.PostOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import redis.clients.jedis.Jedis;

import java.io.IOException;


@Endpoint
public class OrderValSoapController {
    private static final String NAMESPACE_URI = "http://www.order.com/validate";

    @Autowired
    private OrderValRedisClient orderValRedisClient;

    @Autowired
    private OrderValidationService ovs;



    @PayloadRoot(namespace = NAMESPACE_URI , localPart = "postOrderRequest" )
    @ResponsePayload
    public PostOrderResponse postOrder (@RequestPayload PostOrderRequest request) {

        //Creating a new PostOrderResponseObject
        PostOrderResponse response = new PostOrderResponse();

        //ObjectFactory creates a new object of type ProductOrder
        ObjectFactory productOrderFactory = new ObjectFactory();
        ProductOrder newProductOrderResponse = productOrderFactory.createProductOrder();


        //ID of the new productOrder is set to the ID of the request productOrder
        newProductOrderResponse.setID(request.getID());
        newProductOrderResponse.setProductName(request.getProductName());
        newProductOrderResponse.setClientId(request.getClientId());
        newProductOrderResponse.setFunds(request.getFunds());
        newProductOrderResponse.setQuantityOwned(request.getQuantityOwned());
        newProductOrderResponse.setPrice(request.getPrice());
        newProductOrderResponse.setQuantity(request.getQuantity());
        newProductOrderResponse.setSide(request.getSide());
        newProductOrderResponse.setDate(request.getDate());


        //Check if order is valid
        if (!ovs.validate(newProductOrderResponse)){
            //set status failed
            newProductOrderResponse.setStatus("INVALID");

            response.setOrder(newProductOrderResponse);

            return response;
        }

        //The response (of type PostOrderResponse) then returns the new order object
        //with the same id as the request order object confirming that the product order
        // with that particular ID was received.
        //TODO:Set status to SUCCESS?
        newProductOrderResponse.setStatus("VALID");
        response.setOrder(newProductOrderResponse);

        //serialize data for redis server
        OrderSerializer orderSerializer = new OrderSerializer(newProductOrderResponse);

        Jedis redisConnector;
        try {
            //connect to redis server and send order data
            redisConnector = orderValRedisClient.connect();
        }catch (RedisConnectionFailedException rcx){
            throw new RedisConnectionFailedException("Connection to redis server failed.");
        }

        //create orderCreated channel publish order onto it
        redisConnector.publish("orderCreatedT",orderSerializer.serialize());


        redisConnector.close();

        return response;

    }

}
