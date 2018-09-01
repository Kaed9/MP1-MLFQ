package com.mlfq.menu_bar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.mlfq.dialogs.ImplementAlgorithmDialog;
import com.mlfq.dialogs.ProcessNumberDialog;
import com.mlfq.dialogs.UserDefinedProcessesDialog;
import com.mlfq.panels.AdditionalInformationPanel;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.panels.TimesPanel;

public class MenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu app, process, run, tests, generate;
	private JMenuItem clear, help, exit, use;
	private static JMenuItem implement;
	private JMenuItem randomized, userDefined;
	
	public MenuBar()
	{
		menuBar = new JMenuBar();
		menuComponents();
		addListenerToComponents();
	}
	
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}
	
	private void menuComponents()
	{
		app = new JMenu("App");
		app.setMnemonic(KeyEvent.VK_A);
		addAppItems();
		
		process = new JMenu("Process");
		app.setMnemonic(KeyEvent.VK_P);
		addProcessItems();
		
		run = new JMenu("Run");
		run.setMnemonic(KeyEvent.VK_N);
		addRunItems();
		
		tests = new JMenu("Test Cases");
		tests.setMnemonic(KeyEvent.VK_T);
		addTestsItems();
		
		menuBar.add(app);
		menuBar.add(process);
		menuBar.add(run);
		menuBar.add(tests);
	}
	
	private void addAppItems()
	{
		clear = new JMenuItem("Clear All");
		clear.setMnemonic(KeyEvent.VK_C);
		help = new JMenuItem("Help");
		help.setMnemonic(KeyEvent.VK_H);
		exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		
		app.add(clear);
		app.addSeparator();
		app.add(help);
		app.addSeparator();
		app.add(exit);
	}
	
	private void addProcessItems()
	{
		generate = new JMenu("Generate Processes");
		generate.setMnemonic(KeyEvent.VK_G);
		
		randomized = new JMenuItem("Randomized");
		randomized.setMnemonic(KeyEvent.VK_R);
		userDefined = new JMenuItem("User-defined");
		userDefined.setMnemonic(KeyEvent.VK_U);
		
		generate.add(randomized);
		generate.add(userDefined);
		
		process.add(generate);
	}
	
	private void addRunItems()
	{
		implement = new JMenuItem("Implement MLFQ");
		implement.setMnemonic(KeyEvent.VK_I);
		implement.setEnabled(false);
		
		run.add(implement);
	}
	
	private void addTestsItems()
	{
		use = new JMenuItem("Use Test Cases");
		use.setMnemonic(KeyEvent.VK_S);
		
		tests.add(use);
	}
	
	private void addListenerToComponents()
	{
		clear.addActionListener(this);
		help.addActionListener(this);
		exit.addActionListener(this);
		randomized.addActionListener(this);
		userDefined.addActionListener(this);
		implement.addActionListener(this);
		use.addActionListener(this);
	}
	
	public static void setEnabledImplementButton(boolean bool)
	{
		implement.setEnabled(bool);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == clear) {
			ProcessControlBlockPanel.clearComponents();
			TimesPanel.clearComponents();
			AdditionalInformationPanel.clearComponents();
			GanttChartPanel.clearComponents();
			setEnabledImplementButton(false);
		} else if (event.getSource() == help) {
			
		} else if (event.getSource() == randomized) {
			new ProcessNumberDialog();
		} else if (event.getSource() == userDefined) {
			new UserDefinedProcessesDialog();
		} else if (event.getSource() == implement) {
			new ImplementAlgorithmDialog();
		} else if (event.getSource() == use) {
			
		} else if (event.getSource() == exit) {
			System.exit(0);
		}
	}
}
