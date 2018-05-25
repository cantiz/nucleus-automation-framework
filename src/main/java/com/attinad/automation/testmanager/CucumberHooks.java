package com.attinad.automation.testmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import com.attinad.automation.testmanager.PractiTest;
import com.attinad.automation.testmanager.PractiTestConstants;
import com.attinad.automation.testmanager.PractiTestVariables;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cucumber.api.Scenario;

public class CucumberHooks {
	
	public static void start() throws AuthenticationException, ClientProtocolException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
		Date today = new Date();
		String testSetName = "Automation Run-" + dateFormat.format(today);
		PractiTestVariables.TESTSETID = PractiTest.createTestSet(testSetName);

	}

	public static void beforeFirstScenario(Scenario scenario) throws ClientProtocolException, IOException {
		PractiTestVariables.TESTID = scenario.getId().split(":")[0];

		// create Test instance in PractiTest
		PractiTestVariables.TESTINSTANCEID = PractiTest.createTestInstance(PractiTestVariables.TESTID,
				PractiTestVariables.TESTSETID);

		File file = new File(PractiTestConstants.TEST_RESULT_FILE);
		if (!file.createNewFile()) {
			FileWriter writer = new FileWriter(file);
			writer.write("");
			writer.close();
		}
	}

	public static void afterAllScenario(Scenario scenario) throws IOException {
		File file = new File(PractiTestConstants.TEST_RESULT_FILE);
		FileWriter writer = new FileWriter(file, true);
		writer.write(scenario.getStatus() + "\n");
		writer.close();

	}

	public static void afterLastScenario(Scenario scenario) throws ClientProtocolException, IOException {

		BufferedReader abc = new BufferedReader(new FileReader(PractiTestConstants.TEST_RESULT_FILE));
		List<String> status = new ArrayList<String>();
		
		// Get the test result into a List
		String line;
		while ((line = abc.readLine()) != null) {
			status.add(line);
		}
		abc.close();
		
		// Get the test steps from PractiTest and convert it into a JsonObject
		String steps = PractiTest.getTestSteps(PractiTestVariables.TESTID);
		JsonObject jsn = new JsonParser().parse(steps).getAsJsonObject();

		// Convert the steps JsonObject to JsonArray
		JsonArray stepsList = jsn.getAsJsonArray("data").getAsJsonArray();

		JsonArray testResultArray = new JsonArray();
		
		// Create the test result array with name, description , status etc.
		for (int index = 0; index < stepsList.size(); index++) {
			JsonObject step = stepsList.get(index).getAsJsonObject();
			JsonObject scenarioResult = new JsonObject();

			String scenarioDesc = step.getAsJsonObject("attributes").get("description").getAsString();
			scenarioResult.addProperty("name", step.getAsJsonObject("attributes").get("name").getAsString());
			scenarioResult.addProperty("description", scenarioDesc);
			scenarioResult.addProperty("status", status.get(index));
			scenarioResult.addProperty("expected-results", scenarioDesc);
			if (status.get(index).trim().equals("PASSED")) {
				scenarioResult.addProperty("actual-results", scenarioDesc);
			}

			testResultArray.add(scenarioResult);
		}

		// Create PractiTest test execution run
		PractiTest.createTestRun(PractiTestVariables.TESTINSTANCEID, testResultArray);

	}

}
