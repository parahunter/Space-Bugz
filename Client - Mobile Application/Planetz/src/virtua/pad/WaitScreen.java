package virtua.pad;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class WaitScreen extends RelativeLayout
{
	private VirtuaPadClient vpClient;
	private VirtuaPadMain mainApp;
	private ImageView iw;
	private AvatarImage avatarImage;
	private final long delay = 1500;
	
	public WaitScreen(Context context, VirtuaPadClient vpClient, VirtuaPadMain mainApp) 
	{
		super(context);
		
		this.vpClient = vpClient;
		this.mainApp = mainApp;
		
		iw = new ImageView(context);
		iw.setImageResource(R.drawable.rotated_wait_screen_0);
		
		RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(
	            LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		this.addView(iw, imageLayoutParams);
		
		avatarImage = new AvatarImage(this.getContext(), vpClient.getID());
		
		RelativeLayout.LayoutParams avatarParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		avatarParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		avatarParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		this.addView(avatarImage, avatarParams);
		
		this.vpClient.joinGame();
	}
	
	public void DelayAndGotoGameScreen()
	{
		
		iw.setImageResource(R.drawable.rotated_wait_screen_1);
		this.postDelayed(
						new Runnable() 
						{
							public void run() 
							{
								mainApp.setGameLayout();
							}
						}
					, delay);
		
		//mainApp.runOnUiThread(action)
	}
	
	//private class Delay()
}