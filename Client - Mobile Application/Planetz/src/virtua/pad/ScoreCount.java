package virtua.pad;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.SoundPool;

public class ScoreCount extends TextView implements ClientTcpEventSubscriber
{
	private SoundPool soundPool;
	private int soundID;
	boolean loaded = false;
	
	public ScoreCount(Context context) 
	{
		super(context);
		
		this.setText("  score: " + 0);
		
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		soundID = soundPool.load(context, R.raw.coin, 1);
	}
	
	@Override 
	public void onDraw(Canvas canvas) 
	{ 
		canvas.save(); 
		
		canvas.rotate(-180, getWidth()/2f, getHeight()/2f);
		super.onDraw(canvas); 
		
		canvas.restore(); 
	}
	
	public void Recieve(byte[] packet) 
	{
		Log.v("score count", "got some data");
		this.post(new SetScore(this, (int)packet[1] & 0xff));
	}
	
	private class SetScore implements Runnable
	{
		private ScoreCount scoreCount;
		private int score;
		
		public SetScore(ScoreCount scoreCount, int scoreToSet)
		{
			this.scoreCount = scoreCount;
			score = scoreToSet;
		}
		
		public void run() 
		{
			//scoreCount.score = (int)packet[1] & 0xff;
			scoreCount.setText("score: " + score);
			
			if(score != 0)
				soundPool.play(soundID, 1, 1, 1, 0, 1);
		}
		
	}
	
}
