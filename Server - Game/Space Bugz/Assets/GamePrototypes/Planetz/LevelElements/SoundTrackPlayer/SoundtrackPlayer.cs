using UnityEngine;
using System.Collections;

public class SoundtrackPlayer : MonoBehaviour 
{
	public AudioClip[] tracks;
	public AudioClip[] specialTracks;
	public float volume = 0.7f;
	
	private static SoundtrackPlayer mSoundTrackPlayer;
	
	// Use this for initialization
	void Start () 
	{
		if(mSoundTrackPlayer == null)
		{	
			mSoundTrackPlayer = this;
			audio.volume = volume;
			StartCoroutine("PlayTracks");
			DontDestroyOnLoad(gameObject);
		}
		else
			Destroy(gameObject);
	}
	
	void Update()
	{
		if(Input.GetKeyDown(KeyCode.M))
		{
			audio.enabled = !audio.enabled;
			if(audio.enabled)
			{
				StopCoroutine("PlayTracks");
				StartCoroutine("PlayTracks");
			}
		}
	}
	
	public void SetTrack(int clip)
	{
		StopCoroutine("PlayTracks");
		mIsPlaying = false;
		audio.clip = specialTracks[clip];
		audio.Play();
	}
	
	void OnLevelWasLoaded()
	{
		if(GameObject.Find("SoundTrackSelector") != null)
		{
			print("found track");
			SoundTrackSelector selector = GameObject.Find("SoundTrackSelector").GetComponent<SoundTrackSelector>();
			
			SetTrack(selector.clipToSwitchTo);
		}
		else if(!mIsPlaying)
			StartCoroutine(PlayTracks());	
	}
	
	private int mNextTrack = 0;
	private bool mIsPlaying = false;	
	private IEnumerator PlayTracks()	
	{
		print("starting playlist");
		
		mIsPlaying = true;
		while(true)
		{
			audio.clip = tracks[ mNextTrack % tracks.Length ];
			audio.Play();
			
			mNextTrack++;
			
			yield return new WaitForSeconds(audio.clip.length);
			
			if(mIsPlaying == false)
				break;
		}
	}	
}
