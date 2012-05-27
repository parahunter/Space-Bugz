using UnityEngine;
using System.Collections;

public class EnvirornmentDamageToAvatar : MonoBehaviour {

	public int damage = 1337;
	
	void OnTriggerStay(Collider other)
	{
		if(other.tag == "Avatar")
		{
			other.GetComponent<Avatar>().TakeDamage(damage, -1);
			//other.SendMessage("TakeDamage", damage);
		}
	}
	
	void OnTriggerEnter(Collider other)
	{
		if(other.tag == "Avatar")
		{
			other.GetComponent<Avatar>().TakeDamage(damage, -1);
			//other.SendMessage("TakeDamage", damage);
		}
	}
}
