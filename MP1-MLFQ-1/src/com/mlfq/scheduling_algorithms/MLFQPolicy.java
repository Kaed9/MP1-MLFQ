package com.mlfq.scheduling_algorithms;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.mlfq.data_structures.Process;
import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class MLFQPolicy {
	
	private Queue[] queue;
	private int queueIndex, ganttChartCounter;
	private Process[] mlfqProcess;
	private int[] originalBursts;
	private boolean[] isResponseDone, isDone;
	
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
		isResponseDone = new boolean[mlfqProcess.length];
		isDone = new boolean[mlfqProcess.length];
		for (int i = 0; i < mlfqProcess.length; i++) {
			originalBursts[i] = mlfqProcess[i].getBurstTime();
			isResponseDone[i] = false;
			isDone[i] = false;
		}
		
		int[] algorithms = new int[selectedAlgo.length];
		int[] quantumTimes = new int[quantumTime.length];
		for (int i = 0; i < selectedAlgo.length; i++) {
			algorithms[i] = selectedAlgo[i];
			quantumTimes[i] = quantumTime[i];
		}
		
		queue[0].initialProcess(mlfqProcess[0]);
		
		for (int i = 1; i < mlfqProcess.length; i++) {
			if (mlfqProcess[0].getArrivalTime() == mlfqProcess[i].getArrivalTime()) {
				queue[0].enqueue(mlfqProcess[i]);
			}
		}
		
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
		
		originalBursts = new int[mlfqProcess.length];
		isResponseDone = new boolean[mlfqProcess.length];
		isDone = new boolean[mlfqProcess.length];
		for (int i = 0; i < mlfqProcess.length; i++) {
			originalBursts[i] = mlfqProcess[i].getBurstTime();
			isResponseDone[i] = false;
			isDone[i] = false;
		}
		
		int[] algorithms = new int[selectedAlgo.size()];
		int[] quantumTimes = new int[quantumTime.size()];
		for (int i = 0; i < selectedAlgo.size(); i++) {
			algorithms[i] = selectedAlgo.get(i).getSelectedIndex();
			quantumTimes[i] = Integer.parseInt(quantumTime.get(i).getText());
		}
		
		queue[0].initialProcess(mlfqProcess[0]);
		
		for (int i = 1; i < mlfqProcess.length; i++) {
			if (mlfqProcess[0].getArrivalTime() == mlfqProcess[i].getArrivalTime()) {
				queue[0].enqueue(mlfqProcess[i]);
			}
		}
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void checkAlgorithm(int[] algorithms, int[] quantumTimes) {
		
		new Thread() {
			public void run() {
				if (queueIndex == queue.length) {
					for (int i = 0; i < mlfqProcess.length; i++) {
						if (mlfqProcess[i].getBurstTime() != 0) {
							queue[0].initialProcess(mlfqProcess[i]);
							queueIndex = 0;
							break;
						} else {
							queueIndex = queue.length + 1;
						}
					}
				}
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
		
		try {
			Process currentProcess = queue[queueIndex].dequeue();
			
			for (int i = ganttChartCounter; i < currentProcess.getArrivalTime(); i++) {
				System.out.print("0|" + ganttChartCounter + " ");
				GanttChartPanel.addToGanttChart(0, ganttChartCounter, queueIndex);
				ganttChartCounter++;
				
				try {
					Thread.sleep(100);
				} catch(InterruptedException ex) { }
			}
			
			for (int i = 0; i < mlfqProcess.length; i++) {
				if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isResponseDone[i]) {
					TimesPanel.responseTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
					isResponseDone[i] = true;
				}
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
							 queue[(queue.length == 3 ? (queueIndex == 1 ? queueIndex - 1 : queueIndex) : queueIndex)].enqueue(mlfqProcess[j]);
//							 if (queueIndex == queue.length - 1) {
//								 queue[0].enqueue(mlfqProcess[j]);
//							 } else if () {
//								 
//							 }
						 } catch (NullPointerException ex) {
							 queue[(queue.length == 3 ? (queueIndex == 1 ? queueIndex - 1 : queueIndex) : queueIndex)].initialProcess(mlfqProcess[j]);
						 }
					 }
				}
			}
			
			if (currentProcess.getBurstTime() != 0) {
				if (queueIndex == queue.length - 1 && algorithms[queue.length - 1] == 5) {
					try {
						queue[queueIndex].enqueue(currentProcess);
					} catch (NullPointerException ex) {
						queue[queueIndex].initialProcess(currentProcess);
					}
				} else {
					try {
						queue[queueIndex + 1].enqueue(currentProcess);
					} catch (NullPointerException ex) {
						queue[queueIndex + 1].initialProcess(currentProcess);
					}
				}
			}
			
			try {
				if (currentProcess.getBurstTime() == 0) {
					for (int i = 0; i < originalBursts.length; i++) {
						if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
							TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
							TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
							isDone[i] = true;
						}
					}
				}
			} catch (NullPointerException ex) { }
			
			if (queue[queueIndex].getIndex() == 0) {
				if (queueIndex == queue.length - 1 && queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else if (queue.length == 3 && queueIndex == 1 && queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			}
		} catch (NullPointerException ex) { }
		
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
		}
		
		try {
			if (currentProcess.getBurstTime() == 0) {
				for (int i = 0; i < originalBursts.length; i++) {
					if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
						TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
						TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
						isDone[i] = true;
					}
				}
			}
		} catch (NullPointerException ex) { }
		
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
		}
		
		try {
			if (currentProcess.getBurstTime() == 0) {
				for (int i = 0; i < originalBursts.length; i++) {
					if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
						TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
						TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
						isDone[i] = true;
					}
				}
			}
		} catch (NullPointerException ex) { }
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void SRTF(int[] algorithms, int[] quantumTimes) {
		
		Process currentProcess = null;
		
		if (queue[queueIndex].getIndex() == 1) {
			currentProcess = queue[queueIndex].dequeue();
		} else {
			Process[] srtfProcess = new Process[queue[queueIndex].getIndex()];
			int index = queue[queueIndex].getIndex();
			for (int i = 0; i < index; i++) {
				srtfProcess[i] = queue[queueIndex].dequeue();
			}
			
			Process temp = null;
			for (int i = 0; i < srtfProcess.length - 1; i++) {
				 for(int j = 0; j < srtfProcess.length - 1; j++) {
					 if (srtfProcess[i].getBurstTime() > srtfProcess[j + 1].getBurstTime()) {
						temp = srtfProcess[i];
						srtfProcess[i] = srtfProcess[j + 1];
						srtfProcess[j + 1] = temp;
					 }
				}
			}
			
			for (int i = 0; i < srtfProcess.length; i++) {
				try {
					queue[queueIndex].enqueue(srtfProcess[i]);
				} catch (NullPointerException ex) {
					queue[queueIndex].initialProcess(srtfProcess[i]);
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
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime()) {
					if (mlfqProcess[j].getBurstTime() != 0/* && currentProcess.getBurstTime() > mlfqProcess[j].getBurstTime()*/) {
						try {
							queue[0].enqueue(mlfqProcess[j]);
						} catch (NullPointerException ex) {
							queue[0].initialProcess(mlfqProcess[j]);
						}
						
						ifContinue = false;
					} else {
						if (mlfqProcess[j].getBurstTime() == originalBursts[j]) {
							try {
								queue[0].enqueue(mlfqProcess[j]);
							} catch (NullPointerException ex) {
								queue[0].initialProcess(mlfqProcess[j]);
							}
						}
					}
				}
			}
			
			if(!ifContinue) {
				queue[queueIndex].enqueue(currentProcess);
				queueIndex = 0;
				System.out.println();
				break;
			}
		}
		
//		for (int j = 0; j < mlfqProcess.length; j++) {
//			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() == originalBursts[j]) {
//				try {
//					queue[0].enqueue(mlfqProcess[j]);
//				} catch (NullPointerException ex) {
//					queue[0].initialProcess(mlfqProcess[j]);
//				}
//			}
//		}
		
		if (ifContinue) {
			if (queue[queueIndex].getIndex() == 0) {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			} else {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				}
			}
		}
		
		try {
			if (currentProcess.getBurstTime() == 0) {
				for (int i = 0; i < originalBursts.length; i++) {
					if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
						TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
						TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
						isDone[i] = true;
					}
				}
			}
		} catch (NullPointerException ex) { }
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void NPrio(int[] algorithms, int[] quantumTimes) {
		
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
		}
		
		try {
			if (currentProcess.getBurstTime() == 0) {
				for (int i = 0; i < originalBursts.length; i++) {
					if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
						TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
						TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
						isDone[i] = true;
					}
				}
			}
		} catch (NullPointerException ex) { }
		
		checkAlgorithm(algorithms, quantumTimes);
	}
	
	private void Prio(int[] algorithms, int[] quantumTimes) {
		
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
				if (ganttChartCounter == mlfqProcess[j].getArrivalTime()) {
					if (mlfqProcess[j].getBurstTime() != 0/* && currentProcess.getPriority() > mlfqProcess[j].getPriority()*/) {
						try {
							queue[0].enqueue(mlfqProcess[j]);
						} catch (NullPointerException ex) {
							queue[0].initialProcess(mlfqProcess[j]);
						}
						
						ifContinue = false;
					} else {
						if (mlfqProcess[j].getBurstTime() == originalBursts[j]) {
							try {
								queue[0].enqueue(mlfqProcess[j]);
							} catch (NullPointerException ex) {
								queue[0].initialProcess(mlfqProcess[j]);
							}
						}
					}
				}
			}
			
			if(!ifContinue) {
				queue[queueIndex].enqueue(currentProcess);
				queueIndex = 0;
				System.out.println();
				break;
			}
		}
		
//		for (int j = 0; j < mlfqProcess.length; j++) {
//			if (ganttChartCounter >= mlfqProcess[j].getArrivalTime() && mlfqProcess[j].getBurstTime() == originalBursts[j]) {
//				try {
//					queue[0].enqueue(mlfqProcess[j]);
//				} catch (NullPointerException ex) {
//					queue[0].initialProcess(mlfqProcess[j]);
//				}
//			}
//		}
		
		if (ifContinue) {
			if (queue[queueIndex].getIndex() == 0) {
				if (queue[0].getIndex() != 0) {
					queueIndex = 0;
				} else {
					queueIndex++;
				}
				System.out.println();
			}
		}
		
		try {
			if (currentProcess.getBurstTime() == 0) {
				for (int i = 0; i < originalBursts.length; i++) {
					if (currentProcess.getProcessID() == mlfqProcess[i].getProcessID() && !isDone[i]) {
						TimesPanel.turnaroundTime(ganttChartCounter, currentProcess.getArrivalTime(), currentProcess.getProcessID());
						TimesPanel.waitingTime(originalBursts[i], currentProcess.getProcessID());
						isDone[i] = true;
					}
				}
			}
		} catch (NullPointerException ex) { }
		
		checkAlgorithm(algorithms, quantumTimes);
	}
}
