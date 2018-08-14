package com.mlfq.main;

import java.awt.EventQueue;

import com.mlfq.frame.Frame;

public class Main
{
	public Main()
	{
		 Frame frame = new Frame();
	}
	
    public static void main(String[] args)
    {
        Runnable runner = new Runnable()
        {
        	public void run()
        	{
        		new Main();
        	}
        };
        EventQueue.invokeLater(runner);
    }
}
