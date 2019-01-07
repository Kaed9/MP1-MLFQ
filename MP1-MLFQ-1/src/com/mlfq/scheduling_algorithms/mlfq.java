package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class mlfq{
	Process[] mainProcess;
	boolean priority = true; //
	int[] queue;
	//note: algorithm: {1:RR, 2:FCFS, 3:SJF, 4:SRTF, 5:Prio, 6:NPrio}
	public mlfq() {}
	
	public mlfq(Process[] process, boolean priority, int[] algorithm, int[] quantumTime) {
		this.mainProcess = process;
		this.priority = priority;
		this.queue = new int[process.length];
		Process[] temp = process;		//initialize queues of Processes
		for(int i = 0; i < queue.length; i++) {
			queue[i] = 1;
		}
		
		for(int i = 1; i <= algorithm.length; i++) {
			getProcess(algorithm[i-1], mainProcess, quantumTime[i-1], i);
		}
	}
	
	public void getProcess(int num, Process[] process, int quantumTime, int queueNumber) {
		switch (num) {
		case 1: 
			RoundRobin(process, quantumTime, queueNumber);
			break;
		case 2:
			FCFS(process, queueNumber);
			break;
		case 3:
			SJF(process, queueNumber);
			break;
		case 4:
			SRTF(process, queueNumber);
			break;
		case 5:
			Prio(process, queueNumber);
			break;
		case 6: 
			NPrio(process, queueNumber);
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
		
		mainProcess = roundRobinProcess;
		System.out.println();
	}
	
	public void FCFS(Process[] process, int queueNumber) {
		Process[] fcfsProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length-1, 0);
		
		System.out.println();
		new Thread()
		{
			public void run()
			{
				//System.out.print("flag");
				Queue queue1 = new Queue();
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
					int burstTime = fcfsProcess[i].getBurstTime();
					
					if (counter < fcfsProcess[i].getArrivalTime()) {
						for(int k = counter; k < fcfsProcess[i].getArrivalTime(); k++) {
							//GanttChartPanel.addToGanttChart(0, counter);
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
					}
					
					for(int j = 0; j < burstTime; j++) {
						if(queue[i] == queueNumber) {
							if (j == 0) {
								//TimesPanel.responseTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
							}
							
							if (i == 0 && j == 0) {
								queue1.initialProcess(fcfsProcess[i]);
								System.out.print(fcfsProcess[i].getProcessID() + " ");
								fcfsProcess[i].setBurstTime(fcfsProcess[i].getBurstTime() - 1);
								//GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter);
							} else {
								queue1.enqueue(fcfsProcess[i]);
								System.out.print(fcfsProcess[i].getProcessID() + " ");
								fcfsProcess[i].setBurstTime(fcfsProcess[i].getBurstTime() - 1);
								//GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter);
							}
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}						
					}
					
					//TimesPanel.turnaroundTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
					//TimesPanel.waitingTime(fcfsProcess[i].getBurstTime(), fcfsProcess[i].getProcessID());
				}
			}
		}.start();
		
		mainProcess = fcfsProcess;
		System.out.println();
		for(int i=0; i < fcfsProcess.length; i++) {
			System.out.print(fcfsProcess[i].getProcessID() + " ");
		}
		System.out.println();
		
	}
	
	public void SJF(Process[] process, int queueNumber) {
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
				
				Queue queue1 = new Queue();
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
					if(queue[i] == queueNumber) {
						int burstTime = sjfProcess[i].getBurstTime();
						if (i == 0 && SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1) != -1) {
							for(int j = 0; j < burstTime; j++) {
								if (j == 0) {
									//TimesPanel.responseTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
									queue1.initialProcess(sjfProcess[i]);
									//GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter);								
									System.out.print(sjfProcess[i].getProcessID() + " ");
									sjfProcess[i].setBurstTime(sjfProcess[i].getBurstTime() - 1);
								} else {
									queue1.enqueue(sjfProcess[i]);
									//GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), counter);
									System.out.print(sjfProcess[i].getProcessID() + " ");
									sjfProcess[i].setBurstTime(sjfProcess[i].getBurstTime() - 1);
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
								burstTime = sjfProcess[j].getBurstTime();
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
									for(int k = 0; k < burstTime; k++) {
										if (k == 0) {
											//TimesPanel.responseTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
										}
										
										queue1.enqueue(sjfProcess[j]);
										//GanttChartPanel.addToGanttChart(sjfProcess[j].getProcessID(), counter);
										System.out.print(sjfProcess[j].getProcessID() + " ");
										sjfProcess[j].setBurstTime(sjfProcess[j].getBurstTime() - 1);
										
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
			}
		}.start();
		mainProcess = sjfProcess;
	}
	
	public void SRTF(Process[] process, int queueNumber) {
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
				
				Queue queue1 = new Queue();
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
					int burstTime;
					
					//if(queue[i] == queueNumber) {
						for(int j = 0; j < srtfProcess.length; j++) {
							if (i == srtfProcess[j].getArrivalTime()) {
								isAvailable[j] = true;
								tempB[j] = burst[j];
							} else { continue; }
						}
						
						for(int j = 0; j < srtfProcess.length; j++) {
							if (isAvailable[j] = true && SchedulingAlgorithmsUtilities.getSmallestNum(tempB,0) != -1 && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(tempB,0) && flag == false && queue[j] == queueNumber) {
								if (!responseTimeDone[j]) {
									//TimesPanel.responseTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
									responseTimeDone[j] = true;
								}
								if (i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
									queue1.initialProcess(srtfProcess[j]);
									burst[j]--;
									tempB[j]--;
									flag = true;
									//GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter);
									System.out.print(srtfProcess[j].getProcessID() + " ");
									//srtfProcess[j].setBurstTime(srtfProcess[j].getBurstTime() - 1);
								} else {
									queue1.enqueue(srtfProcess[j]);
									burst[j]--;
									tempB[j]--;
									flag = true;
									//GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), counter);
									System.out.print(srtfProcess[j].getProcessID() + " ");
									//srtfProcess[j].setBurstTime(srtfProcess[j].getBurstTime() - 1);
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
					//}					
				}
				for(int i = 0; i < srtfProcess.length; i++) {
					srtfProcess[i].setBurstTime(burst[i]);
				}
			}
		}.start();
		
		mainProcess = srtfProcess;
		System.out.println();
	}
	
	public void Prio(Process[] process, int queueNumber) {
		Process[] prioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		int[] burst = new int[prioProcess.length];;		
		
		new Thread() {
			public void run() {
				boolean flagger = false;
				boolean[] isAvailable = new boolean[prioProcess.length], responseTimeDone = new boolean[prioProcess.length];
				
				int[] tempB = new int[prioProcess.length], prio = new int[prioProcess.length], arrival = new int[prioProcess.length];
			
				for(int i = 0; i < prioProcess.length; i++){
					isAvailable[i] = false;
					responseTimeDone[i] = false;
					burst[i] = prioProcess[i].getBurstTime();
					tempB[i] = 0;
					prio[i] = 0;
					arrival[i] = prioProcess[i].getArrivalTime();
				}
				
				Queue queue1 = new Queue();
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
							if(queue[j] == queueNumber) {
								if(isAvailable[j] = true && SchedulingAlgorithmsUtilities.getSmallestNum(prio, 0) == prio[j] && flag == false) {
									if (!responseTimeDone[j]) {
										//TimesPanel.responseTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
										responseTimeDone[j] = true;
									}
									
									if(i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
										queue1.initialProcess(prioProcess[j]);
										burst[j]--;
										flag = true;
										//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
										System.out.print(prioProcess[j].getProcessID() + " ");
										//prioProcess[j].setBurstTime(prioProcess[j].getBurstTime() - 1);
										
										if(burst[j] == 0) { prio[j] = -1; }
									} else {
										queue1.enqueue(prioProcess[j]);
										burst[j]--;
										flag = true;
										//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
										System.out.print(prioProcess[j].getProcessID() + " ");
										//prioProcess[j].setBurstTime(prioProcess[j].getBurstTime() - 1);
										
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
						}	
					} else if(done) {
						for(int j = 0; j < prioProcess.length; j++) {
							if(SchedulingAlgorithmsUtilities.getSmallestNum(prio, 0) == prio[j] && flag == false && queue[j] == queueNumber) {
								queue1.enqueue(prioProcess[j]);
								burst[j]--;
								flag = true;
								//GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), counter);
								System.out.print(prioProcess[j].getProcessID() + " ");
								//prioProcess[j].setBurstTime(prioProcess[j].getBurstTime() - 1);
								
								if(burst[j] == 0) { prio[j] = -1; }
							}
							
							if (burst[j] == 0) {
								//TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								//TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
							}
						}
					}					
				}	
				for(int i = 0; i < prioProcess.length; i++) {
					prioProcess[i].setBurstTime(burst[i]);
				}
			}			
		}.start();
		
		mainProcess = prioProcess;				
	}
	
	public void NPrio(Process[] process, int queueNumber) {
Process[] nPrioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		new Thread() {
			public void run() {
				
				int prio[] = new int[nPrioProcess.length];
				
				for(int i = 0; i < nPrioProcess.length; i++) {
					prio[i] = nPrioProcess[i].getPriority();
				}
				
				Queue queue1 = new Queue();
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
					int burstTime = nPrioProcess[i].getBurstTime();
					if(queue[i] == queueNumber) {
						if (i == 0) {
							for(int j = 0; j < burstTime; j++) {							
								if (j == 0) {
									queue1.initialProcess(nPrioProcess[i]);
									//TimesPanel.responseTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
									//GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter);
									System.out.print(nPrioProcess[i].getProcessID() + " ");
									nPrioProcess[i].setBurstTime(nPrioProcess[i].getBurstTime() - 1);
								} else {
									queue1.enqueue(nPrioProcess[i]);
									//GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), counter);
									System.out.print(nPrioProcess[i].getProcessID() + " ");
									nPrioProcess[i].setBurstTime(nPrioProcess[i].getBurstTime() - 1);
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
								burstTime = nPrioProcess[i].getBurstTime();
								
								if (nPrioProcess[j].getPriority() == SchedulingAlgorithmsUtilities.getSmallestNum(prio, 1) && prio[j] != -1 && flag == false) {						
									for(int k = 0; k < burstTime; k++) {
										if (k == 0) {
											//TimesPanel.responseTime(counter, nPrioProcess[j].getArrivalTime(), nPrioProcess[j].getProcessID());
										}
										
										queue1.enqueue(nPrioProcess[j]);
										//GanttChartPanel.addToGanttChart(nPrioProcess[j].getProcessID(), counter);
										System.out.print(nPrioProcess[j].getProcessID() + " ");
										nPrioProcess[j].setBurstTime(nPrioProcess[j].getBurstTime() - 1);
										
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
					}										
					//TimesPanel.turnaroundTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
					//TimesPanel.waitingTime(nPrioProcess[i].getBurstTime(), nPrioProcess[i].getProcessID());
				}
			}
		}.start();
		mainProcess = nPrioProcess;
	}
	
	public static void main(String[] args) {
		System.out.println("MLFQ");
		
		Process p[] = {new Process(1, 5, 7, 9), 
				   new Process(2, 1, 5, 2), 
				   new Process(3, 2, 3, 5), 
				   new Process(4, 6, 1, 1), 
				   new Process(5, 4, 2, 6), 
				   new Process(6, 3, 1, 10)}; 
		Process temp[] = p;
		int algo[] = {6, 2};
		int quantumTime[] = {0, 0};
		
		mlfq mlfq = new mlfq(p, true, algo, quantumTime);
		
		//System.out.println("SJF Print");
		//mlfq m = new mlfq();
		
		//m.SRTF(temp, 1);
		
		//m.mlfq(p, true, algo,quantumTime);
		// TODO Auto-generated method stub
	}

}
