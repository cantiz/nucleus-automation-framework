package com.attinad.automation.common;

import com.google.gson.Gson;

public class GSONSerialize {

	//Function to convert JAVA object to JSON String using GSON library
	public static String serializeGSON(Object obj){
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}
}
