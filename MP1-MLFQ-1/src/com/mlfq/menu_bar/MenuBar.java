package com.mlfq.menu_bar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.mlfq.dialogs.ProcessNumberDialog;
import com.mlfq.panels.AdditionalInformationPanel;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.Logger;

public class MenuBar extends JMenuBar implements ActionListener
{
	private static final long serialVersionUID = 1L;

	private JMenuBar menuBar;
	private JMenu app, process, run, tests, generate;
	private JMenuItem clear, help, exit, implement, use;
	private JMenuItem randomized, userDefined;
	
	public MenuBar()
	{
		setName("MenuBar");
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

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == clear) {
			Logger.printToConsole(getName(), "INFORMATION", "Clearing data from components.");
			ProcessControlBlockPanel.clearComponents();
			TimesPanel.clearComponents();
			AdditionalInformationPanel.clearComponents();
		} else if (event.getSource() == help) {
			
		} else if (event.getSource() == randomized) {
			Logger.printToConsole(getName(), "INFORMATION", "Getting number of processes to be generated.");
			new ProcessNumberDialog("randomized");
		} else if (event.getSource() == userDefined) {
			Logger.printToConsole(getName(), "INFORMATION", "Getting number of processes to be generated.");
			new ProcessNumberDialog("user-defined");
		} else if (event.getSource() == implement) {
			
		} else if (event.getSource() == use) {
			
		} else if (event.getSource() == exit) {
			Logger.printToConsole(getName(), "INFORMATION", "Application exiting.");
			System.exit(0);
		}
	}
}
