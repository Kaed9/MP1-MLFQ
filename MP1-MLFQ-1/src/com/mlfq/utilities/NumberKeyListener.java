package com.mlfq.utilities;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;

public class NumberKeyListener extends JDialog
{
    private static final long serialVersionUID = 1L;
    
    private static NumberKeyListener numberKeyListener;
    
    private char c;

    public static NumberKeyListener getInstance()
    {
        return (numberKeyListener == null ? new NumberKeyListener() : numberKeyListener);
    }
    
    public NumberKeyListener()
    {
        
    }

    public void addNumkeyListener(JTextField tf)
    {
        tf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                
                if (tf.getSelectedText()!=null) {
                    String s = tf.getSelectedText();
                    tf.setText(tf.getText().replace(s,""));
                }
                
                c = e.getKeyChar();
                
                if (!((c >= '0') && (c <= '9') ||
                    (c == KeyEvent.VK_BACK_SPACE) ||
                    (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
         });
    }
}