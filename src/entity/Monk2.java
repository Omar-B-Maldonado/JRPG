package entity;

import java.awt.Graphics;

import engine.Sprite;

public class Monk2 extends Entity
{
	public Monk2(int x, int y)
	{	
		walkSpeed  = 3.0;
		dashSpeed  = 6.0;
		size       = 40.0;
		sprite     = new Sprite("monk2", Entity.pose, 1, (int)x, (int)y, 40, 40);
		health     = 20;
	}
	
	public void update()
	{
		sprite.setPose("DN");
	}
	
	public void draw(Graphics pen) 
	{      
        sprite.draw(pen);
    }
	
	public void hitFor(int x)
	{
		health -= x;
	}
}
