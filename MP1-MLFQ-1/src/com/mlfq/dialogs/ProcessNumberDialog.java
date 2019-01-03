package com.mlfq.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mlfq.menu_bar.MenuBar;
import com.mlfq.panels.ProcessControlBlockPanel;
import com.mlfq.utilities.NumberKeyListener;

import net.miginfocom.swing.MigLayout;

public class ProcessNumberDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JPanel panel, buttonPanel;
	private JLabel label;
	private JTextField textField;
	private JButton submitButton, cancelButton;
	
	private NumberKeyListener numberKeyListener = NumberKeyListener.getInstance();

	public ProcessNumberDialog()
	{
		setLayout(new MigLayout("fillx, insets 20"));
		addComponents();
		setSize(new Dimension(350, 150));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void addComponents()
	{
		label = new JLabel("Enter number of processes to generate (MIN: 1; MAX: 20)", JLabel.CENTER);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(JTextField.CENTER);
		numberKeyListener.addNumkeyListener(textField);
		
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");
		submitButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		buttonPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow, 50%][grow, 50%]"));
		buttonPanel.add(submitButton, "align right");
		buttonPanel.add(cancelButton, "align left");
		
		panel = new JPanel(new MigLayout("fillx", "[grow, 40%][grow, 20%][grow, 40%]"));
		panel.add(label, "spanx 3, grow, align center, wrap");
		panel.add(textField, "grow, align center, cell 1 1, wrap");
		panel.add(buttonPanel, "spanx 3, grow, align center");
		
		add(panel, "grow, align center");
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == submitButton) {
			if (textField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(new JFrame(), "Number field cannot be blank.", "ERROR", JOptionPane.ERROR_MESSAGE);
			} else {
				if (Integer.parseInt(textField.getText()) < 1 || Integer.parseInt(textField.getText()) > 20) {
					JOptionPane.showMessageDialog(new JFrame(), "Number must be between 1 and 20.", "ERROR", JOptionPane.ERROR_MESSAGE);
				} else {
					ProcessControlBlockPanel.generateRandomizedData(Integer.parseInt(textField.getText()));
					MenuBar.setEnabledGenerateButton(false);
					dispose();
				}
			}
		} else if (event.getSource() == cancelButton) {
			dispose();
		}
	}
}
