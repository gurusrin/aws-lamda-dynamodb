/**
 * 
 */
package com.techprimers.serverless.modal;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * @author GURUMOORTHYSRINIVASA
 *
 */
@DynamoDBTable(tableName = "user_info")
public class UserInfo {
	private String userId;
	private String firstName;
	private String lastName;
	private String password;

	/**
	 * 
	 */
	public UserInfo() {
	}

	/**
	 * @param userId
	 * @param firstName
	 * @param lastName
	 */
	public UserInfo(String userId, String firstName, String lastName) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the userId
	 */
	@DynamoDBHashKey(attributeName = "userId")
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the firstName
	 */
	@DynamoDBAttribute(attributeName = "firstName")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@DynamoDBAttribute(attributeName = "lastName")
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
