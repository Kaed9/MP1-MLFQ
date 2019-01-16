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
	
	private static int queueCount;
	
	private JLabel titleLabel;
	private static JPanel panel, queuePanel;
	private static JScrollPane scrollPane;

	public GanttChartPanel()
	{
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20", "[grow, 5%]0[grow, 95%]", "[grow]"));
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
		
		queuePanel = new JPanel(new MigLayout("fillx, insets 0, wrap 1", "[grow]", "[]0[]0[]"));
		queuePanel.setBackground(Color.WHITE);
		
		add(titleLabel, "grow, spanx 2, align center, gapbottom 3%, wrap");
		add(queuePanel, "width 100%, height 100%");
		add(scrollPane, "width 100%, height 100%");
	}
	
	public static void addToQueuePanel(int queueCount1) {
		
		queueCount = queueCount1;
		
		for (int i = 0; i < queueCount; i++) {
			JLabel label = new JLabel("Q" + (i + 1));
			label.setPreferredSize(new Dimension(15, 32));
//			label.setBackground(Color.WHITE);
			label.setFont(new Font("Verdana", Font.BOLD, 15));
			
//			JPanel panel1 = new JPanel(new MigLayout("insets 0"));
//			panel.setPreferredSize(new Dimension(60, 25));
//			panel1.setBackground(Color.WHITE);
//			panel1.add(label);
			queuePanel.add(label);
			queuePanel.repaint();
			queuePanel.revalidate();
		}
	}
	
	public static void addToGanttChart(int pID, int counter, int queueNumber)
	{
		JLabel label = null, counterLabel = null;
		JPanel labelPanel = new JPanel(new MigLayout("insets 0, wrap 1", "[]0[]", "[]0[]"));
		queueNumber++;
		
		for (int i = 0; i < queueCount; i++) {
			if (i == (queueNumber - 1)) {
				label = new JLabel((pID == 0 ? " " : "P" + pID), JLabel.CENTER);
				label.setPreferredSize(new Dimension(60, 32));
				label.setOpaque(true);
				label.setBackground(MLFQHandler.getColorByProcessId(pID));
				label.setFont(new Font("Verdana", Font.BOLD, 15));
				label.setBorder((pID == 0 ? BorderFactory.createEmptyBorder(1, 1, 1, 1) : BorderFactory.createLineBorder(Color.BLACK, 1)));
				labelPanel.add(label);
			} else {
				label = new JLabel(" ", JLabel.CENTER);
				label.setPreferredSize(new Dimension(60, 32));
				label.setOpaque(true);
				label.setBackground(new Color(255, 255, 255));
				label.setFont(new Font("Verdana", Font.BOLD, 15));
//				label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				labelPanel.add(label);
			}
			
			if (i == queueCount - 1) {
				counterLabel = new JLabel("" + (counter + 1), JLabel.RIGHT);
				counterLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
				labelPanel.add(counterLabel, "align right");
			}
		}
		
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
		queuePanel.removeAll();
		queuePanel.repaint();
		queuePanel.revalidate();
	}
}
