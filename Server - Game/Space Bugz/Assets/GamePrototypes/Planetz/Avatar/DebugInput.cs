using UnityEngine;
using System.Collections;

public class DebugInput : MonoBehaviour 
{
	
	private AvatarMovement moveScript;
	private AvatarShoot shootScript;
	
	// Use this for initialization
	void Start () 
	{
		moveScript = GetComponent<AvatarMovement>();
		shootScript = GetComponent<AvatarShoot>();
	}
	
	public void Update()
	{
		if(Input.GetButtonDown("Fire1"))
		{
			Vector3 mouseWorldPos = Camera.main.ScreenToWorldPoint( Input.mousePosition );
			mouseWorldPos.z = 0;
			
			Vector3 direction = mouseWorldPos - transform.position;
			
			shootScript.Shoot(direction);
		}	
	}
	
	// Update is called once per frame
	public void FixedUpdate () 
	{
		Vector3 input = new Vector3(Input.GetAxis("Horizontal"), Input.GetAxis("Vertical"),0);
		
		moveScript.ApplyMovement(input);
		
		if(Input.GetKeyDown(KeyCode.Space))
			moveScript.ApplyJump();
	
	}
}
