package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class RoundRobin {
	
	public RoundRobin(Process[] process, int quantumTime) {
		
		int[] originalBursts = new int[process.length];
		for (int i = 0; i < process.length; i++) {
			originalBursts[i] = process[i].getBurstTime();
		}
		
		Process[] roundRobinProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				Process usable = null;
				Queue queue1 = new Queue();
				int counter = 0;
				int[] arrivals = new int[roundRobinProcess.length];
				
				for(int i = 0; i < roundRobinProcess.length; i++) {
					arrivals[roundRobinProcess[i].getProcessID() - 1] = roundRobinProcess[i].getArrivalTime();
				}
				
				if (counter < roundRobinProcess[0].getArrivalTime()) {
					for(int k = 0; k < roundRobinProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter, 1);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				int countTempLength = 0;
				
				queue1.initialProcess(roundRobinProcess[0]);
				
				try {
					while (true) {
						if(countTempLength == roundRobinProcess.length) {
							break;
						}
						
						if (queue1.getIndex() == 0) {
							int nextArrival = 0;
							for (int i = 0; i < arrivals.length; i++) {
								if (arrivals[i] > counter) {
									if (nextArrival == 0) {
										nextArrival = arrivals[i];
									}
									
									if (nextArrival > arrivals[i]) {
										nextArrival = arrivals[i];
									}
								}
							}
							
							for (int i = counter; i < nextArrival; i++) {
								GanttChartPanel.addToGanttChart(0, counter, 1);
								counter++;
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) { }
							}
							
							for (int j = 0; j < roundRobinProcess.length; j++) {
								if (counter == roundRobinProcess[j].getArrivalTime()) {
									queue1.initialProcess(roundRobinProcess[j]);
								}
							}
						}
						
						usable = queue1.dequeue();
						
						if (usable.getBurstTime() == originalBursts[usable.getProcessID() - 1]) {
							TimesPanel.responseTime(counter, usable.getArrivalTime(), usable.getProcessID());
						}
						
						for (int i = 0; i < quantumTime; i++) {
							if (usable.getBurstTime() == 0) {
								countTempLength++;
								break;
							}
							
							GanttChartPanel.addToGanttChart(usable.getProcessID(), counter, 1);
							System.out.print(usable.getProcessID() + " ");
							usable.setBurstTime(usable.getBurstTime() - 1);
							
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
							
							for (int j = 0; j < roundRobinProcess.length; j++) {
								if (counter == roundRobinProcess[j].getArrivalTime()) {
									queue1.enqueue(roundRobinProcess[j]);
								}
							}
						}
						System.out.println();
						
						if (usable.getBurstTime() != 0) {
							queue1.enqueue(usable);
						} else if (usable.getBurstTime() == 0) {
							TimesPanel.turnaroundTime(counter, arrivals[usable.getProcessID() - 1], usable.getProcessID());
							TimesPanel.waitingTime(originalBursts[usable.getProcessID() - 1], usable.getProcessID());
						}
					}
				} catch (NullPointerException ex) { }
			}
		}.start();
	}
	
	public RoundRobin(Process[] process, int quantumTime, int ganttChartCounter, int queueNumber) {
		
		int[] originalBursts = new int[process.length];
		for (int i = 0; i < process.length; i++) {
			originalBursts[i] = process[i].getBurstTime();
		}
		
		Process[] roundRobinProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				Process usable = null;
				Queue queue1 = new Queue();
				int counter = 0, gtCounter = ganttChartCounter;
				int[] arrivals = new int[roundRobinProcess.length], arrivals1 = new int[roundRobinProcess.length];;
				
				for(int i = 0; i < roundRobinProcess.length; i++) {
					arrivals[roundRobinProcess[i].getProcessID() - 1] = roundRobinProcess[i].getArrivalTime();
					arrivals1[i] = roundRobinProcess[i].getArrivalTime();
				}
				
				int countTempLength = 0;
				int min = 60, index = 0;
				for (int i = 0; i < roundRobinProcess.length; i++) {
					if (roundRobinProcess[i].getBurstTime() != 0 && roundRobinProcess[i].getArrivalTime() < min) {
						index = i;
						min = roundRobinProcess[i].getArrivalTime();
					}
				}
				queue1.initialProcess(roundRobinProcess[index]);
				
				try {
					while (true) {
						if(countTempLength == roundRobinProcess.length) {
							break;
						}
						
						if (queue1.getIndex() == 0) {
							int nextArrival = 0;
							for (int i = 0; i < roundRobinProcess.length; i++) {
								if (roundRobinProcess[i].getBurstTime() != 0) {
									if (nextArrival == 0) {
										nextArrival = arrivals1[i];
									}
									if (nextArrival > arrivals1[i]) {
										System.out.print(" YAY ");
										nextArrival = arrivals1[i];
									}
								}
							}
							
							for (int j = 0; j < roundRobinProcess.length; j++) {
								if (counter == roundRobinProcess[j].getArrivalTime()) {
									queue1.initialProcess(roundRobinProcess[j]);
								}
							}
						}
						
						usable = queue1.dequeue();
						
						if (usable.getBurstTime() == originalBursts[usable.getProcessID() - 1]) {
//							TimesPanel.responseTime(counter, usable.getArrivalTime(), usable.getProcessID());
						}
						
						for (int i = 0; i < quantumTime; i++) {
							if (usable.getBurstTime() == 0) {
								countTempLength++;
								break;
							}
							
							GanttChartPanel.addToGanttChart(usable.getProcessID(), gtCounter, queueNumber);
							System.out.print(usable.getProcessID() + " ");
							usable.setBurstTime(usable.getBurstTime() - 1);
							
							counter++;
							gtCounter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
							
							for (int j = 0; j < roundRobinProcess.length; j++) {
								if (counter == roundRobinProcess[j].getArrivalTime() && roundRobinProcess[j].getBurstTime() != 0) {
									queue1.enqueue(roundRobinProcess[j]);
								}
							}
						}
						
						if (usable.getBurstTime() != 0) {
							queue1.enqueue(usable);
						} else if (usable.getBurstTime() == 0) {
//							TimesPanel.turnaroundTime(counter, arrivals[usable.getProcessID() - 1], usable.getProcessID());
//							TimesPanel.waitingTime(originalBursts[usable.getProcessID() - 1], usable.getProcessID());
						}
					}
				} catch (NullPointerException ex) { }
			}
		}.start();
	}
}
