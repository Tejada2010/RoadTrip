package com.roadtrip;

public class Duration {
	private Integer hours;
	private Integer minutes;
	
	public Duration (Integer h, Integer m) {
		hours = h;
		minutes = m;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
}
