package engine;

import java.awt.Color;
import java.awt.Graphics;

import states.OverWorld;

public class Sprite extends Rect
{
	Animation[] animation;
	
	final static int UP = 0;
	final static int DN = 1;
	final static int LT = 2;
	final static int RT = 3;
	
	public int pose = DN;
	
	public boolean moving = false;
	
	public Sprite(String name, String[] pose, int imageCount, int x, int y, int w, int h) 
	{
		super(x, y, w, h);
		
		animation = new Animation[pose.length];
		
		for (int i = 0; i < pose.length; i++)
		{
			animation[i] = new Animation("res/sprites/", name + "_" + pose[i], imageCount, 18);
			//last parameter is duration of each image being on the screen
		}
	}
	
	public Sprite(String name, String[] pose, int imageCount, int durationOnScreen, int x, int y, int w, int h) 
	{
		super(x, y, w, h);
		
		animation = new Animation[pose.length];
		
		for (int i = 0; i < pose.length; i++)
		{
			animation[i] = new Animation("res/sprites/", name + "_" + pose[i], imageCount, durationOnScreen);
			//last parameter is duration of each image being on the screen
		}
	}
	
	public void setMoving(boolean value) {moving = value;}
	
	public void moveUP(double dy)
	{
		pose = UP;
		moving = true;
		goUP(dy);;
	}
	
	public void moveDN(double dy)
	{
		pose = DN;
		moving = true;
		goDN(dy);;
	}
	
	public void moveLT(double dx)
	{
		pose = LT;
		moving = true;
		goLT(dx);
	}
	
	public void moveRT(double dx)
	{
		pose = RT;
		moving = true;
		goRT(dx);;
	}
	
	public boolean isFacing(Sprite s)
	{
		if (this.isLeftOf (s) && pose == RT) return true;
		if (this.isRightOf(s) && pose == LT) return true;
		if (this.isAbove  (s) && pose == DN) return true;
		if (this.isBelow  (s) && pose == UP) return true;
		
		return false;
	}
	
	public void face(Sprite s)
	{
		if(this.isLeftOf (s)) pose = RT;
		if(this.isRightOf(s)) pose = LT;
		if(this.isAbove  (s)) pose = DN;
		if(this.isBelow  (s)) pose = UP;
	}
	
	public void setPose(String s)
	{
		if (s.equals("DN")) pose = DN;
		if (s.equals("UP")) pose = UP;
		if (s.equals("LT")) pose = LT;
		if (s.equals("RT")) pose = RT;
	}
	
	public void draw(Graphics pen) 
	{
		//pen.setColor(Color.YELLOW);
		//pen.drawRect((int)(x - Camera.x), (int)(y - Camera.y), (int) w, (int) h); //this is the sprite's rect border
		
		if (!moving)
			
			pen.drawImage(animation[pose].getStaticImage(), (int)(x - Camera.x), (int)(y - Camera.y), (int) w, (int) h, null);
			
		else
			
			pen.drawImage(animation[pose].getCurrentImage(), (int)(x - Camera.x), (int)(y - Camera.y), (int) w, (int) h, null);
	
	}

}
