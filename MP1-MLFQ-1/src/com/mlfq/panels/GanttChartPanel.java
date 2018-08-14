package com.mlfq.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class GanttChartPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	

	public GanttChartPanel()
	{
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20, wrap 1"));
		setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
		addComponents();
	}
	
	private void addComponents()
	{
		titleLabel = new JLabel("GANTT CHART", JLabel.CENTER);
		titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
		
		add(titleLabel, "grow, align center");
	}
}
