package com.mlfq.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mlfq.menu_bar.MenuBar;

import net.miginfocom.swing.MigLayout;

public class ProcessControlBlockPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	private JTable table;
	private JScrollPane scrollPane;
	
	private static DefaultTableModel tableModel;
	
//	private String[] header = {"PID", "Arrival Time", "CPU Burst Time", "Priority"};
	private String[] header = {"PID", "<html>Arrival<br/>Time</html>", "<html>CPU Burst<br/>&nbsp&nbsp&nbsp Time</html>", "Priority"};
	private Object[][] row = {};
	
	public ProcessControlBlockPanel()
	{
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20, wrap 1"));
		setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		addComponents();
	}
	
	private void addComponents()
	{
		titleLabel = new JLabel("PCB", JLabel.CENTER);
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
				
				setFont(new Font("Verdana", Font.BOLD, (column == 2 ? 13 : (column == 3 ? 15 : 14))));
				setHorizontalAlignment(SwingConstants.CENTER);
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK),
						BorderFactory.createEmptyBorder(5, 0, 5, 0)
						));
				setBackground(new Color(255, 0, 0));
				setForeground(new Color(255, 255, 0));
				
				return this;
			}
			
		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(255, 0, 0));
		table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(header); 
		table.setModel(tableModel);
		CellRenderer cellRenderer = new CellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
//		table.getColumnModel().getColumn(3).setMaxWidth(150);
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
	}
	
	public static void generateRandomizedData(int numberOfProcesses)
	{
		Random random = new Random();
		
		tableModel.setRowCount(0);
		for(int i = 0; i < numberOfProcesses; i++) {
			addRow(i + 1, random.nextInt(51), random.nextInt(50) + 1, random.nextInt(40) + 1);
		}
		MenuBar.setEnabledImplementButton(true);
	}
	
	public static void getUserDefinedTable(JTable table)
	{
		for(int i = 0; i < table.getRowCount(); i++) {
			addRow(Integer.parseInt(table.getValueAt(i, 0).toString()), Integer.parseInt(table.getValueAt(i, 1).toString()), Integer.parseInt(table.getValueAt(i, 2).toString()), Integer.parseInt(table.getValueAt(i, 3).toString()));
		}
		MenuBar.setEnabledImplementButton(true);
	}
	
	public static void generatePCBFromTestCase(int testCaseNumber) throws IOException {
		
		File file = new File("files/tests.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String string;
		int counter = 0, indexCounter = 0;
		int index = (testCaseNumber == 0 || testCaseNumber == 1 ? 5 : (testCaseNumber == 2 || testCaseNumber == 3 ? 10 : 15));
		int[][] forPCB = new int[3][index];
		while((string = reader.readLine()) != null) {
			if (string.contains("testcase" + (testCaseNumber + 1))) {
				for(String part : string.split("=")) {
					if (!part.equalsIgnoreCase("testcase" + (testCaseNumber + 1))) {
						for(String numbers : part.split(",")) {
							forPCB[counter][indexCounter] = Integer.parseInt(numbers);
							indexCounter++;
						}
						counter++;
						indexCounter = 0;
					}
				}
			}
		}
		
		for (int i = 0; i < index; i++) {
			addRow((i + 1), forPCB[0][i], forPCB[1][i], forPCB[2][i]);
		}
		
		reader.close();
	}
	
	public static DefaultTableModel getTableModel()
	{
		return tableModel;
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
