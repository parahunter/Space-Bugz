package virtua.pad;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameScreen extends LinearLayout
{
    //members related to game layout
    private AimView aimView;
	private TextView tw;
	private ScoreCount scoreCount;
	private RelativeLayout buttonLayout;
	private VirtuaPadClient vpClient;
	private AvatarImage avatarImage;
	private VibrateOnHits hitVibrator;
	private ImageView logo;
	
	public GameScreen(Context context, VirtuaPadClient vpClient, int appHeight, int appWidth)
	{
		super(context);
		
		this.vpClient = vpClient;
	    this.setOrientation(LinearLayout.HORIZONTAL);
	    
	    aimView = new AimView(context, vpClient);
	    
	    aimView.setLayoutParams(new LayoutParams(appHeight, appHeight));
	    
	    hitVibrator = new VibrateOnHits(context);
	    vpClient.AddTcpEventSubscriber(Tags.vibrateOnHit, hitVibrator);
	    
	    buttonLayout = new RelativeLayout(context);
	    //buttonLayout.setPadding(0,20,0,20);
	    
	    LayoutParams layout = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    buttonLayout.setLayoutParams(layout);
	   	this.setLayoutParams(layout);
	    
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//params1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
	    
	    avatarImage = new AvatarImage(context, vpClient.getID());
	    avatarImage.setId(2000);
	    avatarImage.setOnClickListener(avatarImageOnClick);
	    
	    buttonLayout.addView(avatarImage, params1);
	    
	    scoreCount = new ScoreCount(context);
	    scoreCount.setPadding(0, 20, 0, 20);
	    vpClient.AddTcpEventSubscriber((int)3, scoreCount);
	    
		RelativeLayout.LayoutParams scoreLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		scoreLayout.addRule(RelativeLayout.ABOVE, avatarImage.getId());
	    scoreLayout.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		
	    buttonLayout.addView(scoreCount, scoreLayout);
	    
		logo = new ImageView(context);
		logo.setImageResource(R.drawable.rotated_logo);
		logo.setPadding(0, 0, 0, 0);
		
//		logo.setLayoutParams(new LayoutParams(800, 400));
		
		RelativeLayout.LayoutParams logoLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//params1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		logoLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		logoLayout.addRule(RelativeLayout.BELOW, avatarImage.getId());
		
		logo.setId(1337);
		
		//logo.setLayoutParams(logoLayoutParams);
		buttonLayout.addView(logo, logoLayout);
	    
	    this.addView(aimView);
	    this.addView(buttonLayout);
	    
	    
	}
	
	private OnClickListener avatarImageOnClick = new OnClickListener() 
	{
		public void onClick(View v) 
		{
			byte[] data = new byte[1];
	    	data[0] = (byte)Tags.avatarBeacon;
	    	vpClient.SendTcpData(data);
		}
	};


	private class VibrateOnHits implements ClientTcpEventSubscriber
	{
		Vibrator v;
		
		public VibrateOnHits(Context context)
		{
			// Get instance of Vibrator from current Context
			v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			
			// Vibrate for 300 milliseconds
			v.vibrate(300);
			
		}
		
		public void Recieve(byte[] packet) 
		{
			if(packet[1] == 0)
				v.vibrate(100);
			else if(packet[1] == 1)
				v.vibrate(300);
			else if(packet[1] == 2)
				v.vibrate(500);
		}
	}
}
