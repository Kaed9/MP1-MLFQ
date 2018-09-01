package com.mlfq.utilities;

import com.mlfq.data_structures.Process;

public class SchedulingAlgorithmsUtilities
{
	private final Process[] process;
	
	public SchedulingAlgorithmsUtilities(Process[] process)
	{
		this.process = process;
	}
	
	public int getSmallestNum(int num[], int c)
	{
		int temp = -1; boolean flag = false;
		
		if (c == 0) {
			for(int i = 0; i < num.length; i++) {
				if (num[i] != 0 && flag == false && num[i] != -1) {
					temp = num[i];
					flag = true;
				} else {
					continue;
				}
			}
		
			for(int i = 0; i < num.length; i++) {
				if (temp > num[i] && num[i] != 0 && num[i] != -1) {
					temp = num[i];
				}
			}
		} else if(c == 1) {
			for(int i = 0; i < num.length; i++) {
				if (flag == false && num[i] != -1) {
					temp = num[i];
					flag = true;
				} else {
					continue;
				}
			}
			
			for(int i = 0; i < num.length; i++) {
				if (temp > num[i] && num[i] != -1) {
					temp = num[i];
				}
			}
		}
		
		return temp;
	}
	
	public int totalTime(int c)
	{
		int count = 0;
		
		//count arrival time
		if (c == 0) {
			for(int i = 0; i < process.length; i++)
			{
				count += process[i].getArrivalTime();
			}
		}
		//count burst time
		if (c == 1) {
			for(int i = 0; i < process.length; i++) {
				count += process[i].getBurstTime();
			}
		}
		return count;
	}
	
	public static Process[] quicksort(Process p[], int low, int high, int c)
	{
		int i = low, j = high;
		
		Process pivot = p[low + (high-low) / 2];
		
		if (c == 0) { //sort arrival time
			while(i <= j) {
				while(p[i].getArrivalTime() < pivot.getArrivalTime()) {
					i++;
				}
			
				while(p[j].getArrivalTime() > pivot.getArrivalTime()) {
					j--;
				}
			
				if (i <= j) {
					swap(p,i, j);
					i++;
					j--;
				}
			}
		} else if(c == 1) { //sort burstTime
			while(i <= j) {
				while(p[i].getBurstTime() < pivot.getBurstTime()) {
					i++;
				}
			
				while(p[j].getBurstTime() > pivot.getBurstTime()) {
					j--;
				}
			
				if (i <= j) {
					swap(p,i, j);
					i++;
					j--;
				}
			}
		} else if(c == 2) { //sort priority
			while(i <= j) {
				while(p[i].getPriority() < pivot.getPriority()) {
					i++;
				}
			
				while(p[j].getPriority() > pivot.getPriority()) {
					j--;
				}
			
				if (i <= j) {
					swap(p,i, j);
					i++;
					j--;
				}
			}
		}
		
		if (low < j) {
			quicksort(p, low, j, c);
		}
		if (i < high) {
			quicksort(p,i, high, c);
		}
		
		return p;
	}
	
	private static void swap(Process arr[], int i, int j)
	{
		Process temporary = arr[i];
		arr[i] = arr[j];
		arr[j] = temporary;
		arr[j] = temporary;
	}
}
