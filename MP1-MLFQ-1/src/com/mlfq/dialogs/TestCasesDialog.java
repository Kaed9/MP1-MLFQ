package com.mlfq.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mlfq.panels.AdditionalInformationPanel;
import com.mlfq.panels.GanttChartPanel;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.panels.TimesPanel;
import com.mlfq.utilities.MLFQHandler;

import net.miginfocom.swing.MigLayout;

public class TestCasesDialog extends JDialog implements ActionListener, ItemListener {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel testCasesLabel, schedulingAlgorithmsLabel, quantumTimeLabel;
	private JComboBox<String> testCases, schedulingAlgorithms;
	private JButton submitButton, cancelButton;
	private JPanel panel, algorithmHolder;
	private JTextField quantumTimeField;
	
	private String[] testCasesArray = {"Test Case #1", "Test Case #2", "Test Case #3", "Test Case #4", "Test Case #5", "Test Case #6"};
	private String[] schedulingAlgorithmsArray = {"First Come First Serve", "Shortest Job First", "Shortest Remaining Time First", "Preemptive Priority Scheduling", "Non-preemptive Priority Scheduling", "Round Robin"};

	public TestCasesDialog() {

		setLayout(new MigLayout("fillx, insets 20"));
		addComponents();
		addListeners();
		setSize(new Dimension(400, 230));
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponents() {
		
		testCasesLabel = new JLabel("Choose test case to use:", JLabel.CENTER);
		testCases = new JComboBox<String>(testCasesArray);
		
		schedulingAlgorithmsLabel = new JLabel("Choose a scheduling algorithm:", JLabel.CENTER);
		schedulingAlgorithms = new JComboBox<String>(schedulingAlgorithmsArray);
		
		quantumTimeLabel = new JLabel("Quantum Time", JLabel.CENTER);
		quantumTimeField = new JTextField();
		quantumTimeField.setText("0");
		quantumTimeField.setEnabled(false);
		
		algorithmHolder = new JPanel(new MigLayout("fillx", "[grow, 70%][grow, 20%][grow, 10%]"));
		algorithmHolder.add(schedulingAlgorithms, "grow, align center");
		algorithmHolder.add(quantumTimeLabel, "grow, align center");
		algorithmHolder.add(quantumTimeField, "grow, align center");
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		
		panel = new JPanel(new MigLayout("fillx", "[grow, 50%][grow, 50%]"));
		panel.add(testCasesLabel, "spanx 2, grow, align center, wrap");
		panel.add(testCases, "spanx 2, align center, wrap, gapbottom 8%");
		panel.add(schedulingAlgorithmsLabel, "spanx 2, grow, align center, wrap");
		panel.add(algorithmHolder, "spanx 2, align center, wrap, gapbottom 15%");
		panel.add(submitButton, "align right");
		panel.add(cancelButton, "align left");
		
		add(panel, "grow, align center");
	}
	
	private void addListeners() {
		
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
		schedulingAlgorithms.addItemListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == submitButton) {
			ProcessControlBlockPanel.clearComponents();
			TimesPanel.clearComponents();
			AdditionalInformationPanel.clearComponents();
			GanttChartPanel.clearComponents();
			
			try {
				ProcessControlBlockPanel.generatePCBFromTestCase(testCases.getSelectedIndex());
			} catch (IOException e) { }
			AdditionalInformationPanel.setDisplayAlgoAndPolicy(new String[] {schedulingAlgorithms.getSelectedItem().toString()}, "Higher before lower", new int[] {Integer.parseInt(quantumTimeField.getText())});
			new MLFQHandler(ProcessControlBlockPanel.getTableModel(), schedulingAlgorithms.getSelectedIndex(), Integer.parseInt(quantumTimeField.getText()));
			dispose();
		} else if (event.getSource() == cancelButton) {
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		
		if (schedulingAlgorithms.getSelectedIndex() == 5) {
			quantumTimeField.setEnabled(true);
		} else {
			quantumTimeField.setEnabled(false);
		}
	}
}
