package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class NonPreemptivePriorityScheduling {
	
	public NonPreemptivePriorityScheduling(Process[] process) {
		
		Process[] nPrioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				int prio[] = new int[nPrioProcess.length];
				
				for(int i = 0; i < nPrioProcess.length; i++) {
					prio[i] = nPrioProcess[i].getPriority();
				}
				
				Queue queue = new Queue();
				int counter = 0;
				
				if (counter < nPrioProcess[0].getArrivalTime()) {
					for(int k = 0; k < nPrioProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter, 0);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < nPrioProcess.length; i++) {
					int currentArrival = 0, currentBurst = 0, currentID = 0;
					
					if (i == 0) {
						for(int j = 0; j < nPrioProcess[i].getBurstTime(); j++) {
							if (j == 0) {
								queue.initialProcess(nPrioProcess[i]);
								TimesPanel.responseTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
								GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter, 0);
								System.out.print(nPrioProcess[i].getProcessID() + " ");
							} else {
								queue.enqueue(nPrioProcess[i]);
								GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter, 0);
								System.out.print(nPrioProcess[i].getProcessID() + " ");
							}
							
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
						
						prio[i] = -1;
					} else {
						boolean flag = false;
						for(int j = 0; j < nPrioProcess.length; j++) {
							if (nPrioProcess[j].getPriority() == SchedulingAlgorithmsUtilities.getSmallestNum(prio, 1) && prio[j] != -1 && flag == false) {
								if (counter < nPrioProcess[j].getArrivalTime()) {
									for(int k = counter; k < nPrioProcess[j].getArrivalTime(); k++) {
										GanttChartPanel.addToGanttChart(0, counter, 0);
										counter++;
										try {
											Thread.sleep(100);
										} catch (InterruptedException ex) { }
									}
								}
								
								for(int k = 0; k < nPrioProcess[j].getBurstTime(); k++) {
									if (k == 0) {
										TimesPanel.responseTime(counter, nPrioProcess[j].getArrivalTime(), nPrioProcess[j].getProcessID());
									}
									
									currentArrival = nPrioProcess[j].getArrivalTime();
									currentBurst = nPrioProcess[j].getBurstTime();
									currentID = nPrioProcess[j].getProcessID();
									
									queue.enqueue(nPrioProcess[j]);
									GanttChartPanel.addToGanttChart(nPrioProcess[j].getProcessID(), counter, 0);
									System.out.print(nPrioProcess[j].getProcessID() + " ");
									
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
								
								prio[j] = -1;
								flag = true;
							}
						}
					}
					
					if (currentArrival == 0 && currentID == 0 && currentBurst == 0) {
						TimesPanel.turnaroundTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
						TimesPanel.waitingTime(nPrioProcess[i].getBurstTime(), nPrioProcess[i].getProcessID());
					} else {
						TimesPanel.turnaroundTime(counter, currentArrival, currentID);
						TimesPanel.waitingTime(currentBurst, currentID);
					}
				}
			}
		}.start();
	}
	
}
