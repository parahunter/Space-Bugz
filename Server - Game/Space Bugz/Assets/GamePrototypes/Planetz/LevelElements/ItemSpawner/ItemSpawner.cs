using UnityEngine;
using System.Collections;

public class ItemSpawner : MonoBehaviour 
{
	public Transform cratePrefab;
	public float minTimebetweenSpawns = 20;
	public float maxTimebetweenSpawns = 30;
		
	private SpawnPointManager mSpawnPointManager;
	
	
	// Use this for initialization
	void Start () 
	{
		mSpawnPointManager = GetComponent<SpawnPointManager>();
		
		StartCoroutine( SpawnStuff() );
	}
	
	
	
	private IEnumerator SpawnStuff()
	{
		while(true)
		{
			yield return new WaitForSeconds( Random.Range(minTimebetweenSpawns,maxTimebetweenSpawns) );
			
			Instantiate(cratePrefab, mSpawnPointManager.GetRandomSpawnPoint() , Quaternion.identity );
		}
	}
}
