package com.techprimers.serverless;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.techprimers.serverless.modal.UserInfo;

@Component
public class Hello implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
															   .withRegion(Regions.US_EAST_2)
															   .build();

    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent input) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setStatusCode(200);
        responseEvent.setBody("[\"Hello! Reached the Spring Cloud Function with message: " + input.getPathParameters() + "\"]");

        saveDummy();

        return responseEvent;
    }

    private void saveDummy() {
    	UserInfo userInfo = new UserInfo("gurusrin", "Gurumoorthy", "Srinivasan");

    	DynamoDBMapper mapper = new DynamoDBMapper(client);

    	mapper.save(userInfo);
    }
}
