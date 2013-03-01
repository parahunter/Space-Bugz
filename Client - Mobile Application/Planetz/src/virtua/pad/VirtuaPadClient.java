package virtua.pad;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.*;
import java.io.IOException;
import java.net.*;

import android.text.Html.TagHandler;
import android.util.Log;

enum clientState{disconnected, obtainingID, hasID, joinedGame, couldNotConnect};

public class VirtuaPadClient
{
	private clientState state = clientState.disconnected;
    public boolean isConnected()
    {
    	if(state != clientState.couldNotConnect || state != clientState.disconnected)
    		return true;
    	else
    		return false;
    }
    
    private final byte tagNewId 	  = 0;
    private final byte tagWantsToJoin = 1;
    private final byte tagJoinAccepted = 2;
    private final byte tagServerShutdown = (byte)255;
    
    private Hashtable<int[], ClientTcpEventSubscriber> tcpEventSubscribers = new Hashtable<int[], ClientTcpEventSubscriber>();
    
    private byte id;
    public int getID()
    {
    	return (int)id & 0xff;
    }
    
    private int udpServerPort = 60000;
    public int getUdpServerPort()
    {
    	return udpServerPort;
    }
    
    private int tcpServerPort = 50000;
    private InetAddress serverAddress;
    private String serverName;
    private Socket socket;
    
    private Thread udpSendThread;
    private UdpSender udpSender;
    
    private Thread tcpWriteThread;
    private TcpWriter tcpWriter;
    
    private Thread tcpListenThread;
    private TcpListen tcpListen;
    
    private Thread listenForIpThread;
    private ListenForServerIp serverIpListener;
    private VirtuaPadMain mainApp;
    
    public VirtuaPadClient(VirtuaPadMain mainApp)
    {
    	state = clientState.disconnected;
    	this.mainApp = mainApp;
    	//GetServerIp();
    }
    
    public void Connect()
    {
    	Thread tryToConnectThread = new Thread(new ConnectRunnable());
    	tryToConnectThread.start();
    }
    private class ConnectRunnable implements Runnable
    {
    	public void run() 
		{
			// TODO Auto-generated method stub
		   	try 
	    	{
	    		StartNetworkThreads("192.168.40.135");
		    }
	    	catch(IOException e)
	    	{	
	    		try
	    		{
	    			StartNetworkThreads("192.168.1.100");
	    		}
	    		catch(IOException e2)
	    		{
	    			SetConnectionState(clientState.couldNotConnect);
	    		}
	    	}
		}
    }
    
    private void GetServerIp()
    {
    	serverIpListener = new ListenForServerIp(this);
    	listenForIpThread = new Thread(serverIpListener);
    	listenForIpThread.start();
    }
    
    private void StartNetworkThreads(String hostname) throws IOException
    {	
    	this.serverName = hostname;
  
	    serverAddress = InetAddress.getByName(serverName);
	    
		SocketAddress socketAddress = new InetSocketAddress(serverAddress, tcpServerPort);
			// Unconnected socket :(
		socket = new Socket();
			// Connect, but with a timeout
		socket.connect(socketAddress, 1000);
			
		//set socket to perform networking with no delay = faster sending but low packet effeciency
		socket.setTcpNoDelay(true);
		
        tcpListen = new TcpListen(socket.getInputStream() , this);
        tcpListenThread = new Thread(tcpListen);
        
        tcpWriter = new TcpWriter(socket.getOutputStream());
        tcpWriteThread = new Thread(tcpWriter);
        
        udpSender = new UdpSender(serverAddress, udpServerPort, this);
    	udpSendThread = new Thread(udpSender);
    	
        Log.v("thread", "starting thread");
		tcpListenThread.start();
		tcpWriteThread.start();
		
		udpSendThread.start();
		Log.v("thread", "thread start finished");
		
		SetConnectionState(clientState.obtainingID);
    }
    
    private void StopNetworkThreads()
    {
    	if(state == clientState.couldNotConnect)
    	{
    		Log.v("fail", "barh");
    		return;
    	}
    	
    	Log.v("threads", "Start Shutdown");
    	tcpWriter.runThread = false;
    	tcpListen.runThread = false;
    	
    	try 
    	{
    		//socket.shutdownInput();
	    	//socket.shutdownOutput();
	    	
	    	socket.shutdownInput();
	    	socket.shutdownOutput();
	    	socket.close();
    		
	    	tcpWriteThread.join();
	    	tcpListenThread.join();
		} 
        catch (InterruptedException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch(NullPointerException e)
    	{
    		
    	}
    	
    	Log.v("threads", "tcp threads shutdown complete");
    	
    	udpSender.runThread = false;
    	try
    	{
    		udpSendThread.join();
    	}
    	catch(Exception e)
    	{
    		Log.e("udp", e.getMessage());
    	}
    	
    	Log.v("threads", "ended shutdown");
    }
    
    public void SetConnectionState(clientState newState)
    {
    	state = newState;
    	
    	mainApp.runOnUiThread(new SetAppState(state));
    }
    private class SetAppState implements Runnable
    {
    	private clientState stateToSet;
    	public SetAppState(clientState state)
    	{
    		stateToSet = state;
    	}
    	public void run() 
    	{
			mainApp.SetAppState(stateToSet);
		}
    }
    
    public void AddTcpEventSubscriber(int id, ClientTcpEventSubscriber subscriber)
    {
    	int[] arr = new int[1];
    	arr[0] = id;
    	tcpEventSubscribers.put(arr, subscriber);
    	
    }
    public void removeTcpEventSubscriber(int id)
    {
    	int[] arr = new int[1];
    	arr[0] = id;
    	tcpEventSubscribers.remove(arr);
    }
    
    public void joinGame()
    {
    	Log.v("button", "works");
		byte[] data = new byte[1];
    	data[0] = this.tagWantsToJoin;
    	this.SendTcpData(data);
    }
    
    public void HandleTcpData(byte[] data)
    {
    	byte tag = data[0];
    	switch(tag)
    	{
    	case tagNewId:
    		id = data[1];
    		Log.v("tcp", "has recieved id from server : " + id);
    		udpSender.StartSending(id);
    		SetConnectionState(clientState.hasID);
    		break;
    	case tagJoinAccepted:
    		if(state != clientState.joinedGame)
    			SetConnectionState(clientState.joinedGame);
    		break;
    	default:
    		int[] tagInt = new int[1];
    		tagInt[0] = (int)tag & 0xff;
    		Log.v("tcp", "length of subscribers " + tcpEventSubscribers.size());
    		
    		Enumeration<int[]> k = tcpEventSubscribers.keys();
    		
    		while( k.hasMoreElements() ) 
    		{
    			  int[] key = (int[])k.nextElement();
    			  if(key[0] == tagInt[0])
    				  tcpEventSubscribers.get(key).Recieve(data);
    		}
    		
    		/*
    		if(tcpEventSubscribers.)
    			tcpEventSubscribers.get(tagInt).Recieve(data);
    		*/
    		//Log.e("tag", "recieved tag " + tagInt[0] + " I do not know");
    		break;
    	}
    }
    
    public void SendTcpData(byte[] data)
    {
    	tcpWriter.Write(data);
    }
    
    public void SetAimVector(Vector2D aimVec)
    {
    	udpSender.setAimVector(aimVec);
    }
    
    public void setAccData(float[] accData)
    {
    	udpSender.setAccData(accData);
    }
    
    public void setIsShooting(boolean isShooting)
    {
    	udpSender.setIsShooting(isShooting);
    }
    
    public void setIsJumping(boolean isJumping)
    {
    	udpSender.setIsJumping(isJumping);
    }
    
    public void Stop()
    {
    	//serverIpListener.runThread = false;
    	StopNetworkThreads();
    }
    
    public clientState getState()
    {
    	return state;
    }
    
    public void setState(clientState newState)
    {
    	state = newState;
    }
        
    public void setID(byte newID)
    {
    	state = clientState.hasID;
    	id = newID;
    }
}
