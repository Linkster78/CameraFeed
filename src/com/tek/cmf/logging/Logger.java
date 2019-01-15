package com.tek.cmf.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
	
	public static void info(String message) {
		System.out.println(String.format("[%s][INFO] %s", time(), message));
	}
	
	public static void warning(String message) {
		System.out.println(String.format("[%s][WARNING] %s", time(), message));
	}
	
	public static void error(String message) {
		System.out.println(String.format("[%s][ERROR] %s", time(), message));
	}
	
	private static String time() {
		LocalDateTime dateTime = LocalDateTime.now();
		return String.format("%s %s", dateTime.format(DateTimeFormatter.ISO_DATE), dateTime.format(DateTimeFormatter.ISO_TIME));
	}
	
}
