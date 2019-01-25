package com.mlfq.scheduling_algorithms;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class FixedTimeSlot {
	
	private Queue queue;
	private int queueIndex, ganttChartCounter;
	private Process[] mlfqProcess;
	private int[] originalBursts, counter;
	private boolean[] isResponseDone, isDone, hasArrived;
	
	public FixedTimeSlot(Process[] process, ArrayList<JComboBox<String>> selectedAlgo, ArrayList<JTextField> quantumTime) {
		
		queue = new Queue();
		counter = new int[selectedAlgo.size()];
		for (int i = 0; i < selectedAlgo.size(); i++) {
			counter[i] = 0;
		}
		
		queueIndex = 0;
		ganttChartCounter = 0;
		mlfqProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length - 1, 0);
		
		originalBursts = new int[mlfqProcess.length];
		isResponseDone = new boolean[mlfqProcess.length];
		isDone = new boolean[mlfqProcess.length];
		hasArrived = new boolean[mlfqProcess.length];
		for (int i = 0; i < mlfqProcess.length; i++) {
			originalBursts[i] = mlfqProcess[i].getBurstTime();
			isResponseDone[i] = false;
			isDone[i] = false;
			hasArrived[i] = false;
		}
		
		int[] algorithms = new int[selectedAlgo.size()];
		int[] quantumTimes = new int[quantumTime.size()];
		for (int i = 0; i < selectedAlgo.size(); i++) {
			algorithms[i] = selectedAlgo.get(i).getSelectedIndex();
			quantumTimes[i] = Integer.parseInt(quantumTime.get(i).getText());
		}
		
		queue.initialProcess(mlfqProcess[0]);
		
		for (int i = 1; i < mlfqProcess.length; i++) {
			if (mlfqProcess[0].getArrivalTime() == mlfqProcess[i].getArrivalTime()) {
				queue.enqueue(mlfqProcess[i]);
			}
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void checkAlgorithm(int[] algorithms, int[] quantumTimes) {
		
		new Thread() {
			public void run() {
				int count = 0;
				for (int i = 0; i < mlfqProcess.length; i++) {
					if (mlfqProcess[i].getBurstTime() == 0) {
						count++;
					}
				}
				if (count == mlfqProcess.length) {
					queueIndex = counter.length + 1;
				}
				
				if (queue.getIndex() == 0 && count != mlfqProcess.length) {
					for (int i = 0; i < mlfqProcess.length; i++) {
						if (mlfqProcess[i].getBurstTime() != 0) {
							queue.initialProcess(mlfqProcess[i]);
							break;
						}
					}
				}
				
				try {
					switch(algorithms[queueIndex]) {
						case 0:
							FCFS(quantumTimes[queueIndex], algorithms, quantumTimes);
							break;
						case 1:
							SJF(quantumTimes[queueIndex], algorithms, quantumTimes);
							break;
						case 2:
							SRTF(quantumTimes[queueIndex], algorithms, quantumTimes);
							break;
						case 3:
							Prio(quantumTimes[queueIndex], algorithms, quantumTimes);
							break;
						case 4:
							NPrio(quantumTimes[queueIndex], algorithms, quantumTimes);
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
	
	// 0 - arrival time; 1 - burst time; 2 - priority
	private Process getSmallest(int constraint) {
		
		Process[] process = new Process[queue.getIndex()];
		int end = queue.getIndex();
		for (int i = 0; i < end; i++) {
			process[i] = queue.dequeue();
		}
		
		Process temp = null;
		for (int i = 0; i < process.length - 1; i++) {
			for (int j = 0; j < process.length - 1; j++) {
				if (constraint == 0) { // arrival time
					if (process[i].getArrivalTime() > process[j + 1].getArrivalTime()) {
						temp = process[i];
						process[i] = process[j + 1];
						process[j + 1] = temp;
					}
				} else if (constraint == 1) { // burst time
					if (process[i].getBurstTime() > process[j + 1].getBurstTime()) {
						temp = process[i];
						process[i] = process[j + 1];
						process[j + 1] = temp;
					}
				} else if (constraint == 2) { // priority
					if (process[i].getPriority() > process[j + 1].getPriority()) {
						temp = process[i];
						process[i] = process[j + 1];
						process[j + 1] = temp;
					}
				}
			}
		}
		
		for (int i = 0; i < process.length; i++) {
			try {
				queue.enqueue(process[i]);
			} catch (NullPointerException ex) {
				queue.initialProcess(process[i]);
			}
		}
		
		return queue.dequeue();
	}
	
	private void printEmptyProcess(Process currentProcess) {
		
		for (int i = ganttChartCounter; i < currentProcess.getArrivalTime(); i++) {
			System.out.print("0|" + ganttChartCounter + " ");
			GanttChartPanel.addToGanttChart(0, ganttChartCounter, queueIndex);
			ganttChartCounter++;
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) { }
		}
	}
	
	private void checkIfProcessArriving(Process currentProcess) {
		
		for (int j = 0; j < mlfqProcess.length; j++) {
			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && !hasArrived[j] && currentProcess.getProcessID() != mlfqProcess[j].getProcessID()) {
				try {
					queue.enqueue(mlfqProcess[j]);
				} catch (NullPointerException ex) {
					queue.initialProcess(mlfqProcess[j]);
				}
				hasArrived[j] = true;
			}
		}
	}
	
	private void displayResponseTime(Process currentProcess) {
		
		for (int i = 0; i < mlfqProcess.length; i++) {
			if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isResponseDone[i]) {
				TimesPanel.responseTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
				isResponseDone[i] = true;
			}
		}
	}
	
	private void displayTurnaroundAndWaitingTime(Process currentProcess) {
		
		for (int i = 0; i < originalBursts.length; i++) {
			if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
				TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
				TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
				isDone[i] = true;
			}
		}
	}
	
	private void FCFS(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = getSmallest(0);
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
		}
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}

		checkIfProcessArriving(currentProcess);
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void SJF(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = getSmallest(1);
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
		}
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}
		
		checkIfProcessArriving(currentProcess);
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void SRTF(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = getSmallest(1);
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		boolean ifContinue = false;
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime()) {
					if (mlfqProcess[j].getBurstTime() != 0 && currentProcess.getBurstTime() > mlfqProcess[j].getBurstTime()) {
						try {
							queue.enqueue(mlfqProcess[j]);
						} catch (NullPointerException ex) {
							queue.initialProcess(mlfqProcess[j]);
						}
						
						ifContinue = false;
					}
				}
			}
			
			if(!ifContinue) {
				break;
			}
		}
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}
		
		checkIfProcessArriving(currentProcess);
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void Prio(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = getSmallest(2);
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		boolean ifContinue = false;
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
			
			for (int j = 0; j < mlfqProcess.length; j++) {
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime()) {
					if (mlfqProcess[j].getBurstTime() != 0 && currentProcess.getBurstTime() > mlfqProcess[j].getBurstTime()) {
						try {
							queue.enqueue(mlfqProcess[j]);
						} catch (NullPointerException ex) {
							queue.initialProcess(mlfqProcess[j]);
						}
						
						ifContinue = false;
					}
				}
			}
			
			if(!ifContinue) {
				break;
			}
		}
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}
		
		checkIfProcessArriving(currentProcess);
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void NPrio(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = getSmallest(2);
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
		}
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}
		
		checkIfProcessArriving(currentProcess);
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void RoundRobin(int quantumTime, int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = queue.dequeue();
		printEmptyProcess(currentProcess);
		displayResponseTime(currentProcess);
		
		int burst = quantumTime - counter[queueIndex];
		for (int i = 0; i < burst; i++) {
			if (currentProcess.getBurstTime() != 0) {
				System.out.print("P" + currentProcess.getProcessID() + "|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(currentProcess.getProcessID(), ganttChartCounter, queueIndex);
				currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
				ganttChartCounter++;
				counter[queueIndex]++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
		}
		
		checkIfProcessArriving(currentProcess);
		
		if (currentProcess.getBurstTime() == 0) {
			displayTurnaroundAndWaitingTime(currentProcess);
		} else {
			try {
				queue.enqueue(currentProcess);
			} catch (NullPointerException ex) {
				queue.initialProcess(currentProcess);
			}
			
			if (counter[queueIndex] == quantumTime) {
				counter[queueIndex] = 0;
				
				if (queueIndex == counter.length - 1) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
			}
			
			System.out.println();
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
}
