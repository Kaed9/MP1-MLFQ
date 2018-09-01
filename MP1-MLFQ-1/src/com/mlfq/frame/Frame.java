package com.mlfq.frame;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mlfq.menu_bar.MenuBar;
import com.mlfq.panels.AdditionalInformationPanel;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.panels.TimesPanel;

import net.miginfocom.swing.MigLayout;

public class Frame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JPanel topPanel, bottomPanel;
	
	private ProcessControlBlockPanel processControlBlockPanel;
	private GanttChartPanel ganttChartPanel;
	private TimesPanel timesPanel;
	private AdditionalInformationPanel additionalInformationPanel;
	
	private MenuBar menuBar;

	public Frame()
	{
		super("Multiple Feedback Queue Scheduling");
		setLayout(new MigLayout("fillx, insets 20, wrap 1", "[grow]", "[grow, 55%]20[grow, 45%]"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		getContentPane().setBackground(Color.GRAY);
		initComponents();
	}
	
	private void initComponents()
	{
		menuBar = new MenuBar();
		setJMenuBar(menuBar.getMenuBar());
		
		topPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow, 35%]20[grow, 65%]", "[fill]"));
		topPanel.setBackground(Color.GRAY);
		
		processControlBlockPanel = new ProcessControlBlockPanel();
		ganttChartPanel = new GanttChartPanel();
		topPanel.add(processControlBlockPanel, "width 100%, height 100%");
		topPanel.add(ganttChartPanel, "width 100%, height 100%");
		
		bottomPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow, 50%]20[grow, 50%]", "[fill]"));
		bottomPanel.setBackground(Color.GRAY);
		
		timesPanel = new TimesPanel();
		additionalInformationPanel = new AdditionalInformationPanel();
		bottomPanel.add(timesPanel, "width 100%, height 100%");
		bottomPanel.add(additionalInformationPanel, "width 100%, height 100%");
		
		add(topPanel, "width 100%, height 100%");
		add(bottomPanel, "width 100%, height 100%");
	}
}
