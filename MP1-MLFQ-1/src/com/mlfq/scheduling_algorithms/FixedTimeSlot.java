package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class FixedTimeSlot {
	static Process[] mainProcess;
	boolean priority = true;
	int[] queue;
	//note: algorithm: {1:RR, 2:FCFS, 3:SJF, 4:SRTF, 5:Prio, 6:NPrio}
	
	public FixedTimeSlot(Process[] p) {
		this.mainProcess = p;
	}
	
	public FixedTimeSlot(Process[] process, boolean priority, int[] algorithm, int[] quantumTime) {
		this.mainProcess = process;
		this.priority = priority;
		this.queue = new int[process.length];
		Process[] temp = process;		//initialize queues of Processes
		for(int i = 0; i < queue.length; i++) {
			queue[i] = 1;
		}
		
		for(int i = 1; i <= algorithm.length; i++) {
			getProcess(algorithm[i-1], process, quantumTime[i-1], i);
		}
	}
	
	public Process[] refresh() {
		return mainProcess;
	}
	
	public void getProcess(int num, Process[] process, int quantumTime, int queueNumber) {
		switch (num) {
		case 1: 
			RoundRobin(process, quantumTime, queueNumber);
			break;
		case 2:
			FCFS(process, queueNumber, quantumTime);
			break;
		case 3:
			SJF(process, queueNumber, quantumTime);
			break;
		case 4:
			SRTF(process, queueNumber, quantumTime);
			break;
		case 5:
			Prio(process, queueNumber, quantumTime);
			break;
		case 6: 
			NPrio(process, queueNumber, quantumTime);
			break;
		default:
			System.out.print("Invalid Input");
		}
	}
	
	public void RoundRobin(Process[] process, int quantumTime, int queueNumber) {
		Process[] roundRobinProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		int burstTime;
				
		for(int i = 0; i < roundRobinProcess.length; i++) {
			int counter = 0;
			burstTime = roundRobinProcess[i].getBurstTime();
			boolean flag = true;
			for(int j = 0; j < burstTime; j++) {
				if(counter < quantumTime && queue[i] == queueNumber) {
					counter++;
					roundRobinProcess[i].setBurstTime(roundRobinProcess[i].getBurstTime() - 1);
					System.out.print(roundRobinProcess[i].getProcessID() + " ");				
				}else{
					if(flag == true) {
						queue[i]++;	
						flag = false;
					}
				}				
			}		
		}
		
		System.out.println();
	}
	
	public void FCFS(Process[] process, int queueNumber, int quantumTime) {
		process = refresh();
		Process[] fcfsProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length-1, 0);
		
		new Thread()
		{
			public void run()
			{
				//System.out.print("flag");
				Queue queue = new Queue();
				int counter = 0;
				
				if (counter < fcfsProcess[0].getArrivalTime()) {
					for(int k = 0; k < fcfsProcess[0].getArrivalTime(); k++) {
						//GanttChartPanel.addToGanttChart(0, counter);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < fcfsProcess.length; i++) {
					if (counter < fcfsProcess[i].getArrivalTime()) {
						for(int k = counter; k < fcfsProcess[i].getArrivalTime(); k++) {
							//GanttChartPanel.addToGanttChart(0, counter);
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
					}
					
					for(int j = 0; j < fcfsProcess[i].getBurstTime(); j++) {
						if (j == 0) {
							//TimesPanel.responseTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
						}
						
						if (i == 0 && j == 0) {
							queue.initialProcess(fcfsProcess[i]);
							System.out.print(fcfsProcess[i].getProcessID() + " ");
							//GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter);
						} else {
							queue.enqueue(fcfsProcess[i]);
							System.out.print(fcfsProcess[i].getProcessID() + " ");
							//GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter);
						}
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
					
					//TimesPanel.turnaroundTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
					//TimesPanel.waitingTime(fcfsProcess[i].getBurstTime(), fcfsProcess[i].getProcessID());
				}
			}
		}.start();
		System.out.println();
		
	}
	
	public void SJF(Process[] process, int queueNumber, int quantumTime) {
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
						//GanttChartPanel.addToGanttChart(0, counter);
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
								//TimesPanel.responseTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
								queue.initialProcess(sjfProcess[i]);
								//GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter);
								System.out.print(sjfProcess[i].getProcessID() + " ");
							} else {
								queue.enqueue(sjfProcess[i]);
								//GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter);
								System.out.print(sjfProcess[i].getProcessID() + " ");
							}
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
						
						//TimesPanel.turnaroundTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
						//TimesPanel.waitingTime(sjfProcess[i].getBurstTime(), sjfProcess[i].getProcessID());
						
						burst[i] = -1;
					} else {
						for(int j = 1; j < sjfProcess.length; j++) {
							if (burst[j] != -1) {
								isAvailable[j] = true;
								burst[j] = sjfProcess[j].getBurstTime();
							}
						}
						boolean flag = false;
						
						for(int j = 0; j < sjfProcess.length; j++) {
							if (counter < sjfProcess[j].getArrivalTime()) {
								for(int k = counter; k < sjfProcess[j].getArrivalTime(); k++) {
									//GanttChartPanel.addToGanttChart(0, counter);
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
							}
							
							if (isAvailable[j] == true && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(burst, 0) && flag == false) {
								for(int k = 0; k < sjfProcess[j].getBurstTime(); k++) {
									if (k == 0) {
										//TimesPanel.responseTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
									}
									
									queue.enqueue(sjfProcess[j]);
									//GanttChartPanel.addToGanttChart(sjfProcess[j].getProcessID(), counter);
									System.out.print(sjfProcess[j].getProcessID() + " ");
									
									counter++;
									try {
										Thread.sleep(100);
									} catch (InterruptedException ex) { }
								}
								
								burst[j] = -1;
								flag = true;
							} else { continue; }
							
							//TimesPanel.turnaroundTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
							//TimesPanel.waitingTime(sjfProcess[j].getBurstTime(), sjfProcess[j].getProcessID());
						}
					}
				}
			}
		}.start();
	}
	
	public void SRTF(Process[] process, int queueNumber, int quantumTime) {
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
						//GanttChartPanel.addToGanttChart(0, counter);
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
						if (isAvailable[j] = true && SchedulingAlgorithmsUtilities.getSmallestNum(tempB,0) != -1 && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(tempB,0) && flag == false) {
							if (!responseTimeDone[j]) {
								//TimesPanel.responseTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
								responseTimeDone[j] = true;
							}
							if (i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
								queue.initialProcess(srtfProcess[j]);
								burst[j]--;
								tempB[j]--;
								flag = true;
								//GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter);
								System.out.print(srtfProcess[j].getProcessID() + " ");
							} else {
								queue.enqueue(srtfProcess[j]);
								burst[j]--;
								tempB[j]--;
								flag = true;
								//GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter);
								System.out.print(srtfProcess[j].getProcessID() + " ");
							}
						} else { continue; }
						
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
						
						if (burst[j] == 0) {
							//TimesPanel.turnaroundTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
							//TimesPanel.waitingTime(srtfProcess[j].getBurstTime(), srtfProcess[j].getProcessID());
						}
					}
				}
			}
		}.start();
	}
	
	public void Prio(Process[] process, int queueNumber, int quantumTime) {
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
						//GanttChartPanel.addToGanttChart(0, counter);
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
									//TimesPanel.responseTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
									responseTimeDone[j] = true;
								}
								
								if(i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
									queue.initialProcess(prioProcess[j]);
									burst[j]--;
									flag = true;
									//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
									System.out.print(prioProcess[j].getProcessID() + " ");
									
									if(burst[j] == 0) { prio[j] = -1; }
								} else {
									queue.enqueue(prioProcess[j]);
									burst[j]--;
									flag = true;
									//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
									System.out.print(prioProcess[j].getProcessID() + " ");
									
									if(burst[j] == 0) { prio[j] = -1; }
								}
							} else { continue; }
							
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
							
							if (burst[j] == 0) {
								//TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								//TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
							}
						}
					} else if(done) {
						for(int j = 0; j < prioProcess.length; j++) {
							if(SchedulingAlgorithmsUtilities.getSmallestNum(prio, 0) == prio[j] && flag == false) {
								queue.enqueue(prioProcess[j]);
								burst[j]--;
								flag = true;
								//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
								System.out.print(prioProcess[j].getProcessID() + " ");
								
								if(burst[j] == 0) { prio[j] = -1; }
							}
							
							if (burst[j] == 0) {
								//TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								//TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
							}
						}
					}
				}
			}
		}.start();
	}
	
	public void NPrio(Process[] process, int queuNumber, int quantumTime) {
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
						//GanttChartPanel.addToGanttChart(0, counter);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < nPrioProcess.length; i++) {
					if (i == 0) {
						for(int j = 0; j < nPrioProcess[i].getBurstTime(); j++) {
							if (j == 0) {
								queue.initialProcess(nPrioProcess[i]);
								//TimesPanel.responseTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
								//GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter);
								System.out.print(nPrioProcess[i].getProcessID() + " ");
							} else {
								queue.enqueue(nPrioProcess[i]);
								//GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter);
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
								for(int k = 0; k < nPrioProcess[j].getBurstTime(); k++) {
									if (k == 0) {
										//TimesPanel.responseTime(counter, nPrioProcess[j].getArrivalTime(), nPrioProcess[j].getProcessID());
									}
									
									queue.enqueue(nPrioProcess[j]);
									//GanttChartPanel.addToGanttChart(nPrioProcess[j].getProcessID(), counter);
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
					
					//TimesPanel.turnaroundTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
					//TimesPanel.waitingTime(nPrioProcess[i].getBurstTime(), nPrioProcess[i].getProcessID());
				}
			}
		}.start();
	}
	public static void main(String[] args) {
System.out.println("Fixed Time Slot");
		
		Process p[] = {new Process(1, 5, 7, 9), 
				   new Process(2, 1, 5, 2), 
				   new Process(3, 2, 3, 5), 
				   new Process(4, 6, 1, 1), 
				   new Process(5, 4, 2, 6), 
				   new Process(6, 3, 1, 10)};
		
		Process temp[] = p;
		int algo[] = {2};
		int quantumTime[] = {4};
		
		FixedTimeSlot fts = new FixedTimeSlot(p, true, algo, quantumTime);
		
		System.out.println("Print");
		FixedTimeSlot m = new FixedTimeSlot(p);
		//System.out.print("flag");
		//m.getProcess(2, temp, 0, 1);
		System.out.println();
		m.FCFS(temp, 1, 0);
		// TODO Auto-generated method stub

	}

}
