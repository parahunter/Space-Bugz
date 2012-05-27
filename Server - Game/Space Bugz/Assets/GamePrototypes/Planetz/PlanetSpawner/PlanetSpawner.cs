using UnityEngine;
using System.Collections;

public class PlanetSpawner : MonoBehaviour {
	
	
	public float innerRadius = 3.0f;
	public int layers = 3;
	
	public int currentLayer = 8;
	
	public Transform PlanetChunkInner;
	public Transform PlanetChunkMiddle;
	public Transform PlanetChunkBorder;
		
	public float widthOfChunk = 1.0f;
	
	public void OnDrawGizmos()
	{
		Gizmos.color = Color.green;
		
		Gizmos.DrawWireSphere(transform.position, innerRadius);
		Gizmos.color = Color.green;
		Gizmos.DrawWireSphere(transform.position, innerRadius + widthOfChunk*layers);
		
	}
	
	public void Start () 
	{
		particleSystem.Simulate(20);
		particleSystem.Play();
		//GameObject.Find("PlanetManager").GetComponent<PlanetCreator>().CreatePlanet(transform.position, innerRadius, layers, transform);
	}
	
	public void CreatePlanet(Vector3 center, float innerRadius, int numOfLayers, Transform planetCenter)
	{
		int chunkCount = 0;
		//for each layer
		for(int layer = 0 ; layer < numOfLayers ; layer++ )
		{
			float radius = innerRadius + widthOfChunk*layer; //get the current radius
			
			//calculate the length of this layer shell
			float circumferenceLength = radius*2*Mathf.PI;
		
			//get how many chunks we can fit in that distance
			int numOfChunks = (int)( circumferenceLength / widthOfChunk );
			
			//we use a parametric function to iterate trough the circumference
			//this has the interval 0 < t < PI*2 since it is around the circumference of the unit circle
			float deltaT = 2*Mathf.PI / numOfChunks;
			
			//we iterate trough the length of the circumference
			for(float t = 0 ; t < 2*Mathf.PI - 0.5f*deltaT  ; t += deltaT)
			{
				chunkCount++;
				//get the position of the chunk
				Vector3 chunkPos = new Vector3(Mathf.Cos(t)*radius, Mathf.Sin(t)*radius, 0) + center;
				
				//and spawn a new chunk
				Transform newChunk;
				
				
				if(layer < (numOfLayers / 2))
					newChunk = (Transform)Instantiate(PlanetChunkInner,chunkPos,Quaternion.identity);
				else if(layer < (numOfLayers - 1 ))
					newChunk = (Transform)Instantiate(PlanetChunkMiddle,chunkPos,Quaternion.identity);
				else
					newChunk = (Transform)Instantiate(PlanetChunkBorder,chunkPos,Quaternion.identity);
								
				newChunk.LookAt(planetCenter.position, Vector3.back);
				
				newChunk.gameObject.layer = currentLayer;
				
				newChunk.parent = planetCenter;
			}
			
		}
		
		currentLayer++;
		
		SetParticleSystem();
		SetAtmosphereAndBackground();
		
		print("planet made with " + chunkCount + " chunks");
		
		
	}
	
	
	
	private void SetAtmosphereAndBackground()
	{
		Transform background = transform.Find("Background");
		
		float scale = (innerRadius + layers) * 5.125632f/25f;
		
		background.localScale = new Vector3(scale, scale, scale);
		
		GravityWell well = GetComponent<GravityWell>();
		
		if(well != null)
		{
			Debug.Log("yes!");
			
			Transform atmosphere = transform.Find("Atmosphere");
			
			scale = GetComponent<SphereCollider>().radius*0.21875f;
			atmosphere.localScale = new Vector3(scale, scale, scale);
			
			
			
		}
	}
	
	/// <summary>
	/// Sets the particle system based on the inner radius of the planet.
	/// </summary>
	private void SetParticleSystem()
	{
		particleSystem.startSpeed = 1.5f * innerRadius / 5;
		
		particleSystem.startLifetime = 1.5f * innerRadius / 6;
		
		
		
		particleSystem.startSize = 1 * innerRadius / 5;
		
		particleSystem.Simulate(20);
	}
	
}
