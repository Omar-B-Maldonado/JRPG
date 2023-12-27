package engine;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation 
{
	/* the general way of handling an animation is to use an array
	 * 
	 * we can't just make a new image because images are tied to the hardware
	 */
	
	private Image[] image;
	
	private int current = 0;
	
	private int duration; //how long we keep a particular image on the screen
	private int delay;	  //countdown
	
	public Animation(String directory, String name, int imageCount, int duration)
	{
		this.duration = duration;
		delay 		  = duration;
		
		image = new Image[imageCount];
		
		for(int i = 0; i < imageCount; i++)
		{
			image[i] = Toolkit.getDefaultToolkit().getImage(directory + name + "_" + i + ".png");
		}	
	}
	
	public Image getStaticImage()
	{
		return image[0];
	}
	
	public Image getCurrentImage()
	{
		delay--;
		
		if (delay == 0)
		{
			current++;
			
			if (current == image.length) current = 0;
			
			delay = duration;
		}
		return image[current];
	}
	
	public boolean nextFrameIsFirstFrame() 
	{
		return ((delay - 1 == 0) && (current + 1 == image.length));
	}
         
}
