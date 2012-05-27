using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Gravity : MonoBehaviour 
{
	public float gravitationalConstant = 100.0f;
	public float planetAllignmentSpeed = 5.0f;
	
	private List<GravityWell> wells = new List<GravityWell>();
	private List<Transform> wellTransforms = new List<Transform>();
				
	public void ApplyGravity(Transform transformToAffect,Rigidbody rigidbodyToAffect)
	{
				
		//get distance to first well
		Vector3 shortestVecToWell = wellTransforms[0].position - transformToAffect.position;
		int selectedWell = 0;
		
		//compare with rest of the wells
		
		for(int i = 1 ; i < wells.Count ; i++)
		{
			Vector3 VecToWell = wellTransforms[i].position - transformToAffect.position;
			
			if(VecToWell.sqrMagnitude < shortestVecToWell.sqrMagnitude)
			{
				shortestVecToWell = VecToWell;
				selectedWell = i;	
			}
		}
		
		//now we calculate strengh of gravity
		
		Vector3 gravity = gravitationalConstant*shortestVecToWell.normalized*wells[selectedWell].acceleration;// / shortestVecToWell.sqrMagnitude;
		
		Debug.DrawRay(transformToAffect.position, gravity);
		
		//apply it
		rigidbodyToAffect.AddForce(gravity,ForceMode.Acceleration);
		
		//get entity to point in the right direction
		transformToAffect.up = Vector3.Slerp(transformToAffect.up, -gravity.normalized, planetAllignmentSpeed*Time.fixedDeltaTime);
		
	}
	
	
	public void AddGravityWell(GravityWell well)
	{
		wells.Add(well);
		wellTransforms.Add(well.transform);
	}
	
	public void RemoveGravityWell(GravityWell well)
	{
		wells.Remove(well);
		wellTransforms.Remove(well.transform);
	}
}
