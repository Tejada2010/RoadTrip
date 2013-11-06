package com.roadtrip;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsHelper {
	
	private final static String DIRECTIONS_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	
	
	public static Duration getDuration(String start, String end) {
		
		String url = DIRECTIONS_URL + "origin="
				+ start + "&destination=" + end + "&sensor=false";
		
		JSONObject obj = RestHelper.requestWebService(url);
		
		try {
			String timeText = obj.getJSONArray("routes").getJSONObject(0)
					.getJSONArray("legs").getJSONObject(0).getJSONObject("duration")
					.getString("text");
			
			String[] timeParsed = timeText.split(" ");
			Duration time = new Duration(Integer.parseInt(timeParsed[0])
					, Integer.parseInt(timeParsed[3]));
			return time;
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
