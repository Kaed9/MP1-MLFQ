package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class ShortestJobFirst
{
	public ShortestJobFirst(Process[] process)
	{
		Process[] sjfProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread()
		{
			public void run()
			{
				int burst[] = new int[process.length], arrival[] = new int[process.length];
				boolean isAvailable[] = new boolean[process.length];
				
				for(int i = 0; i < process.length; i++) {
					burst[i] = 0;
					arrival[i] = sjfProcess[i].getArrivalTime();
				}
				
				Queue queue = new Queue();
				int counter = 0;
				
				if (counter < sjfProcess[0].getArrivalTime()) {
					for(int k = 0; k < sjfProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter, 1);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < sjfProcess.length; i++) {
					if (i == 0 && SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1) != -1) {
						for(int j = 0; j < sjfProcess[i].getBurstTime(); j++) {
							if (j == 0) {
								TimesPanel.responseTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
								queue.initialProcess(sjfProcess[i]);
								GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter, 1);
								System.out.print(sjfProcess[i].getProcessID() + " ");
							} else {
								queue.enqueue(sjfProcess[i]);
								GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter, 1);
								System.out.print(sjfProcess[i].getProcessID() + " ");
							}
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
						
						TimesPanel.turnaroundTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
						TimesPanel.waitingTime(sjfProcess[i].getBurstTime(), sjfProcess[i].getProcessID());
						
						burst[i] = -1;
					} else {
						for(int j = 1; j < sjfProcess.length; j++) {
							if (burst[j] != -1 && counter > sjfProcess[j].getArrivalTime()) {
								isAvailable[j] = true;
								burst[j] = sjfProcess[j].getBurstTime();
							}
						}
						boolean flag = false;
						
						for(int j = 0; j < sjfProcess.length; j++) {
							if (counter < sjfProcess[j].getArrivalTime()) {
								for(int k = counter; k < sjfProcess[j].getArrivalTime(); k++) {
									GanttChartPanel.addToGanttChart(0, counter, 1);
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
							}
							
							if (isAvailable[j] == true && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(burst, 0) && flag == false) {
								for(int k = 0; k < sjfProcess[j].getBurstTime(); k++) {
									if (k == 0) {
										TimesPanel.responseTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
									}
									
									queue.enqueue(sjfProcess[j]);
									GanttChartPanel.addToGanttChart(sjfProcess[j].getProcessID(), counter, 1);
									System.out.print(sjfProcess[j].getProcessID() + " ");
									
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
								
								burst[j] = -1;
								flag = true;
							} else { continue; }
							
							TimesPanel.turnaroundTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
							TimesPanel.waitingTime(sjfProcess[j].getBurstTime(), sjfProcess[j].getProcessID());
						}
					}
				}
			}
		}.start();
	}
}
