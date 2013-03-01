package virtua.pad;

import java.text.DateFormat.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AvatarImage extends RelativeLayout
{
	private ImageView mainImage;
	private ImageView overlayImage;
	
	public AvatarImage(Context context, int id) 
	{
		super(context);
				
		int textureIndex = (254 - id) % 4;
		
		int mainTexId;
		int overlayTexId;
		
		try {
		    Class res = R.drawable.class;
		    String mainTexName = "rotated_avatar_main_" + textureIndex;
		    String overlayTexName = "rotated_avatar_overlay_" + textureIndex;
		    
		    java.lang.reflect.Field field = res.getField(mainTexName);
		    
		    mainTexId = field.getInt(null);
		    
		    field = res.getField(overlayTexName);
		    
		    overlayTexId = field.getInt(null);
		}
		catch (Exception e) 
		{
			mainTexId = R.drawable.rotated_avatar_main_3;
			overlayTexId = R.drawable.rotated_avatar_overlay_3;
			
		    Log.e("avatar", "Failure to get drawable id.", e);
		}
		
		mainImage = new ImageView(context);
		overlayImage = new ImageView(context);
		
		mainImage.setImageResource(mainTexId);
		overlayImage.setImageResource(overlayTexId);
		
		int colorID = (254 - id) % 7;
		
		switch(colorID)
		{
		case 0:
			mainImage.setColorFilter(Color.rgb(255, 0, 0), PorterDuff.Mode.MULTIPLY);
			break;
		case 1:
			mainImage.setColorFilter(Color.rgb(0, 255, 0), PorterDuff.Mode.MULTIPLY);
			break;
		case 2:
			mainImage.setColorFilter(Color.rgb(255, 0, 255), PorterDuff.Mode.MULTIPLY);
			break;
		case 3:
			mainImage.setColorFilter(Color.rgb(0, 0, 255), PorterDuff.Mode.MULTIPLY);
			break;
		case 4:
			mainImage.setColorFilter(Color.rgb(0, 255, 255), PorterDuff.Mode.MULTIPLY);
			break;	
		case 5:
			mainImage.setColorFilter(Color.rgb(255, 255, 0), PorterDuff.Mode.MULTIPLY);
			break;
		case 6:
			mainImage.setColorFilter(Color.rgb(255, 255, 0), PorterDuff.Mode.MULTIPLY);
			break;
		default:
			break;
		}
		
		this.addView(mainImage);
		this.addView(overlayImage);
		
		//this.setImageResource(drawableId);
		
		//else
	}

}
