using UnityEngine;
using System.Collections;
using System.IO;
using System;
using System.Collections.Generic;

public class LogManager : MonoBehaviour 
{
	public string pathToFolder = "C:/VirtuaPadLogs/";
	public string folderText = "planet log data ";
	public float timeBetweenWrites = 2f;
		
	private string folderPath;
	private string dateString;
	
	private AvatarManager avatarManager;
	
	private Dictionary<byte, StreamWriter> streams = new Dictionary<byte, StreamWriter>();
		
	void Awake()
	{
		folderPath = CreateFolder();
		avatarManager = GetComponent<AvatarManager>();
		
		StartCoroutine(WriteToLogs());
	}
	
	public void CreateClientLog(byte id)
	{
		#if UNITY_STANDALONE_WIN
		lock(streams)
		{
			
			StreamWriter stream = File.CreateText(folderPath + "/" + "client " + id + ".txt");
			streams.Add(id, stream);
			stream.WriteLine("log file for client " + id);
			stream.Flush();
		}
		#endif
	}
	
	public void CloseClientLog(byte id)
	{
		lock(streams)
		{
			streams[id].Close();
			streams.Remove(id);
		}
	}
	
	private IEnumerator WriteToLogs()
	{
		while(true)
		{
			yield return new WaitForSeconds(timeBetweenWrites);
			
			lock(streams)
			{
				foreach(KeyValuePair<byte,StreamWriter> streamPair in streams)
				{
					//print("writing to client " + streamPair.Key);
					string data = Time.realtimeSinceStartup + "	";
					
					data += VecToString( avatarManager.GetAvatarPos( streamPair.Key));
					
					streamPair.Value.WriteLine(data);
					streamPair.Value.Flush();
				}
			}
		}
	}
	
	void OnApplicationExit()
	{
		StopCoroutine("WriteToLogs");
		
		lock(streams)
		{
			foreach(KeyValuePair<byte,StreamWriter> streamPair in streams)
			{
				streamPair.Value.Close();
			}	
		}
	}
	
	private string VecToString(Vector3 vec)
	{
		string str = vec.x + "	" + vec.y + "	";
		//print(str);
		return str;
	}
	
	private string CreateFolder()
	{
		dateString =  DateTime.Now.Day + "-" + DateTime.Now.Month + "-" + DateTime.Now.Year + "  " + DateTime.Now.Hour + "-" + DateTime.Now.Minute + "-" + DateTime.Now.Second;

		string path = pathToFolder + "/logs/" + folderText + " " + dateString;
		
		System.IO.Directory.CreateDirectory(path);
		
		return path;
	}
	
	
}
