package com.mlfq.scheduling_algorithms;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class FixedTimeSlot{
	Process[] mainProcess;
	boolean priority = true; //
	int[] queue;
	int timeElapsed, totalConsumedTime, ganttChartCounter, selectedAlgoSize;
	//note: algorithm: {1:RR, 2:FCFS, 3:SJF, 4:SRTF, 5:Prio, 6:NPrio}
	
	public FixedTimeSlot(Process[] process, boolean priority, ArrayList<JComboBox<String>> selectedAlgo, ArrayList<JTextField> quantumTime) {
		
		this.mainProcess = process;
		this.priority = priority;
		this.queue = new int[process.length];
		this.timeElapsed = 0;
		this.totalConsumedTime = 0;
		this.ganttChartCounter = 0;
		this.selectedAlgoSize = selectedAlgo.size();
		Process[] temp = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length-1, 0);
		
		for(int i = 0; i < queue.length; i++) {
			queue[i] = 1;
		}
		
		int[] arrivalTimes = new int[process.length];
		for (int i = 0; i < process.length; i++) {
			arrivalTimes[i] = temp[i].getArrivalTime();
		}
		totalConsumedTime = SchedulingAlgorithmsUtilities.totalTime(1, temp) + SchedulingAlgorithmsUtilities.getSmallestNum(arrivalTimes, 1);
		
		new Thread() {
			public void run() {
				for(int i = 1; i <= selectedAlgo.size(); i++) {
					getProcess(selectedAlgo.get(i - 1).getSelectedIndex(), mainProcess, Integer.parseInt(quantumTime.get(i - 1).getText()), i);
				}
			}
		}.start();
	}
	
	public void getProcess(int algorithm, Process[] process, int quantumTime, int queueNumber) {
		
		switch (algorithm) {
			case 5: 
				RoundRobin(process, quantumTime, queueNumber);
				break;
			case 0:
				FCFS(process, quantumTime, queueNumber);
				break;
			case 1:
				SJF(process, quantumTime, queueNumber);
				break;
			case 2:
				SRTF(process, quantumTime, queueNumber);
				break;
			case 3:
				Prio(process, quantumTime, queueNumber);
				break;
			case 4: 
				NPrio(process, quantumTime, queueNumber);
				break;
			default:
				System.out.print("Invalid Input");
		}
	}
	
	public void RoundRobin(Process[] process, int quantumTime, int queueNumber) {
		
		Process[] roundRobinProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		int burstTime;
		
		if (timeElapsed < roundRobinProcess[0].getArrivalTime()) {
			if (queueNumber == 1) {
				for (int i = 0; i < roundRobinProcess[0].getArrivalTime(); i++) {
					System.out.print("0 ");
					GanttChartPanel.addToGanttChart(0, ganttChartCounter, queueNumber);
					timeElapsed++;
					ganttChartCounter++;
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) { }
				}
			}
		}
		
		if (queueNumber == selectedAlgoSize) {
			new RoundRobin(process, quantumTime, ganttChartCounter, queueNumber);
		} else {
			for (int i = 0; i < roundRobinProcess.length; i++) {
				int counter = 0;
				burstTime = roundRobinProcess[i].getBurstTime();
				boolean flag = true;
				
				for (int j = 0; j < burstTime; j++) {
					if (counter < quantumTime && queue[i] == queueNumber) {
						counter++;
						timeElapsed = counter;
						roundRobinProcess[i].setBurstTime(roundRobinProcess[i].getBurstTime() - 1);
						System.out.print(roundRobinProcess[i].getProcessID() + " ");
						GanttChartPanel.addToGanttChart(roundRobinProcess[i].getProcessID(), ganttChartCounter, queueNumber);
						
						ganttChartCounter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					} else {
						if (flag == true) {
							queue[i]++;	
							flag = false;
						}
					}				
				}				
			}
		}
		
		mainProcess = roundRobinProcess;
		System.out.println();
	}
	
	public void FCFS(Process[] process, int quantumTime, int queueNumber) {
		
		Process[] fcfsProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length-1, 0);
		
		Queue queue1 = new Queue();
		
		for(int i = 0; i < fcfsProcess.length; i++) {
			int burstTime = fcfsProcess[i].getBurstTime();
			int ftsCounter = 0;
			
			for(int j = 0; j < burstTime; j++) {
				if(queue[i] == queueNumber) {
					if (j == 0) {
						//TimesPanel.responseTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
					}
					
					if (i == 0 && j == 0) {
						queue1.initialProcess(fcfsProcess[i]);
						System.out.print(fcfsProcess[i].getProcessID() + " ");
						fcfsProcess[i].setBurstTime(fcfsProcess[i].getBurstTime() - 1);
						GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), ganttChartCounter, queueNumber);
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize) {
								queue[i] = 1;
							}else 
								queue[i]++;
						}
					} else {
						try {
							queue1.enqueue(fcfsProcess[i]);
						} catch (NullPointerException ex) {
							queue1.initialProcess(fcfsProcess[i]);
						}
						
						System.out.print(fcfsProcess[i].getProcessID() + " ");
						fcfsProcess[i].setBurstTime(fcfsProcess[i].getBurstTime() - 1);
						GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), ganttChartCounter, queueNumber);
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize) {
								queue[i] = 1;
							}else
								queue[i]++;
						}
					}
					
					timeElapsed++;
					ganttChartCounter++;
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) { }
				}						
			}
				
				//TimesPanel.turnaroundTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
				//TimesPanel.waitingTime(fcfsProcess[i].getBurstTime(), fcfsProcess[i].getProcessID());
		}
		
		mainProcess = fcfsProcess;
	}
	
	public void SJF(Process[] process, int quantumTime,int queueNumber) {
		
		Process[] sjfProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		int burst[] = new int[process.length], arrival[] = new int[process.length];
		boolean isAvailable[] = new boolean[process.length];
		
		for(int i = 0; i < process.length; i++) {
			burst[i] = 0;
			arrival[i] = sjfProcess[i].getArrivalTime();
		}
		
		Queue queue1 = new Queue();
		
		for(int i = 0; i < sjfProcess.length; i++) {
			if(queue[i] == queueNumber) {
				int burstTime = sjfProcess[i].getBurstTime();
				int ftsCounter = 0;
				
				if (i == 0 && SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1) != -1) {
					for(int j = 0; j < burstTime; j++) {
						if (j == 0) {
							//TimesPanel.responseTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
							queue1.initialProcess(sjfProcess[i]);
							GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), ganttChartCounter, queueNumber);								
							System.out.print(sjfProcess[i].getProcessID() + " ");
							sjfProcess[i].setBurstTime(sjfProcess[i].getBurstTime() - 1);
							ftsCounter++;
							if(ftsCounter == quantumTime) {
								if(queueNumber == selectedAlgoSize)
									queue[i] = 1;
								else
									queue[i]++;
							}
						} else {
							queue1.enqueue(sjfProcess[i]);
							GanttChartPanel.addToGanttChart(sjfProcess[i].getProcessID(), ganttChartCounter, queueNumber);
							System.out.print(sjfProcess[i].getProcessID() + " ");
							sjfProcess[i].setBurstTime(sjfProcess[i].getBurstTime() - 1);
							ftsCounter++;
							if(ftsCounter == quantumTime) {
								if(queueNumber == selectedAlgoSize)
									queue[i] = 1;
								else
									queue[i]++;
							}
						}
						
						timeElapsed++;
						ganttChartCounter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
					
					//TimesPanel.turnaroundTime(counter, sjfProcess[i].getArrivalTime(), sjfProcess[i].getProcessID());
					//TimesPanel.waitingTime(sjfProcess[i].getBurstTime(), sjfProcess[i].getProcessID());
					
					burst[i] = -1;
				} else {
					for(int j = 1; j < sjfProcess.length; j++) {
						if (burst[j] != -1 && ganttChartCounter > sjfProcess[j].getArrivalTime()) {
							isAvailable[j] = true;
							burst[j] = sjfProcess[j].getBurstTime();
						}
					}
					boolean flag = false;
					
					for(int j = 0; j < sjfProcess.length; j++) {
						burstTime = sjfProcess[j].getBurstTime();
						ftsCounter = 0;
						
						if (isAvailable[j] == true && burst[j] == SchedulingAlgorithmsUtilities.getSmallestNum(burst, 0) && flag == false) {
							for(int k = 0; k < burstTime; k++) {
								if (k == 0) {
									//TimesPanel.responseTime(counter, sjfProcess[j].getArrivalTime(), sjfProcess[j].getProcessID());
								}
								
								try {
									queue1.enqueue(sjfProcess[j]);
								} catch (NullPointerException ex) {
									queue1.initialProcess(sjfProcess[i]);
								}
								
								GanttChartPanel.addToGanttChart(sjfProcess[j].getProcessID(), ganttChartCounter, queueNumber);
								System.out.print(sjfProcess[j].getProcessID() + " ");
								sjfProcess[j].setBurstTime(sjfProcess[j].getBurstTime() - 1);
								ftsCounter++;
								if(ftsCounter == quantumTime) {
									if(queueNumber == selectedAlgoSize)
										queue[j] = 1;
									else
										queue[j]++;
								}
								
								timeElapsed++;
								ganttChartCounter++;
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
		mainProcess = sjfProcess;
	}
	
	public void SRTF(Process[] process, int quantumTime,int queueNumber) {
		
		Process[] srtfProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
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
		
		for(int i = timeElapsed; i < totalConsumedTime; i++) {
			boolean flag = false;
			
			for(int j = 0; j < srtfProcess.length; j++) {
				if (i == srtfProcess[j].getArrivalTime()) {
					isAvailable[j] = true;
					tempB[j] = burst[j];
				} else { continue; }
			}
			
			for(int j = 0; j < srtfProcess.length; j++) {
				int ftsCounter = 0;
				
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
						GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(srtfProcess[j].getProcessID() + " ");
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[j] = 1;
							else
								queue[j]++;
						}
					} else {
						try {
							queue1.enqueue(srtfProcess[j]);
						} catch (NullPointerException ex) {
							queue1.initialProcess(srtfProcess[j]);
						}
						
						burst[j]--;
						tempB[j]--;
						flag = true;
						GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(srtfProcess[j].getProcessID() + " ");
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[j] = 1;
							else
								queue[j]++;
						}
					}
				} else { continue; }
				
				timeElapsed++;
				ganttChartCounter++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) { }
				
				if (i == ((SchedulingAlgorithmsUtilities.totalTime(1, srtfProcess) + SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) - 1)) {
					while (burst[j] != 0) {
						queue1.enqueue(srtfProcess[j]);
						burst[j]--;
						tempB[j]--;
						flag = true;
						GanttChartPanel.addToGanttChart(srtfProcess[j].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(srtfProcess[j].getProcessID() + " ");
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[j] = 1;
							else
								queue[j]++;
						}
						
						timeElapsed++;
						ganttChartCounter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				if (burst[j] == 0) {
					//TimesPanel.turnaroundTime(counter, srtfProcess[j].getArrivalTime(), srtfProcess[j].getProcessID());
					//TimesPanel.waitingTime(srtfProcess[j].getBurstTime(), srtfProcess[j].getProcessID());
				}
			}
		}
		
		for(int i = 0; i < srtfProcess.length; i++) {
			srtfProcess[i].setBurstTime(burst[i]);
		}
		
		mainProcess = srtfProcess;
	}
	
	public void Prio(Process[] process, int quantumTime, int queueNumber) {
		
		Process[] prioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		int[] burst = new int[prioProcess.length];
		
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
		
		for(int i = timeElapsed; i < totalConsumedTime; i++) {			
			boolean flag = false, done = true, first = false;
			int ftsCounter = 0;
			
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
						if(isAvailable[j] = true && flag == false && burst[j] != 0) {
							if (!responseTimeDone[j]) {
								//TimesPanel.responseTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
								responseTimeDone[j] = true;
							}
							
							if(i == SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) {
								queue1.initialProcess(prioProcess[j]);
								burst[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), ganttChartCounter, queueNumber);
								System.out.print(prioProcess[j].getProcessID() + " ");								
								
								if(burst[j] == 0) { prio[j] = -1; }
								
								ftsCounter++;
								if(ftsCounter == quantumTime) {
									if(queueNumber == selectedAlgoSize)
										queue[j] = 1;
									else
										queue[j]++;
								}
							} else {
								try {
									queue1.enqueue(prioProcess[j]);
								} catch (NullPointerException ex) {
									queue1.initialProcess(prioProcess[j]);
								}
								
								burst[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), ganttChartCounter, queueNumber);
								System.out.print(prioProcess[j].getProcessID() + " ");								
								
								if(burst[j] == 0) { prio[j] = -1; }
								
								ftsCounter++;
								if(ftsCounter == quantumTime) {
									if(queueNumber == selectedAlgoSize)
										queue[j] = 1;
									else
										queue[j]++;
								}
							}
						} else { continue; }
						
						timeElapsed++;
						ganttChartCounter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
						if (i == ((SchedulingAlgorithmsUtilities.totalTime(1, prioProcess) + SchedulingAlgorithmsUtilities.getSmallestNum(arrival, 1)) - 1)) {
							while (burst[j] != 0) {
								queue1.enqueue(prioProcess[j]);
								burst[j]--;
								flag = true;
								GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), ganttChartCounter, queueNumber);
								System.out.print(prioProcess[j].getProcessID() + " ");
								ftsCounter++;
								if(ftsCounter == quantumTime) {
									if(queueNumber == selectedAlgoSize)
										queue[j] = 1;
									else
										queue[j]++;
								}
								
								timeElapsed++;
								ganttChartCounter++;
								try {
									Thread.sleep(100);
								} catch (InterruptedException ex) { }
							}
						}
						
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
						GanttChartPanel.addToGanttChart(prioProcess[j].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(prioProcess[j].getProcessID() + " ");
						
						if(burst[j] == 0) { prio[j] = -1; }
						
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[j] = 1;
							else
								queue[j]++;
						}
					}
					
					if (burst[j] == 0) {
						//TimesPanel.turnaroundTime(counter, prioProcess[j].getArrivalTime(), prioProcess[j].getProcessID());
						//TimesPanel.waitingTime(prioProcess[j].getBurstTime(), prioProcess[j].getProcessID());
					}
				}
			}
		}
		
		mainProcess = prioProcess;				
	}
	
	public void NPrio(Process[] process, int quantumTime, int queueNumber) {
		
		Process[] nPrioProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
				
		int prio[] = new int[nPrioProcess.length];
		
		for(int i = 0; i < nPrioProcess.length; i++) {
			prio[i] = nPrioProcess[i].getPriority();
		}
		
		Queue queue1 = new Queue();
		
		for(int i = 0; i < nPrioProcess.length; i++) {
			int ftsCounter = 0;
			
			if (i == 0) {
				for(int j = 0; j < nPrioProcess[i].getBurstTime(); j++) {							
					if (j == 0) {
						queue1.initialProcess(nPrioProcess[i]);
						//TimesPanel.responseTime(counter, nPrioProcess[i].getArrivalTime(), nPrioProcess[i].getProcessID());
						GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(nPrioProcess[i].getProcessID() + " ");
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[i] = 1;
							else
								queue[i]++;
						}						
					} else {
						queue1.enqueue(nPrioProcess[i]);
						GanttChartPanel.addToGanttChart(nPrioProcess[i].getProcessID(), ganttChartCounter, queueNumber);
						System.out.print(nPrioProcess[i].getProcessID() + " ");
						ftsCounter++;
						if(ftsCounter == quantumTime) {
							if(queueNumber == selectedAlgoSize)
								queue[i] = 1;
							else
								queue[i]++;
						}
					}
					
					timeElapsed++;
					ganttChartCounter++;
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
							
							try {
								queue1.enqueue(nPrioProcess[j]);
							} catch (NullPointerException ex) {
								queue1.initialProcess(nPrioProcess[i]);
							}
							
							GanttChartPanel.addToGanttChart(nPrioProcess[j].getProcessID(), ganttChartCounter, queueNumber);
							System.out.print(nPrioProcess[j].getProcessID() + " ");
							ftsCounter++;
							if(ftsCounter == quantumTime) {
								if(queueNumber == selectedAlgoSize)
									queue[j] = 1;
								else
									queue[j]++;
							}
							
							timeElapsed++;
							ganttChartCounter++;
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
		mainProcess = nPrioProcess;
	}
}
