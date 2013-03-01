package virtua.pad;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;

//import virtua.pad.AndroidVirtuaPadMain.clientState;

import android.util.Log;

public class UdpSender implements Runnable {
	
	private InetAddress serverAddress;
	
	private int serverPort = 40000;
		
	public boolean runThread = true;
	private byte id;
	private boolean hasID = false;
	
	private float[] accData = new float[3];
	private boolean isShooting = false;
	private boolean isJumping = false;
	
	private int milisecondsPerPackage = 1000/25;
	
	private Vector2D aimVector = new Vector2D();
	
	public synchronized void setAimVector(Vector2D vec)
	{
		Log.v("aim", "aim tha bitch");
		aimVector = vec;
	}
	
	public synchronized void setAccData(float[] accData)
	{
		this.accData = accData;
	}
	
	public synchronized void setIsJumping(boolean isJumping)
	{
		this.isJumping = isJumping;
	}
	
	public synchronized void setIsShooting(boolean isShooting)
	{
		this.isShooting = isShooting;
	}
	
	//public synchronized void setShooting
	
	public void StartSending(byte id)
	{
		this.id = id;
		hasID = true;
	}
	
	public UdpSender ( InetAddress address, int port, VirtuaPadClient client) 
	{
		serverPort = port;
		serverAddress = address;
		//this.client = client;
	}
	
	public void run() 
	{
		//Log.v("progress", "start of thread");
		try {
			
			// Create UDP socket
			DatagramSocket socket = new DatagramSocket();
			//OutputStream stream = new OutputStream();
			ByteArrayOutputStream stream = new ByteArrayOutputStream(0);
			DataOutputStream dataStream = new DataOutputStream(stream);
			
			while(runThread == true) 
			{
				if(hasID == true)
				{
					stream.reset();
					
					stream.write(id);
						
					//Log.v("udp", "sending stuff " + runThread);
					
					dataStream.writeFloat(accData[0]);
					dataStream.writeFloat(accData[1]);
					dataStream.writeFloat(accData[2]);
					
					// Tell if the player is shaking the phone
					dataStream.writeBoolean(isJumping);
					
					// Tell the server if the player is shooting or not
					dataStream.writeBoolean(isShooting);
						
					dataStream.writeFloat(aimVector.x);
					dataStream.writeFloat(aimVector.y);
					
					dataStream.flush();
					
					byte[] bytes = stream.toByteArray();
					
					//Log.v("UDP", "x " + tempAccData[0] + " y " + tempAccData[1] + " z " + tempAccData[2]);
					
					// Create the UDP packet with destination
					DatagramPacket packet = new DatagramPacket(bytes,bytes.length, serverAddress,serverPort);
				
					//Log.v("packet message", "trying to send packet");
					// Send of the packet
					socket.send(packet);
				}
				
				//suspend the thread for x miliseconds
				Thread.sleep(milisecondsPerPackage);
			}
			
		}
		catch(SocketException e)
		{
			Log.e("udp error", e.toString());
		}
		catch (Exception e) 
		{
			Log.e("udp error", e.getMessage());
			StackTraceElement[] stackTrace= e.getStackTrace();
			for(StackTraceElement element : stackTrace )
				Log.e("udp error trace", element.toString());
			
			e.printStackTrace();
		}
		
		
	}

}
