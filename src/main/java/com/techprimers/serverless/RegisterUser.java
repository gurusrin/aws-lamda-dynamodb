/**
 * 
 */
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

/**
 * @author GURUMOORTHYSRINIVASA
 *
 */
public class RegisterUser implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>  {
	private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
			   												   .withRegion(Regions.US_EAST_2)
			   												   .build();

	@Override
	public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent input) {
		APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

		response.setStatusCode(200);

		try {
			UserInfo userInput = validateUserInput(input.getBody());

			saveUser(userInput);

			response.setBody("{\"message\" : \"Successfully registered the new user.\"}");
		} catch (Exception e) {
			e.printStackTrace();

			response.setStatusCode(500);
			response.setBody("{\"message\" : \"Failed to persist new user: " + e.getMessage() + "\"}");
		}

		return response;
	}

	private UserInfo validateUserInput(String requestContent) throws IOException {
		ObjectMapper objMapper = new ObjectMapper();
		UserInfo     userInput = objMapper.readValue(requestContent.getBytes(), UserInfo.class);

		if (userInput.getUserId() == null)
			throw new IOException("Attribute userId is required.");

		if (   userInput.getFirstName() == null
			|| userInput.getFirstName().trim().length() == 0)
			throw new IOException(  "Attribute firstName is not provided."
								  + " All attributes are mandatory.");

		if (   userInput.getLastName() == null
			|| userInput.getLastName().trim().length() == 0)
			throw new IOException(  "Attribute lastName is not provided."
								  + " All attributes are mandatory.");

		if (   userInput.getPassword() == null
			|| userInput.getPassword().trim().length() == 0)
			throw new IOException(  "Attribute password is not provided."
								  + " All attributes are mandatory.");

		if (   userInput.getUserId().trim().length() < 3
			|| userInput.getPassword().trim().length() < 10)
			throw new IOException(  "User ID should have at least 3 characters"
								  + " and Password should have at least"
								  + " 10 characters.");

		return userInput;
	}

	private void saveUser(UserInfo newUserInfo) throws IOException {
		DynamoDBMapper dbMapper  = new DynamoDBMapper(client);

		dbMapper.save(newUserInfo);
	}
}
