package virtua.pad;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import android.util.Log;

public class ListenForServerIp implements Runnable
{
	public boolean runThread = true;
	
	VirtuaPadClient vpClient;
	
	public ListenForServerIp(VirtuaPadClient vpClient)
	{
		this.vpClient = vpClient;
	}
	
	public void run() 
	{
		DatagramSocket socket;
		byte[] buffer = new byte[1024];
		DatagramPacket pack = new DatagramPacket(buffer, 1024);
		
		//pack.setSocketAddress(socketAddress);
		Log.v("server ip", "started listening");
		
		while(runThread)
		{
			try 
			{
				//Log.v("server ip", InetAddress.getLocalHost().getHostName());
				//SocketAddress socketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostName(), vpClient.getUdpServerPort());
				
				socket = new DatagramSocket(60005, InetAddress.getByName("255.255.255.255"));
				  
				socket.setBroadcast(true);
				socket.setSoTimeout(5000);
				socket.receive(pack);
				
				Log.v("server ip", "we recieved something");
			} 
			catch (SocketException e) 
			{
				Log.e("server ip", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) 
			{
				Log.e("server ip", e.getMessage());				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				runThread = false;
			}
			
			String message = new String(buffer);
			
			Log.v("server ip", "recieved " + message);
			
			if(message.contains("VPServerIp:"))
			{
				Log.v("server ip", "got ip from server!");
				
				String serverIp =  message.substring(10);
				
				Log.v("server ip", "server ip is " + serverIp);
				
				return;
			}
		}
	}
	
}
