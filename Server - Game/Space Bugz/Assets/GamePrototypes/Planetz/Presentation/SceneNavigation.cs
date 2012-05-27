using UnityEngine;
using System.Collections;

public class SceneNavigation : MonoBehaviour 
{
	public string goBackToScene = "undefined";
	public string goToScene		= "undefined";
	
	// Use this for initialization
	void Start () 
	{
	
	}
	
	// Update is called once per frame
	void Update () 
	{
		if(Input.GetKeyDown(KeyCode.LeftArrow) && goBackToScene != "undefined") 
			Application.LoadLevel(goBackToScene);
		else if(Input.GetKeyDown(KeyCode.RightArrow) && goToScene != "undefined")
			Application.LoadLevel(goToScene);
	}
}
