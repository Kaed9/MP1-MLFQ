package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class RoundRobin {
	
	public RoundRobin(Process[] process, int quantumTime) {
		
		Process[] roundRobinProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				Process usable = null, temp1[] = roundRobinProcess;
				Queue queue1 = new Queue(), queue2 = new Queue();
				int tempLength = roundRobinProcess.length;
				int counter = 0;
				int[] bursts = new int[tempLength], arrivals = new int[tempLength];
				boolean[] responseTimeDone = new boolean[tempLength];
				boolean isDone = false;
				
				for(int i = 0; i < tempLength; i++) {
					bursts[roundRobinProcess[i].getProcessID() - 1] = roundRobinProcess[i].getBurstTime();
					arrivals[roundRobinProcess[i].getProcessID() - 1] = roundRobinProcess[i].getArrivalTime();
					responseTimeDone[i] = false;
				}
				
				if (counter < roundRobinProcess[0].getArrivalTime()) {
					for(int k = 0; k < roundRobinProcess[0].getArrivalTime(); k++) {
						//GanttChartPanel.addToGanttChart(0, counter);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				queue2.initialProcess(roundRobinProcess[0]);
				for(int i = 1; i < roundRobinProcess.length; i++) {
					queue2.enqueue(roundRobinProcess[i]);
				}
				
				int countTempLength = tempLength;
				int quantumTimeLoop = roundRobinProcess[0].getArrivalTime();
				
				while (true) {
					usable = queue2.dequeue();
					
					int currentBurst = usable.getBurstTime();
					int currentArrival = usable.getArrivalTime();
					
					if (!responseTimeDone[usable.getProcessID() - 1]) {
						//TimesPanel.responseTime(counter, usable.getArrivalTime(), usable.getProcessID());
						responseTimeDone[usable.getProcessID() - 1] = true;
					}
					
					if (usable.getArrivalTime() <= quantumTimeLoop) {
						if (currentBurst > quantumTime) {
							usable.setBurstTime(currentBurst - quantumTime);
							usable.setArrivalTime(currentArrival + quantumTime);
							quantumTimeLoop += quantumTime;

							for(int j = 0; j < quantumTime; j++) {
								for(int i = 0; i < tempLength; i++) {
									if (queue1.getIndex() == 0) {
										if(temp1[i].getProcessID() == usable.getProcessID()) {									
											queue1.initialProcess(temp1[i]);
											//GanttChartPanel.addToGanttChart(temp1[i].getProcessID(), counter);
											System.out.print(temp1[i].getProcessID() + " ");
										}
									} else {
										if (temp1[i].getProcessID() == usable.getProcessID()) {
											queue1.enqueue(temp1[i]);
											//GanttChartPanel.addToGanttChart(temp1[i].getProcessID(), counter);
											System.out.print(temp1[i].getProcessID() + " ");
										}
									}
								}
								
								counter++;
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) { }
							}
						} else if(currentBurst <= quantumTime && currentBurst != 0) {
							usable.setBurstTime(currentBurst - currentBurst);
							countTempLength--;
							quantumTimeLoop += currentBurst;
							isDone = true;

							for(int j = 0; j < currentBurst; j++) {
								for(int i = 0; i < tempLength; i++) {
									if(queue1.getIndex() == 0) {
										if (temp1[i].getProcessID() == usable.getProcessID()) {
											queue1.initialProcess(temp1[i]);
											//GanttChartPanel.addToGanttChart(temp1[i].getProcessID(), counter);
											System.out.print(temp1[i].getProcessID() + " ");
										}
									} else {
										if (temp1[i].getProcessID() == usable.getProcessID()) {
											queue1.enqueue(temp1[i]);
											//GanttChartPanel.addToGanttChart(temp1[i].getProcessID(), counter);
											System.out.print(temp1[i].getProcessID() + " ");
										}
									}
								}
								
								counter++;
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) { }
							}
						}
					}
					
					if (isDone) {
						//TimesPanel.turnaroundTime(counter, arrivals[usable.getProcessID() - 1], usable.getProcessID());
						//TimesPanel.waitingTime(bursts[usable.getProcessID() - 1], usable.getProcessID());
						isDone = false;
					}
					
					queue2.enqueue(usable);

					if(countTempLength == 0) {
						break;
					}
				}
			}
		}.start();
	}
	
}
