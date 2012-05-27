using UnityEngine;
using System.Collections;

public class ShowAsGizmo : MonoBehaviour {
	
	public Color gizmoColor = Color.green;
	public float gizmoRadius = 5.0f;
	public bool  gizmoDrawSolid = false;
	
	void OnDrawGizmos()
	{
		Gizmos.color = gizmoColor;
		
		if(gizmoDrawSolid)
			Gizmos.DrawSphere(transform.position,gizmoRadius);
		else
			Gizmos.DrawWireSphere(transform.position,gizmoRadius);
	}
}
