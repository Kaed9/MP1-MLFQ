package com.mlfq.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.mlfq.utilities.Logger;

import net.miginfocom.swing.MigLayout;

public class AdditionalInformationPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel titleLabel;
	private JTable table;
	private static JTextField outputField, policyField;
	private JLabel outputLabel, policyLabel;
	private JScrollPane scrollPane, outputScrollPane;
	
	private static DefaultTableModel tableModel;
	
	private String[] header = {"Queue #", "Classical Scheduling Algorithm"};
	private Object[][] row = {};
	
	private static String componentName;
	
	public AdditionalInformationPanel()
	{
		setName("AdditionalInformationPanel");
		componentName = getName();
		setBackground(Color.WHITE);
		setLayout(new MigLayout("fillx, insets 20, wrap 1"));
		setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
		addComponents();
	}
	
	private void addComponents()
	{
		titleLabel = new JLabel("ADDITIONAL INFORMATION", JLabel.CENTER);
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
				setBackground(new Color(0, 255, 0));
				
				return this;
			}
			
		});
		table.getTableHeader().setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));
		table.getTableHeader().setBackground(new Color(0, 255, 0));
		tableModel = new DefaultTableModel();
		tableModel.setColumnIdentifiers(header);
		table.setModel(tableModel);
		CellRenderer cellRenderer = new CellRenderer();
		table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		table.getColumnModel().getColumn(0).setMaxWidth(300);
		table.setShowGrid(false);
		table.setRowHeight(25);
		table.setShowVerticalLines(false);
		
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		
		outputLabel = new JLabel("Output", JLabel.CENTER);
		outputLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		outputField = new JTextField();
		outputField.setFont(new Font("Verdana", Font.PLAIN, 12));
		outputField.setHorizontalAlignment(JTextField.CENTER);
		outputField.setText(" P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1 P1");
		outputField.setEditable(false);
		outputField.setBackground(Color.WHITE);
		outputField.setBorder(BorderFactory.createEmptyBorder());
		
		outputScrollPane = new JScrollPane(outputField, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		outputScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		outputScrollPane.setBorder(BorderFactory.createEmptyBorder());
		outputScrollPane.setBackground(Color.WHITE);
		outputScrollPane.getViewport().setBackground(Color.WHITE);
		
		policyLabel = new JLabel("Priority Policy", JLabel.CENTER);
		policyLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		policyField = new JTextField();
		policyField.setFont(new Font("Verdana", Font.PLAIN, 12));
		policyField.setHorizontalAlignment(JTextField.CENTER);
		policyField.setText("higher before lower");
		policyField.setEditable(false);
		
		add(titleLabel, "grow, align center, gapbottom 3%");
		add(scrollPane, "grow, align center, height 27%");
		add(outputLabel, "grow, align center");
		add(outputScrollPane, "grow, align center");
		add(policyLabel, "grow, align center");
		add(policyField, "grow, align center");
		
		addRow("1", "First Come First Serve");
		addRow("2", "Shortest Job First");
		addRow("3", "Shortest Remaining Time First");
		addRow("4", "Preemptive Priority Scheduling");
		addRow("5", "Non-preemptive Priority Scheduling");
		addRow("6", "Round Robin");
	}
	
	public void addRow(String queueNumber, String schedulingAlgorithm)
	{
		tableModel.addRow(new Object[] {queueNumber, schedulingAlgorithm});
	}
	
	public static void clearComponents()
	{
		tableModel.setRowCount(0);
		outputField.setText(" ");
		policyField.setText(" ");
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
            setBackground(new Color(0, 255, 0, (row % 2 == 0 ? 150 : 50)));

            return this;
        }   
    }
}
