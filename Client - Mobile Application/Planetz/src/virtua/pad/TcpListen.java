package virtua.pad;

import java.util.concurrent.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

//import virtua.pad.AndroidVirtuaPadMain.clientState;

import android.graphics.Color;
import android.util.Log;

public class TcpListen implements Runnable {
	
	private InputStream input;
		
	private VirtuaPadClient vpClient;
	
	public boolean runThread = true; // Currently not in use?
	
	// Constructor
	public TcpListen (InputStream input, VirtuaPadClient client) 
	{
		this.input = input;
		vpClient = client;
	}
	
	private byte[] buffer;
	private int currentBufferPosition = 0;
	private byte lengthOfPacket = 0;
	private boolean firstByte = true;
	
	public void run() 
	{
		while(runThread)
		{
			try 
			{
				int newByte = input.read();
				
				Log.v("tcp", "read byte with walue " + newByte);
				
				if(firstByte && newByte != -1)
				{
					Log.v("tcp", "buffer size set to " + newByte);
					
					lengthOfPacket = (byte)newByte;
					buffer = new byte[lengthOfPacket];
					firstByte = false;
				}
				else
				{
					
					buffer[currentBufferPosition] = (byte)newByte;
					
					currentBufferPosition++;
					
					if(currentBufferPosition == lengthOfPacket)
					{
						currentBufferPosition = 0;
						
						firstByte = true;
						
						vpClient.HandleTcpData(buffer);
					}
				}
				
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
