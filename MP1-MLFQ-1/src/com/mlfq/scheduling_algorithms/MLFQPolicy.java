package com.mlfq.scheduling_algorithms;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class MLFQPolicy {
	
	private Queue[] queue;
	private int queueIndex, ganttChartCounter;
	private Process[] mlfqProcess;
	private int[] originalBursts;
	
	/*
	 * for testing use
	 */
	public MLFQPolicy(Process[] process, int[] selectedAlgo, int[] quantumTime) {
		
		queue = new Queue[selectedAlgo.length];
		for (int i = 0; i < selectedAlgo.length; i++) {
			queue[i] = new Queue();
		}
		
		queueIndex = 0;
		ganttChartCounter = 0;
		mlfqProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		originalBursts = new int[mlfqProcess.length];
		for (int i = 0; i < mlfqProcess.length; i++) {
			originalBursts[i] = mlfqProcess[i].getBurstTime();
		}
		
		int[] algorithms = new int[selectedAlgo.length];
		int[] quantumTimes = new int[quantumTime.length];
		for (int i = 0; i < selectedAlgo.length; i++) {
			algorithms[i] = selectedAlgo[i];
			quantumTimes[i] = quantumTime[i];
		}
		
		queue[0].initialProcess(mlfqProcess[0]);
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	/*
	 * for actual use
	 */
	public MLFQPolicy(Process[] process, ArrayList<JComboBox<String>> selectedAlgo, ArrayList<JTextField> quantumTime) {
		
		queue = new Queue[selectedAlgo.size()];
		for (int i = 0; i < selectedAlgo.size(); i++) {
			queue[i] = new Queue();
		}
		
		queueIndex = 0;
		ganttChartCounter = 0;
		mlfqProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		int[] algorithms = new int[selectedAlgo.size()];
		int[] quantumTimes = new int[quantumTime.size()];
		for (int i = 0; i < selectedAlgo.size(); i++) {
			algorithms[i] = selectedAlgo.get(i).getSelectedIndex();
			quantumTimes[i] = Integer.parseInt(quantumTime.get(i).getText());
		}
		
		queue[0].initialProcess(mlfqProcess[0]);
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void checkAlgorithm(int[] algorithms, int[] quantumTimes) {
		
		new Thread() {
			public void run() {
				try {
					switch(algorithms[queueIndex]) {
						case 0:
							FCFS(algorithms, quantumTimes);
							break;
						case 1:
							SJF(algorithms, quantumTimes);
							break;
						case 2:
							SRTF(algorithms, quantumTimes);
							break;
						case 3:
							Prio(algorithms, quantumTimes);
							break;
						case 4:
							NPrio(algorithms, quantumTimes);
							break;
						case 5:
							RoundRobin(quantumTimes[queueIndex], algorithms, quantumTimes);
							break;
						default:
							System.out.println("Internal error. Exiting....");
							break;
					}
				} catch (ArrayIndexOutOfBoundsException ex) { }
			}
		}.start();
	}
	
	private void RoundRobin(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = queue[queueIndex].dequeue();
	
		// response time
	
		for (int i = ganttChartCounter; i < currentProcess.getArrivalTime(); i++) {
			System.out.print("0|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(0, ganttChartCounter, queueIndex);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
		}
		
		for (int i = 0; i < quantumTime; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				 if (ganttChartCounter == mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getProcessID() != currentProcess.getProcessID()) {
					 try {
						 queue[queueIndex].enqueue(mlfqProcess[j]);
					 } catch (NullPointerException ex) {
						 queue[queueIndex].initialProcess(mlfqProcess[j]);
					 }
				 }
			}
		}
		
		if (currentProcess.getBurstTime() != 0) {
			try {
				queue[queueIndex + 1].enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue[queueIndex + 1].initialProcess(currentProcess);
			}
		} else {
			// turnaround time, waiting time
		}
		
		if (queue[queueIndex].getIndex() == 0) {
			queueIndex++;
			System.out.println();
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void FCFS(int[] algorithms, int[] quantumTimes) {
		
			Process currentProcess = queue[queueIndex].dequeue();
			
			int burst = 0;
			try {
				burst = currentProcess.getBurstTime();
			} catch (NullPointerException ex) { }
			for (int i = 0; i < burst; i++) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() != 0) {
					try {
						queue[0].enqueue(mlfqProcess[j]);
					} catch (NullPointerException ex) {
						queue[0].initialProcess(mlfqProcess[j]);
					}
				}
			}
			
			if (queue[queueIndex].getIndex() == 0) {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			} else {
				// turnaround time, waiting time
			}
			
			checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void SJF(int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = null;
		if (queue[queueIndex].getIndex() == 1) {
			currentProcess = queue[queueIndex].dequeue();
		} else {
			Process[] sjfProcess = new Process[queue[queueIndex].getIndex()];
			int index = queue[queueIndex].getIndex();
			for (int i = 0; i < index; i++) {
				sjfProcess[i] = queue[queueIndex].dequeue();
			}
			
			Process temp = null;
			for (int i = 0; i < sjfProcess.length - 1; i++) {
				 for(int j = 0; j < sjfProcess.length - 1; j++) {
					 if (sjfProcess[i].getBurstTime() > sjfProcess[j + 1].getBurstTime()) {
						temp = sjfProcess[i];
						sjfProcess[i] = sjfProcess[j + 1];
						sjfProcess[j + 1] = temp;
					 }
				}
			}
			
			for (int i = 0; i < sjfProcess.length; i++) {
				try {
					queue[queueIndex].enqueue(sjfProcess[i]);
				} catch (NullPointerException ex) {
					queue[queueIndex].initialProcess(sjfProcess[i]);
				}
			}
			
			currentProcess = queue[queueIndex].dequeue();
		}
		
		int burst = 0;
		try {
			burst = currentProcess.getBurstTime();
		} catch (NullPointerException ex) { }
		
		for (int i = 0; i < burst; i++) {
			System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
			currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
		}

		for (int j = 0; j < mlfqProcess.length; j++) {
			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() != 0) {
				try {
					queue[0].enqueue(mlfqProcess[j]);
				} catch (NullPointerException ex) {
					queue[0].initialProcess(mlfqProcess[j]);
				}
			}
		}
		
		if (queue[queueIndex].getIndex() == 0) {
			if (queue[0].getIndex() != 0) {
				queueIndex = 0;
			} else {
				queueIndex++;
			}
			System.out.println();
		} else {
			// turnaround time, waiting time
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void SRTF(int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = null;
		
		if (queue[queueIndex].getIndex() == 1) {
			currentProcess = queue[queueIndex].dequeue();
		} else {
			Process[] sjfProcess = new Process[queue[queueIndex].getIndex()];
			int index = queue[queueIndex].getIndex();
			for (int i = 0; i < index; i++) {
				sjfProcess[i] = queue[queueIndex].dequeue();
			}
			
			Process temp = null;
			for (int i = 0; i < sjfProcess.length - 1; i++) {
				 for(int j = 0; j < sjfProcess.length - 1; j++) {
					 if (sjfProcess[i].getBurstTime() > sjfProcess[j + 1].getBurstTime()) {
						temp = sjfProcess[i];
						sjfProcess[i] = sjfProcess[j + 1];
						sjfProcess[j + 1] = temp;
					 }
				}
			}
			
			for (int i = 0; i < sjfProcess.length; i++) {
				try {
					queue[queueIndex].enqueue(sjfProcess[i]);
				} catch (NullPointerException ex) {
					queue[queueIndex].initialProcess(sjfProcess[i]);
				}
			}
			
			currentProcess = queue[queueIndex].dequeue();
		}
		
		int burst = 0;
		try {
			burst = currentProcess.getBurstTime();
		} catch (NullPointerException ex) { }
		
		boolean ifContinue = true;
		for (int i = 0; i < burst; i++) {
			System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
			currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() != 0 && currentProcess.getBurstTime() > mlfqProcess[j].getBurstTime()) {
					try {
						queue[0].enqueue(mlfqProcess[j]);
					} catch (NullPointerException ex) {
						queue[0].initialProcess(mlfqProcess[j]);
					}
					
					ifContinue = false;
				}
			}
			
			if(!ifContinue) {
				queue[queueIndex].enqueue(currentProcess);
				queueIndex = 0;
				System.out.println();
				break;
			}
		}
		
		for (int j = 0; j < mlfqProcess.length; j++) {
			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() == originalBursts[j]) {
				try {
					queue[0].enqueue(mlfqProcess[j]);
				} catch (NullPointerException ex) {
					queue[0].initialProcess(mlfqProcess[j]);
				}
			}
		}
		
		if (ifContinue) {
			if (queue[queueIndex].getIndex() == 0) {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			} else {
				// turnaround time, waiting time
			}
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void Prio(int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = null;
		if (queue[queueIndex].getIndex() == 1) {
			currentProcess = queue[queueIndex].dequeue();
		} else {
			Process[] prioProcess = new Process[queue[queueIndex].getIndex()];
			int index = queue[queueIndex].getIndex();
			for (int i = 0; i < index; i++) {
				prioProcess[i] = queue[queueIndex].dequeue();
			}
			
			Process temp = null;
			for (int i = 0; i < prioProcess.length - 1; i++) {
				 for(int j = 0; j < prioProcess.length - 1; j++) {
					 if (prioProcess[i].getPriority() > prioProcess[j + 1].getPriority()) {
						temp = prioProcess[i];
						prioProcess[i] = prioProcess[j + 1];
						prioProcess[j + 1] = temp;
					 }
				}
			}
			
			for (int i = 0; i < prioProcess.length; i++) {
				try {
					queue[queueIndex].enqueue(prioProcess[i]);
				} catch (NullPointerException ex) {
					queue[queueIndex].initialProcess(prioProcess[i]);
				}
			}
			
			currentProcess = queue[queueIndex].dequeue();
		}
		
		int burst = 0;
		try {
			burst = currentProcess.getBurstTime();
		} catch (NullPointerException ex) { }
		
		for (int i = 0; i < burst; i++) {
			System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
			currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
		}

		for (int j = 0; j < mlfqProcess.length; j++) {
			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() != 0) {
				try {
					queue[0].enqueue(mlfqProcess[j]);
				} catch (NullPointerException ex) {
					queue[0].initialProcess(mlfqProcess[j]);
				}
			}
		}
		
		if (queue[queueIndex].getIndex() == 0) {
			if (queue[0].getIndex() != 0) {
				queueIndex = 0;
			} else {
				queueIndex++;
			}
			System.out.println();
		} else {
			// turnaround time, waiting time
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void NPrio(int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = null;
		
		if (queue[queueIndex].getIndex() == 1) {
			currentProcess = queue[queueIndex].dequeue();
		} else {
			Process[] nPrioProcess = new Process[queue[queueIndex].getIndex()];
			int index = queue[queueIndex].getIndex();
			for (int i = 0; i < index; i++) {
				nPrioProcess[i] = queue[queueIndex].dequeue();
			}
			
			Process temp = null;
			for (int i = 0; i < nPrioProcess.length - 1; i++) {
				 for(int j = 0; j < nPrioProcess.length - 1; j++) {
					 if (nPrioProcess[i].getPriority() > nPrioProcess[j + 1].getPriority()) {
						temp = nPrioProcess[i];
						nPrioProcess[i] = nPrioProcess[j + 1];
						nPrioProcess[j + 1] = temp;
					 }
				}
			}
			
			for (int i = 0; i < nPrioProcess.length; i++) {
				try {
					queue[queueIndex].enqueue(nPrioProcess[i]);
				} catch (NullPointerException ex) {
					queue[queueIndex].initialProcess(nPrioProcess[i]);
				}
			}
			
			currentProcess = queue[queueIndex].dequeue();
		}
		
		int burst = 0;
		try {
			burst = currentProcess.getBurstTime();
		} catch (NullPointerException ex) { }
		
		boolean ifContinue = true;
		for (int i = 0; i < burst; i++) {
			System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
			currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() != 0 && currentProcess.getPriority() > mlfqProcess[j].getPriority()) {
					try {
						queue[0].enqueue(mlfqProcess[j]);
					} catch (NullPointerException ex) {
						queue[0].initialProcess(mlfqProcess[j]);
					}
					
					ifContinue = false;
				}
			}
			
			if(!ifContinue) {
				queue[queueIndex].enqueue(currentProcess);
				queueIndex = 0;
				System.out.println();
				break;
			}
		}
		
		for (int j = 0; j < mlfqProcess.length; j++) {
			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() == originalBursts[j]) {
				try {
					queue[0].enqueue(mlfqProcess[j]);
				} catch (NullPointerException ex) {
					queue[0].initialProcess(mlfqProcess[j]);
				}
			}
		}
		
		if (ifContinue) {
			if (queue[queueIndex].getIndex() == 0) {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			} else {
				// turnaround time, waiting time
			}
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	public static void main(String[] args) {
		
		System.out.println("My MLFQ\n");
		
//		Process process[] = {new Process(1, 5, 7, 9), 
//				   			 new Process(2, 1, 5, 2), 
//				   			 new Process(3, 2, 3, 5), 
//				   			 new Process(4, 6, 1, 1), 
//				   			 new Process(5, 4, 2, 6), 
//				   			 new Process(6, 3, 1, 10)};
		
		Process process[] = {new Process(1, 10, 13, 4), 
	   			 			 new Process(2, 15, 6, 2), 
	   			 			 new Process(3, 20, 8, 1), 
	   			 			 new Process(4, 25, 5, 3)};
		
		int algo[] = {5, 4};
		int quantumTime[] = {4, 0};
		
		new MLFQPolicy(process, algo, quantumTime);
	}
}
