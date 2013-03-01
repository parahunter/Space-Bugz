package virtua.pad;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TutorialScreen extends RelativeLayout
{
	private ImageView tutImage;
	private LinearLayout buttons;
	private int tutState;
	private BackButton backButton;
	private ForwardButton forwardButton;
	private VirtuaPadClient vpClient;
	private VirtuaPadMain mainApp;
	
	public TutorialScreen(Context context, VirtuaPadClient vpClient, VirtuaPadMain mainApp) 
	{
		super(context);
		
		this.vpClient = vpClient;
		this.mainApp = mainApp;
						
		RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(
	            LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	    this.setLayoutParams(labelLayoutParams);
		
	    this.setPadding(10, 10, 10, 10);
	    
		tutImage = new ImageView(context);
		tutState = 0;
		tutImage.setImageResource(R.drawable.rotated_tutorial00);
		
		backButton = new BackButton(context, this);
		forwardButton = new ForwardButton(context, this);
		backButton.setLayoutParams(new LayoutParams(100, 50));
		forwardButton.setLayoutParams(new LayoutParams(100,50));

		buttons = new LinearLayout(context);
		
		this.addView(tutImage);
				
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		
		this.addView(backButton,params1);
		
		backButton.setVisibility(INVISIBLE);
		this.addView(forwardButton,params2);
	}
	
	public void GoBack()
	{
		switch(tutState)
		{
		case 0:
			break;
		case 1:
			tutImage.setImageResource(R.drawable.rotated_tutorial00);
			tutState = 0;
			backButton.setVisibility(INVISIBLE);
			break;
		case 2:
			tutImage.setImageResource(R.drawable.rotated_tutorial01);
			tutState = 1;
			break;
		case 3:
			tutImage.setImageResource(R.drawable.rotated_tutorial02);
			tutState = 2;
			break;
		case 4:
			tutImage.setImageResource(R.drawable.rotated_tutorial03);
			tutState = 3;
			break;	
		case 5:
			tutImage.setImageResource(R.drawable.rotated_tutorial04);
			forwardButton.setText("Next");
			tutState = 4;
			break;
		case 6:
			tutImage.setImageResource(R.drawable.rotated_tutorial05);
			forwardButton.setText("Next");
			tutState = 5;
			break;
		default:
			break;
		}
	}
	
	public void GoForward()
	{
		switch(tutState)
		{
		case 0:
			tutState = 1;
			backButton.setVisibility(VISIBLE);
			tutImage.setImageResource(R.drawable.rotated_tutorial01);
			break;
		case 1:
			tutState = 2;
			backButton.setVisibility(VISIBLE);
			tutImage.setImageResource(R.drawable.rotated_tutorial02);
			break;
		case 2:
			tutState = 3;
			backButton.setVisibility(VISIBLE);
			tutImage.setImageResource(R.drawable.rotated_tutorial03);
			break;
		case 3:
			tutState = 4;
			backButton.setVisibility(VISIBLE);
			tutImage.setImageResource(R.drawable.rotated_tutorial04);
			break;
		case 4:
			tutState = 5;
			backButton.setVisibility(VISIBLE);
			tutImage.setImageResource(R.drawable.rotated_tutorial05);
			break;
		case 5:
			tutState = 6;
			backButton.setVisibility(VISIBLE);
			forwardButton.setText("Start Game!");
			tutImage.setImageResource(R.drawable.rotated_tutorial06);
			break;	
		case 6:
			tutState = 0;
			mainApp.SetWaitLayout();
			break;
		default:
			break;
		}
	}
	
    private class ForwardButton extends Button implements OnClickListener
    {
    	private TutorialScreen tutorialScreen;
    	
    	public ForwardButton(Context context, TutorialScreen tutorialScreen) 
		{
			super(context);
									
			this.setText("Next");
			this.tutorialScreen = tutorialScreen;
			
			this.setOnClickListener(this);
		}
    	
    	@Override 
    	public void onDraw(Canvas canvas) 
    	{ 
    		canvas.save(); 
    		//float px =  getWidth()/2f;
    		
    		canvas.rotate(-180, getWidth()/2f, getHeight()/2f);
    		super.onDraw(canvas); 
    		
    		canvas.restore(); 
    	}
    	
		public void onClick(View v) 
		{
			tutorialScreen.GoForward();
		}
    }
	
    private class BackButton extends Button implements OnClickListener
    {
    	private TutorialScreen tutorialScreen;
    	
    	public BackButton(Context context, TutorialScreen tutorialScreen) 
		{
			super(context);
									
			this.setText("Back");
			this.tutorialScreen = tutorialScreen;
			
			this.setOnClickListener(this);
		}
    	
    	@Override 
    	public void onDraw(Canvas canvas) 
    	{ 
    		canvas.save();
    		
    		canvas.rotate(-180, getWidth()/2f, getHeight()/2f);
    		super.onDraw(canvas); 
    		
    		canvas.restore(); 
    	}
    	
		public void onClick(View v) 
		{
			tutorialScreen.GoBack();
		}
    }
}