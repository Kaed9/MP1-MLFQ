package com.mlfq.scheduling_algorithms;

import com.mlfq.data_structures.Queue;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.data_structures.Process;
import com.mlfq.utilities.SchedulingAlgorithmsUtilities;

public class FirstComeFirstServe
{	
	public FirstComeFirstServe(Process[] process)
	{
		Process[] fcfsProcess = SchedulingAlgorithmsUtilities.quicksort(process, 0, process.length-1, 0);
		
		new Thread()
		{
			public void run()
			{
				Queue queue = new Queue();
				int counter = 0;
				
				if (counter < fcfsProcess[0].getArrivalTime()) {
					for(int k = 0; k < fcfsProcess[0].getArrivalTime(); k++) {
						GanttChartPanel.addToGanttChart(0, counter, 1);
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
				}
				
				for(int i = 0; i < fcfsProcess.length; i++) {
					if (counter < fcfsProcess[i].getArrivalTime()) {
						for(int k = counter; k < fcfsProcess[i].getArrivalTime(); k++) {
							GanttChartPanel.addToGanttChart(0, counter, 1);
							counter++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException ex) { }
						}
					}
					
					for(int j = 0; j < fcfsProcess[i].getBurstTime(); j++) {
						if (j == 0) {
							TimesPanel.responseTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
						}
						
						if (i == 0 && j == 0) {
							queue.initialProcess(fcfsProcess[i]);
							System.out.print(fcfsProcess[i].getProcessID() + " ");
							GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter, 1);
						} else {
							queue.enqueue(fcfsProcess[i]);
							System.out.print(fcfsProcess[i].getProcessID() + " ");
							GanttChartPanel.addToGanttChart(fcfsProcess[i].getProcessID(), counter, 1);
						}
						counter++;
						try {
							Thread.sleep(100);
						} catch (InterruptedException ex) { }
					}
					
					TimesPanel.turnaroundTime(counter, fcfsProcess[i].getArrivalTime(), fcfsProcess[i].getProcessID());
					TimesPanel.waitingTime(fcfsProcess[i].getBurstTime(), fcfsProcess[i].getProcessID());
				}
			}
		}.start();
		System.out.println();
	}
}
