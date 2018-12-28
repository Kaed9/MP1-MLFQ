package com.mlfq.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mlfq.utilities.MLFQHandler;

import net.miginfocom.swing.MigLayout;

public class GanttChartPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	private static JPanel panel;
	private static JScrollPane scrollPane;

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
		titleLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray),
				BorderFactory.createEmptyBorder(0, 0, 5, 0)
				));
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
		
		panel = new JPanel(new MigLayout("insets 0", "[]0[]"));
		scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		add(titleLabel, "grow, align center, gapbottom 3%");
		add(scrollPane, "grow, align center, height 100%");
	}
	
	public static void addToGanttChart(int pID, int counter)
	{
		JLabel label = new JLabel((pID == 0 ? " " : "P" + pID), JLabel.CENTER);
		label.setPreferredSize(new Dimension(60, 70));
		label.setOpaque(true);
		label.setBackground(MLFQHandler.getColorByProcessId(pID));
		label.setFont(new Font("Verdana", Font.PLAIN, 20));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		JLabel counterLabel = new JLabel("" + (counter + 1), JLabel.RIGHT);
		counterLabel.setFont(new Font("Verdana", Font.PLAIN, 16));
		JPanel labelPanel = new JPanel(new MigLayout("insets 0, wrap 1"));
		labelPanel.add(label);
		labelPanel.add(counterLabel, "align right");
		panel.add(labelPanel);
		scrollPane.getHorizontalScrollBar().setValue(scrollPane.getHorizontalScrollBar().getMaximum());
		scrollPane.repaint();
		scrollPane.revalidate();
	}
	
	public static void clearComponents()
	{
		panel.removeAll();
		panel.repaint();
		panel.revalidate();
	}
}
