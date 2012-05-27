using UnityEngine;
using System.Collections;

public class ExplosionOnImpact : MonoBehaviour 
{
	public float explosionRadius = 5.0f;
	public float explosionForce = 50.0f;
	
	public int damage = 1337;
	
	void OnCollisionEnter(Collision col)
	{
		Vector3 explosionPos = transform.position;
		
  		Collider[] colliders = Physics.OverlapSphere(explosionPos, explosionRadius);
        
		foreach (Collider hit in colliders) 
		{
            
			if(!hit)
            	continue;
				
            if (hit.rigidbody)
			{
                hit.rigidbody.AddExplosionForce(explosionForce, explosionPos, explosionForce);
				
				/*
				if(hit.tag == "Avatar")
					hit.GetComponent<Avatar>().TakeDamage(damage, GetComponent<ShootByAvatar>().id);
				*/
			}
        }
		
		Destroy(gameObject);
	}
}
