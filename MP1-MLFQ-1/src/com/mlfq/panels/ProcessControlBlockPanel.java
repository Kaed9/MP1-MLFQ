package com.mlfq.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mlfq.utilities.Logger;

import net.miginfocom.swing.MigLayout;

public class ProcessControlBlockPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	private JTable table;
	private JScrollPane scrollPane;
	
	private static DefaultTableModel tableModel;
	
	private String[] header = {"PID", "Arrival Time", "CPU Burst Time", "Priority"};
	private Object[][] row = {};
	
	private static String componentName;
	
	public ProcessControlBlockPanel()
	{
		setName("ProcessControlBlockPanel");
		componentName = getName();
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20, wrap 1"));
		setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		addComponents();
	}
	
	private void addComponents()
	{
		titleLabel = new JLabel("PROCESS CONTROL BLOCK", JLabel.CENTER);
		titleLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray),
				BorderFactory.createEmptyBorder(0, 0, 5, 0)
				));
		titleLabel.setFont(new Font("Verdana", Font.BOLD, 28));
		
		table = new JTable(row, header)
		{
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
			
		};
		table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				setFont(new Font("Verdana", Font.BOLD, 14));
				setHorizontalAlignment(SwingConstants.CENTER);
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK),
						BorderFactory.createEmptyBorder(5, 0, 5, 0)
						));
				setBackground(new Color(255, 0, 0));
				
				return this;
			}
			
		});
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(255, 0, 0));
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(header);
		table.setModel(tableModel);
		CellRenderer cellRenderer = new CellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(3).setMaxWidth(150);
		table.setShowGrid(false);
		table.setRowHeight(25);
		table.setShowVerticalLines(false);
		
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		
		add(titleLabel, "grow, align center, gapbottom 3%");
		add(scrollPane, "grow, align center");
	}
	
	public static void addRow(int pID, int arrivalTime, int cpuBurstTime, int priority)
	{
		tableModel.addRow(new Object[] {pID, arrivalTime, cpuBurstTime, priority});
	}
	
	public static void clearComponents()
	{
		tableModel.setRowCount(0);
		Logger.printToConsole(componentName, "INFORMATION", "Data cleared.");
	}
	
	public static void generateRandomizedData(int numberOfProcesses)
	{
		Random random = new Random();
		
		tableModel.setRowCount(0);
		for(int i = 0; i < numberOfProcesses; i++) {
			addRow(i + 1, random.nextInt(50), random.nextInt(50), random.nextInt(40));
		}
		Logger.printToConsole(componentName, "INFORMATION", "Generated " + numberOfProcesses + " random data successfully.");
	}
	
	private class CellRenderer extends DefaultTableCellRenderer
	{
        private static final long serialVersionUID = 1L;
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(new Font("Verdana", Font.PLAIN, 16));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(255, 0, 0, (row % 2 == 0 ? 150 : 50)));

            return this;
        }   
    }
}
