package com.mlfq.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

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

public class TimesPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JLabel titleLabel;
	private JTable table, averageTable;
	private JScrollPane scrollPane, averageScrollPane;
	
	private static DefaultTableModel tableModel, averageTableModel;

	private String[] header = {"PID", "Response Time", "Turnaround Time", "Waiting Time"}, averageHeader = {"", "", "", ""};
	private Object[][] row = {}, averageRow = {};
	
	private static String componentName;
	
	public TimesPanel()
	{
		setName("TimesPanel");
		componentName = getName();
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
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(0, 0, 255));
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(header);
		table.setModel(tableModel);
		CellRenderer cellRenderer = new CellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
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
		add(averageScrollPane, "grow, align center, height 100%");
		
		addRow("1", "6.00", "54.00", "6.00");
		addRow("2", "6.00", "54.00", "6.00");
		addRow("3", "6.00", "54.00", "6.00");
		addRow("4", "6.00", "54.00", "6.00");
		
		addAverageRow("AVERAGE", "10.00", "20.00", "30.00");
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
		addAverageRow("AVERAGE", "", "", "");
		Logger.printToConsole(componentName, "INFORMATION", "Data cleared.");
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
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            setFont(new Font("Verdana", (column == 0 ? Font.BOLD : Font.PLAIN), 16));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(0, 128, 129, (column != 0 ? 150 : 255)));
            setBorder(BorderFactory.createMatteBorder(0, (column == 0 ? 1 : 0), 1, (column == 3 ? 1 : 0), Color.BLACK));

            return this;
        }   
    }
}
