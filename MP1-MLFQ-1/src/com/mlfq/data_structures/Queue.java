package com.mlfq.data_structures;

public class Queue
{
	private int index;
	private LinkedList processList;
	
	public Queue ()
	{
		
	}
	
	public void initialProcess(Process p)
	{
		processList = new LinkedList(p);
		//index = processList.getCurrentIndex();
		index++;
	}
	
	public void enqueue(Process data)
	{
		processList.insert(data);
		index++;	
	}
	
	public Process dequeue()
	{
		Process n = null;
		if (index != 0) {
			Node temp = processList.getHead();
			// System.out.println("Test1: "+temp.getData().getArrivalTime());
			int ind = temp.id;
			// System.out.println("Test2: "+ind);
			n = processList.delete(ind);
			index--;
			//System.out.println("Overflow");
		} else {
			System.out.println("Overflow");
		}
		return n;
	}
	
	public int getIndex()
	{
		return index;
	}
	public Process[] getProcess()
	{
		return processList.print();
	}
	
	/*public static void main(String[] args)
	 * {
		System.out.println("Test Queue");
		Queue q = new Queue();
		Process p[] = {new Process(7,5,1), new Process(5, 1,2), new Process(3, 2,5)};
		
		for(int i = 0; i < 3; i++) {
			if (i == 0) {
				q.initialProcess(p[i]);
			} else
				q.enqueue(p[i]);
		}	
		
		Process pr[] = q.getProcess();
		 for(int i = 0; i < 3; i++) {
			System.out.print(p[i].getArrivalTime()+" ");
		} 
	}*/
}
