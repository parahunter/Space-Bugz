using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class AvatarBeaconManager : MonoBehaviour, TcpEventSubscriber
{
	public float timeForBeacon = 0.5f;
	public float scalePerSecond = 2.0f;
	public float maxScaleInScreenHeight = 0.1f;
	private float maxScale;
	public Texture2D beaconTex;
	
	private class Beacon
	{
		public Transform avatarTransform;
		public float scale;
		public float startTime;
		public Color color;
		
		public Beacon(Transform avatarTransform, float scale, float startTime, Color color)
		{
			this.avatarTransform = avatarTransform;
			this.scale = scale;
			this.startTime = startTime;
			this.color = color;
		}
	}
	
	private List<Beacon> beacons = new List<Beacon>();
	
	private AvatarManager avatarManager;
	
	void Start()
	{
		maxScale = maxScaleInScreenHeight*Screen.height;
		
		avatarManager = GetComponent<AvatarManager>();
	}
	
	private List<byte> beaconsToAdd = new List<byte>();
	
	#region TcpEventSubscriber implementation
	void TcpEventSubscriber.Recieve (byte id, byte[] packet, int lengthOfPacket)
	{
		lock(beaconsToAdd)
		{
			beaconsToAdd.Add(id);	
		}
				
		//throw new System.NotImplementedException ();
	}
	#endregion
	
	void OnGUI()
	{
		lock(beaconsToAdd)
		{
			foreach(byte id in beaconsToAdd)
				StartCoroutine(beaconAnimation(id));
			
			beaconsToAdd.Clear();
		}
		
		foreach(Beacon beacon in beacons)
		{
			DisplayBeacon(beacon);
		}
	}
	
	private void DisplayBeacon(Beacon beacon)
	{
		Vector3 screenPos = Camera.main.WorldToScreenPoint(beacon.avatarTransform.position);
			
		GUI.color = beacon.color;
		float scale = maxScale*beacon.scale;
		GUI.DrawTexture(new Rect(screenPos.x - scale, Screen.height -screenPos.y -scale, scale*2, scale*2), beaconTex);		
	}
	
	private IEnumerator beaconAnimation(byte id)
	{
		Beacon newBeacon = new Beacon(avatarManager.GetAvatarTransform(id), 0, Time.time, avatarManager.GetAvatarColor(id));
		
		beacons.Add(newBeacon);
		
		//yield return new WaitForSeconds(timeForBeacon);
		
		while(newBeacon.startTime + timeForBeacon > Time.time)
		{
			print(newBeacon.startTime);
			newBeacon.scale += scalePerSecond*Time.deltaTime;	
			yield return 0;
		}
		
		beacons.Remove(newBeacon);
		print("what");
	}
}
