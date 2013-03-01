package virtua.pad;

import java.util.concurrent.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

import android.graphics.Color;
import android.util.Log;

public class TcpWriter implements Runnable {
	
	private OutputStream output;
		
	public volatile boolean runThread = true;
	
    private ConcurrentLinkedQueue<byte[]> writeQueue = new ConcurrentLinkedQueue<byte[]>();
	    
	// Constructor
	public TcpWriter (OutputStream output) 
	{
		this.output = output;
		
	}
	
	public void Write(byte[] packet)
	{
		writeQueue.add(packet);
	}
	
	public void run() 
	{
		Log.v("threads", "thread started");
		while(runThread == true)
		{
			
			if(writeQueue.isEmpty() == false) //do we have data to send?
			{
				Log.v("tcp listen", "begin write");
				
				
				byte[] dataToSend = writeQueue.poll();
				
				Log.v("threads", "we apparently have stuff to send");
				try 
				{
					output.write(dataToSend);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Thread.yield();
			}
		}
	}
	
	/**
	 * <p>Take a String and convert it
	 * to a byte array, then send it to
	 * the server.
	 * </p>
	 * @param msg - the String to send.
	 */
	/*
	private void sendBytes (String msg) 
	{
		byte[] byteMsg = new byte[msg.length() + 1];
	    byte[] stringBytes = msg.getBytes();
	    
	    // Prevents byteMsg from being resized.
	    for(int i = 0 ; i < msg.length() ; i++)
	    	byteMsg[i] = stringBytes[i];
	    // Terminate it
	    byteMsg[byteMsg.length - 1] = 0;
		try 
		{
			out.write(byteMsg);
			out.flush();
		} 
		catch (Exception e) 
		{
			Log.e("send", e.getMessage());
		}
		
	}
	*/
}
