package com.mlfq.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

public class TimesPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	private JTable table, averageTable;
	private JScrollPane scrollPane, averageScrollPane;
	
	private static DefaultTableModel tableModel, averageTableModel;

//	private String[] header = {"PID", "Response Time", "Turnaround Time", "Waiting Time"}, averageHeader = {"", "", "", ""};
 	private String[] header = {"PID", "<html>Response<br/>&nbsp&nbsp Time</html>", "<html>Turnaround<br/>&nbsp&nbsp&nbsp&nbsp Time</html>", "<html>Waiting<br/>&nbsp Time</html>"}, averageHeader = {"", "", "", ""};
	private Object[][] row = {}, averageRow = {};
	
	private static int[] responseTime, turnaroundTime, waitingTime;
	
	public TimesPanel()
	{
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20, wrap 1", "", "[][]0[]"));
		setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
		addComponents();
	}
	
	private void addComponents()
	{
		titleLabel = new JLabel("TIMES", JLabel.CENTER);
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
				
				setFont(new Font("Verdana", Font.BOLD, (column == 2 ? 12 : 14)));
				setHorizontalAlignment(SwingConstants.CENTER);
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK),
						BorderFactory.createEmptyBorder(5, 0, 5, 0)
						));
				setBackground(new Color(0, 0, 255));
				setForeground(new Color(255, 255, 0));
				
				return this;
			}
			
		});
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(0, 0, 255));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
		table.setShowGrid(false);
		table.setRowHeight(25);
		table.setShowVerticalLines(false);
		
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));

        averageTable = new JTable(averageRow, averageHeader)
		{
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
			
		};
		averageTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer()
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
				setBackground(new Color(0, 0, 255));
				
				return this;
			}
		});
		averageTable.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		averageTable.getTableHeader().setBackground(new Color(0, 0, 255));
		averageTableModel = new DefaultTableModel();
		averageTableModel.setColumnIdentifiers(averageHeader);
		averageTable.setModel(averageTableModel);
		averageTable.setTableHeader(null);
		CellRenderer1 cellRenderer1 = new CellRenderer1();
		averageTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer1);
		averageTable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer1);
		averageTable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer1);
		averageTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer1);
		averageTable.getColumnModel().getColumn(0).setMaxWidth(50);
		averageTable.setShowGrid(false);
		averageTable.setRowHeight(25);
		averageTable.setShowVerticalLines(false);
        
        averageScrollPane = new JScrollPane(averageTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        averageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        averageScrollPane.setBackground(Color.WHITE);
        averageScrollPane.getViewport().setBackground(Color.WHITE);
        averageScrollPane.getVerticalScrollBar().setEnabled(false);
        
		add(titleLabel, "grow, align center, gapbottom 3%");
		add(scrollPane, "grow, align center");
		add(averageScrollPane, "grow, align center, height 41%");
		
		addAverageRow("AVE", "", "", "");
	}
	
	public void addRow(String pID, String responseTime, String turnaroundTime, String waitingTime)
	{
		tableModel.addRow(new Object[] {pID, responseTime, turnaroundTime, waitingTime});
	}
	
	public static void addAverageRow(String pID, String responseTime, String turnaroundTime, String waitingTime)
	{
		averageTableModel.addRow(new Object[] {pID, responseTime, turnaroundTime, waitingTime});
	}
	
	public static void clearComponents()
	{
		tableModel.setRowCount(0);
		averageTableModel.setRowCount(0);
		addAverageRow("AVE", "", "", "");
	}
	
	public static void initializeTimes(int processSize)
	{
		responseTime = new int[processSize];
		turnaroundTime = new int[processSize];
		waitingTime = new int[processSize];
		
		for(int i = 0; i < processSize; i++) {
			tableModel.addRow(new Object[] {"" + (i + 1), "", "", ""});
		}
	}
	
	public static void responseTime(int firstExecution, int arrivalTime, int pID)
	{
		// response time = first execution - arrival time
		responseTime[pID - 1] += (firstExecution - arrivalTime);
		tableModel.setValueAt(responseTime[pID - 1], pID - 1, 1);
		averageTimes(responseTime, 1);
	}
	
	public static void turnaroundTime(int completionTime, int arrivalTime, int pID)
	{
		// turnaround time = completion - arrival time
		turnaroundTime[pID - 1] += (completionTime - arrivalTime);
		tableModel.setValueAt(turnaroundTime[pID - 1], pID - 1, 2);
		averageTimes(turnaroundTime, 2);
	}
	
	public static void waitingTime(int burstTime, int pID)
	{
		// waiting time = turnaround time - burst time
		waitingTime[pID - 1] += (turnaroundTime[pID - 1] - burstTime);
		tableModel.setValueAt(waitingTime[pID - 1], pID - 1, 3);
		averageTimes(waitingTime, 3);
	}
	
	private static void averageTimes(int[] time, int column)
	{
		float average = 0;
		for(int i = 0; i < time.length; i++) {
			average += time[i];
		}
		
		average /= time.length;
		
		averageTableModel.setValueAt(average, 0, column);
	}
	
	private class CellRenderer extends DefaultTableCellRenderer
	{
        private static final long serialVersionUID = 1L;
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        { 
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(new Font("Verdana", Font.PLAIN, 16));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(0, 0, 255, (row % 2 == 0 ? 150 : 50)));

            return this;
        }   
    }
	
	private class CellRenderer1 extends DefaultTableCellRenderer
	{
        private static final long serialVersionUID = 1L;
        
        private final DecimalFormat format = new DecimalFormat("#0.00");
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (column != 0) {
            	String s;
            	try {
            		Double d = Double.parseDouble(String.valueOf(value));
            		s = format.format(d);
                	component = super.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
            	} catch (NumberFormatException ex) { }
            }
            
            component.setFont(new Font("Verdana", (column == 0 ? Font.BOLD : Font.PLAIN), 15));
            setHorizontalAlignment(SwingConstants.CENTER);
            component.setBackground(new Color(0, 128, 129, (column != 0 ? 150 : 255)));
            setBorder(BorderFactory.createMatteBorder(0, (column == 0 ? 1 : 0), 1, (column == 3 ? 1 : 0), Color.BLACK));
            setForeground(column == 0 ? new Color(255, 255, 0) : Color.BLACK);
            
            return component;
        }   
    }
}
