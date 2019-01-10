package com.mlfq.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.mlfq.data_structures.Process;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.scheduling_algorithms.FirstComeFirstServe;
import com.mlfq.scheduling_algorithms.FixedTimeSlot;
import com.mlfq.scheduling_algorithms.MLFQ;
import com.mlfq.scheduling_algorithms.NonPreemptivePriorityScheduling;
import com.mlfq.scheduling_algorithms.PreemptivePriorityScheduling;
import com.mlfq.scheduling_algorithms.RoundRobin;
import com.mlfq.scheduling_algorithms.ShortestJobFirst;
import com.mlfq.scheduling_algorithms.ShortestRemainingTimeFirst;

public class MLFQHandler
{	
	private DefaultTableModel tableModel;
	
	private ArrayList<JComboBox<String>> selectedAlgo;
	private ArrayList<JTextField> quantumTime;
	
	private int selectedAlgoTest, quantumTimeTest;
	
	private Process[] process;
	
	private Random random;
	
	private static Color[] color;
	
	public MLFQHandler(DefaultTableModel tableModel, ArrayList<JComboBox<String>> selectedAlgo, ArrayList<JTextField> quantumTime, int priorityPolicy)
	{
		this.tableModel = tableModel;
		this.selectedAlgo = selectedAlgo;
		this.quantumTime = quantumTime;
		makeNewProcesses();
		colorRandomizer();
		GanttChartPanel.addToQueuePanel(selectedAlgo.size());
		
		if (selectedAlgo.size() == 1) {
			implementAlgorithm();
		} else {
			if (priorityPolicy == 0) {
				if (selectedAlgo.get(0).getSelectedIndex() == 5) {
					new MLFQ(process, true, selectedAlgo, quantumTime);
				} else {
					implementAlgorithm();
				}
			} else {
//				new FixedTimeSlot();
			}
		}
	}
	
	public MLFQHandler(DefaultTableModel tableModel, int selectedAlgoTest, int quantumTimeTest)
	{
		this.tableModel = tableModel;
		this.selectedAlgoTest = selectedAlgoTest;
		this.quantumTimeTest = quantumTimeTest;
		makeNewProcesses();
		colorRandomizer();
		implementAlgorithmForTestCase();
	}
	
	private void makeNewProcesses()
	{
		process = new Process[tableModel.getRowCount()];
		
		for(int i = 0; i < tableModel.getRowCount(); i++) {
			process[i] = new Process(
					Integer.parseInt(tableModel.getValueAt(i, 0).toString()),
					Integer.parseInt(tableModel.getValueAt(i, 1).toString()),
					Integer.parseInt(tableModel.getValueAt(i, 2).toString()),
					Integer.parseInt(tableModel.getValueAt(i, 3).toString()));
		}
	}
	
	private void colorRandomizer()
	{
		color = new Color[tableModel.getRowCount() + 1];
		random = new Random();
		color[0] = new Color(255, 255, 255);
		for(int i = 1; i < tableModel.getRowCount(); i++) {
			color[i] = new Color(random.nextInt(155) + 100, random.nextInt(155) + 100, random.nextInt(155) + 100);
		}
	}
	
	public static Color getColorByProcessId(int pID)
	{
		return color[pID];
	}
	
	private void implementAlgorithm()
	{
		TimesPanel.initializeTimes(tableModel.getRowCount());
		
//		for(int i = 0; i < selectedAlgo.size(); i++) {
		switch(selectedAlgo.get(0).getSelectedIndex()) {
			case 0:
				new FirstComeFirstServe(process);
				break;
			case 1:
				new ShortestJobFirst(process);
				break;
			case 2:
				new ShortestRemainingTimeFirst(process);
				break;
			case 3:
				new PreemptivePriorityScheduling(process);
				break;
			case 4:
				new NonPreemptivePriorityScheduling(process);
				break;
			case 5:
				new RoundRobin(process, Integer.parseInt(quantumTime.get(0).getText()));
				break;
			default:
				break;
		}
//		}
	}
	
	private void implementAlgorithmForTestCase() {
		
		TimesPanel.initializeTimes(tableModel.getRowCount());
		
		switch(selectedAlgoTest) {
			case 0:
				new FirstComeFirstServe(process);
				break;
			case 1:
				new ShortestJobFirst(process);
				break;
			case 2:
				new ShortestRemainingTimeFirst(process);
				break;
			case 3:
				new PreemptivePriorityScheduling(process);
				break;
			case 4:
				new NonPreemptivePriorityScheduling(process);
				break;
			case 5:
				new RoundRobin(process, quantumTimeTest);
				break;
			default:
				break;
		}
	}
}