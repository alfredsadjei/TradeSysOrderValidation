package com.OrderValidation.tradeSystem;


import com.order.validate.ObjectFactory;
import com.order.validate.PostOrderResponse;
import com.order.validate.ProductOrder;
import com.order.validate.PostOrderRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class OrderValidationController {
    private static final String NAMESPACE_URI = "http://www.order.com/validate";



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

        //The response (of type PostOrderResponse) then returns the new order object
        //with the same id as the request order object confirming that the product order
        // with that particular ID was received.
        response.setOrder(newProductOrderResponse);

        return response;
    }

}
