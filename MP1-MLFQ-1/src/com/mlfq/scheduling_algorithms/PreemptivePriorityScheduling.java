package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class PreemptivePriorityScheduling {
	
	public PreemptivePriorityScheduling(Process[] process) {
		
		Process[] prioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				boolean[] isAvailable = new boolean[prioProcess.length], responseTimeDone = new boolean[prioProcess.length];
				int[] burst = new int[prioProcess.length], tempB = new int[prioProcess.length], prio = new int[prioProcess.length], arrival = new int[prioProcess.length];
			
				for(int i = 0; i < prioProcess.length; i++){
					isAvailable[i] = false;
					responseTimeDone[i] = false;
					burst[i] = prioProcess[i].getBurstTime();
					tempB[i] = 0;
					prio[i] = 0;
					arrival[i] = prioProcess[i].getArrivalTime();
				}
				
				Queue queue = new Queue();
				int counter = 0;
				
				if(counter < prioProcess[0].getArrivalTime()) {
					for(int k = 0; k < prioProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < (SchedulingAlgorithmsUtilities.totalTime(1, prioProcess) + SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)); i++) {
					boolean flag = false, done = true, first = false;
					
					for(int j = 0; j < prioProcess.length; j++) {
						if(i == prioProcess[j].getArrivalTime()) {
							isAvailable[j] = true;
							tempB[j] = burst[j];
							prio[j] = prioProcess[j].getPriority();
							
						} else { continue; }
					}
					
					first = isAvailable[0];
					for(int j = 0; j < prioProcess.length; j++) {				
						if(first != isAvailable[j]) {
							done = false;
						}		
					}
					
					if(done == false) {
						for(int j = 0; j < prioProcess.length; j++) {
							if(isAvailable[j] = true && SchedulingAlgorithmsUtilities.getSmallestNum(prio, 0) == prio[j] && flag == false) {
								if (!responseTimeDone[j]) {
									TimesPanel.responseTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
									responseTimeDone[j] = true;
								}
								
								if(i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
									queue.initialProcess(prioProcess[j]);
									burst[j]--;
									flag = true;
									GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
									System.out.print(prioProcess[j].getProcessID() + " ");
									
									if(burst[j] == 0) { prio[j] = -1; }
								} else {
									queue.enqueue(prioProcess[j]);
									burst[j]--;
									flag = true;
									GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
									System.out.print(prioProcess[j].getProcessID() + " ");
									
									if(burst[j] == 0) { prio[j] = -1; }
								}
							} else { continue; }
							
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
							
							if (burst[j] == 0) {
								TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
							}
						}
					} else if(done) {
						for(int j = 0; j < prioProcess.length; j++) {
							if(SchedulingAlgorithmsUtilities.getSmallestNum(prio, 0) == prio[j] && flag == false) {
								queue.enqueue(prioProcess[j]);
								burst[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
								System.out.print(prioProcess[j].getProcessID() + " ");
								
								if(burst[j] == 0) { prio[j] = -1; }
							}
							
							if (burst[j] == 0) {
								TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
							}
						}
					}
				}
			}
		}.start();
	}
	
}
