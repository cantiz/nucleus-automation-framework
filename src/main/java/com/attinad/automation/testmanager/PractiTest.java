package com.attinad.automation.testmanager;

import static com.attinad.automation.testmanager.PractiTestConstants.PASSWORD;
import static com.attinad.automation.testmanager.PractiTestConstants.URL_CREATE_TEST_INSTANCE;
import static com.attinad.automation.testmanager.PractiTestConstants.URL_CREATE_TEST_RUN;
import static com.attinad.automation.testmanager.PractiTestConstants.URL_CREATE_TEST_SET;
import static com.attinad.automation.testmanager.PractiTestConstants.URL_GET_TEST_STEPS;
import static com.attinad.automation.testmanager.PractiTestConstants.USERNAME;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;

import com.attinad.automation.common.HttpClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PractiTest {

	public static String getTestSteps(String testId) throws ClientProtocolException, IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		headers.put("Authorization", USERNAME + ":" + PASSWORD);

		String uri = URL_GET_TEST_STEPS + "/?test-ids=" + testId;
		String response = HttpClient.getInstance().get(uri, headers);

		return response;
	}

	public static String createTestSet(String testSetName)
			throws AuthenticationException, ClientProtocolException, IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		headers.put("Authorization", USERNAME + ":" + PASSWORD);

		String bodyJson = "{\"data\": { \"type\": \"sets\", \"attributes\": {\"name\": \"" + testSetName + "\"}}}";
		String response = HttpClient.getInstance().post(URL_CREATE_TEST_SET, headers, bodyJson);

		JsonObject jsn = new JsonParser().parse(response).getAsJsonObject();
		try {
			return jsn.getAsJsonObject("data").get("id").getAsString();
		} catch (NullPointerException e) {
			return null;
		}
	}

	public static String createTestInstance(String testId, String testSetId)
			throws ClientProtocolException, IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		headers.put("Authorization", USERNAME + ":" + PASSWORD);

		String bodyJson = "{\"data\": { \"type\": \"instances\", \"attributes\": {\"test-id\": " + testId
				+ ", \"set-id\": " + testSetId + "}}}";
		String response = HttpClient.getInstance().post(URL_CREATE_TEST_INSTANCE, headers, bodyJson);

		JsonObject jsn = new JsonParser().parse(response).getAsJsonObject();
		try {
			return jsn.getAsJsonObject("data").get("id").getAsString();
		} catch (NullPointerException e) {
			return null;
		}

	}

	public static String createTestRun(String instanceId, JsonArray scenarioResult)
			throws ClientProtocolException, IOException {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-type", "application/json");
		headers.put("Authorization", USERNAME + ":" + PASSWORD);

		String bodyJson = getTestRunRequestBody(instanceId, scenarioResult);
		String response = HttpClient.getInstance().post(URL_CREATE_TEST_RUN, headers, bodyJson);
		JsonObject jsn = new JsonParser().parse(response).getAsJsonObject();
		try {
			return jsn.getAsJsonObject("data").get("id").getAsString();
		} catch (NullPointerException e) {
			return null;
		}

	}

	private static String getTestRunRequestBody(String testInstanceId, JsonArray scenarioResult) {
		JsonObject stepsData = new JsonObject();
		stepsData.add("data", scenarioResult);

		JsonObject data = new JsonObject();
		data.addProperty("type", "instances");
		data.add("attributes", new JsonParser().parse("{\"instance-id\": " + testInstanceId + "}").getAsJsonObject());
		data.add("steps", stepsData);

		JsonObject requestBody = new JsonObject();
		requestBody.add("data", data);

		return requestBody.toString();

	}

}
