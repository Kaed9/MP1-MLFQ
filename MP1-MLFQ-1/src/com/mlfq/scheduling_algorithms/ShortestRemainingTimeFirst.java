package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class ShortestRemainingTimeFirst
{
	public ShortestRemainingTimeFirst(Process[] process)
	{
		Process[] srtfProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread()
		{
			public void run()
			{
				boolean[] isAvailable = new boolean[process.length], responseTimeDone = new boolean[process.length];
				int[] burst = new int[process.length], tempB = new int[process.length], arrival = new int[process.length];
				
				for(int i = 0; i < srtfProcess.length; i++) {
					responseTimeDone[i] = false;
					isAvailable[i] = false;
					burst[i] = srtfProcess[i].getBurstTime();
					tempB[i] = 0;
					arrival[i] = srtfProcess[i].getArrivalTime();
				}
				
				Queue queue = new Queue();
				int counter = 0;
				
				if (counter < srtfProcess[0].getArrivalTime()) {
					for(int k = 0; k < srtfProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter, 0);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < (SchedulingAlgorithmsUtilities.totalTime(1, srtfProcess) + SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)); i++) {
					boolean flag = false;
					
					for(int j = 0; j < srtfProcess.length; j++) {
						if (i == srtfProcess[j].getArrivalTime()) {
							isAvailable[j] = true;
							tempB[j] = burst[j];
						} else { continue; }
					}
					
					for(int j = 0; j < srtfProcess.length; j++) {
						int currentArrival = 0, currentBurst = 0, currentID = 0;
						
						if (isAvailable[j] = true && SchedulingAlgorithmsUtilities.getSmallestNum(tempB, 0) != -1 && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(tempB, 0) && flag == false) {
							if (counter < srtfProcess[j].getArrivalTime()) {
								for(int k = counter; k < srtfProcess[j].getArrivalTime(); k++) {
									GanttChartPanel.addToGanttChart(0, counter, 0);
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
							}
							
							currentArrival = srtfProcess[j].getArrivalTime();
							currentBurst = srtfProcess[j].getBurstTime();
							currentID = srtfProcess[j].getProcessID();
							
							if (!responseTimeDone[j]) {
								TimesPanel.responseTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
								responseTimeDone[j] = true;
							}
							
							if (i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
								queue.initialProcess(srtfProcess[j]);
								burst[j]--;
								tempB[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter, 0);
								System.out.print(srtfProcess[j].getProcessID() + " ");
							} else {
								queue.enqueue(srtfProcess[j]);
								burst[j]--;
								tempB[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter, 0);
								System.out.print(srtfProcess[j].getProcessID() + " ");
							}
						} else { continue; }
						
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
						
						if (i == ((SchedulingAlgorithmsUtilities.totalTime(1, srtfProcess) + SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) - 1)) {
							while (burst[j] != 0) {
								queue.enqueue(srtfProcess[j]);
								burst[j]--;
								tempB[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter, 0);
								System.out.print(srtfProcess[j].getProcessID() + " ");
								
								counter++;
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) { }
							}
						}
						
						if (burst[j] == 0) {
							if (currentArrival == 0 && currentBurst == 0 && currentID == 0) {
								TimesPanel.turnaroundTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
								TimesPanel.waitingTime(srtfProcess[j].getBurstTime(), srtfProcess[j].getProcessID());
							} else {
								TimesPanel.turnaroundTime(counter, currentArrival, currentID);
								TimesPanel.waitingTime(currentBurst, currentID);
							}
						}
					}
				}
			}
		}.start();
	}
}
