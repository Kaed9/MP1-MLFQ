package com.mlfq.data_structures;

public class Node
{
	Node next = null;
	Node prev = null;
	int id;
	
	private Process data;
	
	public Node(int id, Process data)
	{
		this.id = id;
		this.data = data;
	}
	
	public void setData(Process data)
	{
		this.data = data;
	}
	
	public Process getData()
	{
		return data;
	}
}
