package com.mlfq.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mlfq.menu_bar.MenuBar;
import com.mlfq.panels.ProcessControlBlockPanel;

import net.miginfocom.swing.MigLayout;

public class UserDefinedProcessesDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel;
	private JTable table;
	private JButton addRow, deleteRow, submitButton, cancelButton;
	private JScrollPane scrollPane;
	
	private DefaultTableModel tableModel;
	
	private String[] header = {"PID", "Arrival Time", "CPU Burst Time", "Priority"};
	private Object[][] row = {};
	
	public UserDefinedProcessesDialog()
	{
		setLayout(new MigLayout("fillx, insets 20"));
		addComponents();
		addListeners();
		setSize(new Dimension(500, 600));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponents()
	{
		addRow = new JButton("Add Row");
		deleteRow = new JButton("Delete Row");
		deleteRow.setEnabled(false);
		
		table = new JTable(row, header)
		{
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column)
			{
				return (column == 0 ? false : true);
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
				setBackground(new Color(217, 134, 58));
				
				return this;
			}
			
		});
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(217, 134, 58));
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
		table.setRowHeight(20);
		table.setShowVerticalLines(false);
		
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		panel = new JPanel(new MigLayout("fillx", "[grow, 50%][grow, 50%]", ""));
		panel.add(addRow, "align right, gapbottom 3%");
		panel.add(deleteRow, "align left, gapbottom 3%, wrap");
		panel.add(scrollPane, "spanx 2, grow, align center, wrap");
		panel.add(submitButton, "align right, gaptop 3%");
		panel.add(cancelButton, "align left, gaptop 3%");
		
		add(panel, "grow, align center");
	}
	
	private void addListeners()
	{
		addRow.addActionListener(this);
		deleteRow.addActionListener(this);
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	public void addRow(int pID, String arrivalTime, String cpuBurstTime, String priority)
	{
		tableModel.addRow(new Object[] {pID, arrivalTime, cpuBurstTime, priority});
	}
	
	private void checkTableRowCount()
	{
		if (tableModel.getRowCount() == 0) {
			deleteRow.setEnabled(false);
			submitButton.setEnabled(false);
		} else if (tableModel.getRowCount() == 20) {
			addRow.setEnabled(false);
			submitButton.setEnabled(true);
		} else {
			addRow.setEnabled(true);
			deleteRow.setEnabled(true);
			submitButton.setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{	
		if (event.getSource() == addRow) {
			tableModel.addRow(new Object[] {tableModel.getRowCount() + 1, "", "", ""});
		} else if (event.getSource() == deleteRow) {
			tableModel.removeRow(tableModel.getRowCount() - 1);
		} else if (event.getSource() == submitButton) {
			checkIfTableEmpty();
		} else if (event.getSource() == cancelButton) {
			dispose();
		}
		
		checkTableRowCount();
	}
	
	private void checkIfTableEmpty()
	{
		int counter = 0;
		for(int i = 0; i < tableModel.getRowCount(); i++) {
			for(int j = 0; j < tableModel.getColumnCount(); j++) {
				if (tableModel.getValueAt(i, j).toString().isEmpty()) {
					JOptionPane.showMessageDialog(new JFrame(), "Some field/s is/are blank.", "ERROR", JOptionPane.ERROR_MESSAGE);
					counter++;
					break;
				} 
			}
			if (counter > 0) {
				break;
			}
		}
		
		if (counter == 0) {
			ProcessControlBlockPanel.getUserDefinedTable(table);
			MenuBar.setEnabledGenerateButton(false);
			dispose();
		}
	}
	
	private class CellRenderer extends DefaultTableCellRenderer
	{
        private static final long serialVersionUID = 1L;
        private final DecimalFormat format = new DecimalFormat("#0");
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component component;
            
            if (column != 0) {
            	if (value.toString().isEmpty()) {
            		component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            	} else {
            		String s;
            		try {
            			Double d = Double.parseDouble(String.valueOf(value));
            			if (d <= 0 && column != 1) {
            				table.setValueAt("1", row, column);
            				s = "1";
            			} else if (d >= 51) {
            				if (column != 3) {
            					table.setValueAt("50", row, column);
            					s = "50";
            				} else {
            					table.setValueAt("40", row, column);
            					s = "40";
            				}
            			} else {
            				s = format.format(d);
            			}
            		} catch (NumberFormatException ex) {
            			s = "";
            		}
                	component = super.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
            	}
            } else {
            	component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
            
            component.setFont(new Font("Verdana", Font.PLAIN, 16));
            setHorizontalAlignment(SwingConstants.CENTER);
            component.setBackground(new Color(217, 134, 58, (row % 2 == 0 ? 150 : 50)));
            if (isSelected) {
            	component.setForeground((row % 2 == 0 ? Color.WHITE : new Color(125, 125, 125)));
            } else {
            	component.setForeground(null);
            }
            
            return component;
        }   
    }
}
