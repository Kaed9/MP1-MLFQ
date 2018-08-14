package com.mlfq.data_structures;

public class Process
{
	private int processID, arrivalTime, burstTime, priority;
	
	public Process(int processID, int arrivalTime, int burstTime, int priority)
	{
		this.processID = processID;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.priority = priority;
	}
	
	public int getProcessID()
	{
		return processID;
	}

	public int getArrivalTime()
	{
		return arrivalTime;
	}

	public int getBurstTime()
	{
		return burstTime;	
	}
	
	public int getPriority()
	{ 
		return priority;
	}
	
	public void setArrivalTime(int arrivalTime)
	{

		this.arrivalTime = arrivalTime;
	}

	public void setBurstTime(int burstTime)
	{

		this.burstTime = burstTime;
	}
}
