using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class SpawnPointManager : MonoBehaviour 
{
	
	private List<Vector3> mSpawnPoints = new List<Vector3>();
		
	// Use this for initialization
	void Awake () 
	{
		foreach(Transform child in transform)
		{
			mSpawnPoints.Add(child.position);
		}
	}
	
	private int nextSpawnPoint = 0;
	public Vector3 GetNextSpawnPoint()
	{
		return mSpawnPoints[ ++nextSpawnPoint % mSpawnPoints.Count ];
	}
	
	public Vector3 GetRandomSpawnPoint()
	{
		return mSpawnPoints[Random.Range(0, mSpawnPoints.Count-1)];
		
	}
	
	
}
