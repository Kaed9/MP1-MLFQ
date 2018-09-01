package com.mlfq.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.mlfq.data_structures.Process;
import com.mlfq.panels.TimesPanel;
import com.mlfq.scheduling_algorithms.FirstComeFirstServe;

public class MLFQHandler
{	
	private DefaultTableModel tableModel;
	
	private ArrayList<JComboBox<String>> selectedAlgo;
	private ArrayList<JTextField> quantumTime;
	
	private Process[] process;
	
	private Random random;
	
	private static Color[] color;
	
	public MLFQHandler(DefaultTableModel tableModel, ArrayList<JComboBox<String>> selectedAlgo, ArrayList<JTextField> quantumTime)
	{
		this.tableModel = tableModel;
		this.selectedAlgo = selectedAlgo;
		this.quantumTime = quantumTime;
		makeNewProcesses();
		colorRandomizer();
		implementAlgorithm();
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
		
		for(int i = 0; i < selectedAlgo.size(); i++) {
			switch(selectedAlgo.get(i).getSelectedIndex()) {
				case 0:
					new FirstComeFirstServe(process);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				default:
					break;
			}
		}
	}
}
