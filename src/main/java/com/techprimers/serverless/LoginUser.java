package com.techprimers.serverless;

import java.io.IOException;
import java.util.function.Function;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techprimers.serverless.modal.UserInfo;

public class LoginUser implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
	private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			   												   .withRegion(Regions.US_EAST_2)
			   												   .build();

	@Override
	public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent input) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

		response.setStatusCode(200);

		try {
			UserInfo userInput = validateInputs(input.getBody());

			validateLoginCreds(userInput);

			response.setBody("{\"message\" : \"User logged in successfully.\"}");
		} catch (IOException e) {
			e.printStackTrace();

			response.setStatusCode(500);
			response.setBody("{\"message\" : \"Failed to login: " + e.getMessage() + "\"}");
		}

		return response;
	}

	private UserInfo validateInputs(String requestContent) throws IOException {
		ObjectMapper mapper    = new ObjectMapper();
		UserInfo     userInput = mapper.readValue(requestContent.getBytes(), UserInfo.class);

		if (userInput.getUserId() == null || userInput.getUserId().trim().length() < 3)
			throw new IOException("Invalid login credentials.");

		if (userInput.getPassword() == null || userInput.getPassword().trim().length() < 10)
			throw new IOException("Invalid login credentials.");

		return userInput;
	}

	private void validateLoginCreds(UserInfo newUserInfo) throws IOException {
		DynamoDBMapper dbMapper  = new DynamoDBMapper(client);

		// dbMapper.save(newUserInfo);
		UserInfo storedInfo = dbMapper.load(UserInfo.class, newUserInfo.getUserId());

		if (storedInfo == null)
			throw new IOException("Invalid login credentials.");

		if (!storedInfo.getPassword().equals(newUserInfo.getPassword()))
			throw new IOException("Invalid login credentials.");
	}
}
