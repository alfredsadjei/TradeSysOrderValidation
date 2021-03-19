package com.OrderValidation.tradeSystem;


import com.OrderValidation.tradeSystem.exceptions.RedisConnectionFailedException;
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


@Endpoint
public class OrderValidationController {
    private static final String NAMESPACE_URI = "http://www.order.com/validate";

    @Autowired
    private OrderValRedisClient orderValRedisClient;


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
        newProductOrderResponse.setPrice(request.getPrice());
        newProductOrderResponse.setQuantity(request.getQuantity());
        newProductOrderResponse.setSide(request.getSide());
        newProductOrderResponse.setDate(request.getDate());

        //The response (of type PostOrderResponse) then returns the new order object
        //with the same id as the request order object confirming that the product order
        // with that particular ID was received.
        response.setOrder(newProductOrderResponse);

        //serialize data and send to redis server
        OrderSerializer orderSerializer = new OrderSerializer(newProductOrderResponse);

        Jedis redisConnector;

        try {
            //connect to redis server and send order data
            redisConnector = orderValRedisClient.connect();
        }catch (RedisConnectionFailedException rcx){
            throw new RedisConnectionFailedException("Connection to redis server failed.");
        }

        //create orderCreated queue and push order onto it
        redisConnector.lpush("orderCreated",orderSerializer.serialize());

        redisConnector.close();

        return response;

    }

}
