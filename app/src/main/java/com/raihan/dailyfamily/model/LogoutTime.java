package com.raihan.dailyfamily.model;

public class LogoutTime {
	
	public static long startTime=15*60*1000; // 4 MINS IDLE TIME
	//public static long startTime=30000; // 2 MINS IDLE TIME
	
	public static long interval = 2 * 1000;
	
	
	public static int deviceSleepMode=5*60*1000; // 5 For time out
	
	public static String sessionMssage = "You Session Is Expire! Please Sing In Again";
	



}
