package com.mlfq.utilities;

public class Logger
{
	public Logger()
	{
		
	}
	
	public static void printToConsole(String componentName, String messageType, String message)
	{
		System.out.println(messageType + " message from [" + componentName + "]: " + message);
	}
}
