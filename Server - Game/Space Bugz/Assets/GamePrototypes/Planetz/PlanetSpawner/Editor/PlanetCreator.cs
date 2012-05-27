using UnityEngine;
using UnityEditor;
using System.Collections;

public class PlanetCreator : Editor
	
{

	
		[MenuItem ("Level Editing/Remove All Planets")]
	public static void RemoveAllChunks()
	{
		GameObject[] chunks = GameObject.FindGameObjectsWithTag("PlanetChunk");
		
		foreach(GameObject chunk in chunks)
		{
			DestroyImmediate(chunk);
		}
	}
	
	[MenuItem ("Level Editing/Rebake planets")]
    static void RebakePlanets () 
	{
		RemoveAllChunks();
		CreateAllPlanets();
	}
	
	public static void CreateAllPlanets()
	{
		PlanetSpawner[] spawners = FindObjectsOfType(typeof(PlanetSpawner)) as PlanetSpawner[];
			
		foreach(PlanetSpawner spawner in spawners)
		{
			//print(spawner.name);
			spawner.CreatePlanet(spawner.transform.position, spawner.innerRadius, spawner.layers, spawner.transform);
		}
	}
}
