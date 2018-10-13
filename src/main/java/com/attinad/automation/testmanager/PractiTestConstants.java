package com.attinad.automation.testmanager;

public class PractiTestConstants {

	private PractiTestConstants() {
	}

	public static final String USERNAME = "anand.xavier@attinadsoftware.com";
	public static final String PASSWORD = "6cc82ef55255ff3374c010314bb5c1727f797b74";

	public static final String PROJECT_ID = "4148";

	public static final String URL_HOME = "https://api.practitest.com/api/v2";
	public static final String URL_PROJECT = URL_HOME + "/projects/" + PROJECT_ID;
	public static final String URL_GET_TEST = URL_PROJECT + "/tests";
	public static final String URL_CREATE_TEST_INSTANCE = URL_PROJECT + "/instances.json";
	public static final String URL_CREATE_TEST_SET = URL_PROJECT + "/sets.json";
	public static final String URL_CREATE_TEST_RUN = URL_PROJECT + "/runs.json";
	public static final String URL_GET_TEST_STEPS = URL_PROJECT + "/steps.json";

	public static final String TEST_RESULT_FILE = "TestResult.txt";
}
