package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Camera;
import engine.Rect;
import engine.Sprite;
import states.OverWorld;

public class Skellington extends Entity
{
	public Skellington(int x, int y)
	{
		originX = x; originY = y;
		health       =    6;
		walkSpeed    =  2.0;
		dashSpeed    =  2.6;
		size         =   40;
		
		spawnNode    = new Rect(originX, originY, 1, 1);
		sprite       = new Sprite("skeleton", pose, 4, x, y, size, size);
		
		
		hBarWidth           = 24;
		hBarContainerWidth  = hBarWidth + 4;
		hBarContainerX      = (int)sprite.x + 4;
		hBarContainerY      = (int)sprite.y - 15;
		
		hBarTo1DamageFactor = hBarWidth / health;
	}
	public void update()
	{
		sprite.moving = false;
		
		distanceFromPlayer = (Math.abs(sprite.x - OverWorld.player.sprite.x) + Math.abs(sprite.y - OverWorld.player.sprite.y));
		
		if (distanceFromPlayer < 280) runFrom(OverWorld.player.sprite, dashSpeed);
		
		move();
		
		hBarContainerX = (int)sprite.x +  4;
		hBarContainerY = (int)sprite.y - 15;
	}
	
	public void move()
	{
		sprite.applyFriction();
		sprite.move();
	}
	
	public void handleCollisions()
	{
		//skellington collides with monk2
    	if (OverWorld.monk2 != null && this.sprite.overlaps( OverWorld.monk2.sprite)) this.sprite.pushOutOf(OverWorld.monk2.sprite);
 	    
    	//skellington collides with player
    	if				    (this.sprite.overlaps(OverWorld.player.sprite)) this.sprite.pushOutOf(OverWorld.player.sprite);
    	
    	//skellington collides with skeletons
	    for (int i = 0; i < OverWorld.skeletons.length; i++) 
	    {
	    	if (OverWorld.skeletons[i] != null && this.sprite.overlaps(OverWorld.skeletons[i].sprite))
	    	{
	    		this.sprite.pushOutOf(OverWorld.skeletons[i].sprite);
	    	}
	    }
	}
	
	double distanceFromPlayer;
	
	public boolean isInRangeOfPlayer(int upperBound, int lowerBound)
	{
		return (upperBound > distanceFromPlayer && distanceFromPlayer > lowerBound);
	}
	public boolean isAwayFromPlayerByMoreThan(int upperBound)
	{
		return (distanceFromPlayer > upperBound);
	}
	
	public void draw(Graphics pen)
	{
		sprite.draw(pen);
	}
	
	public void drawHealthBar(Graphics pen)
	{
		//health bar container
		pen.setColor(Color.BLACK);
		pen.fillRoundRect(hBarContainerX     - (int)Camera.x, hBarContainerY     - (int)Camera.y, hBarContainerWidth, 9, 7, 7);
				
		//health bar
		pen.setColor(Color.GREEN);
		pen.fillRoundRect(hBarContainerX + 2 - (int)Camera.x, hBarContainerY + 2 - (int)Camera.y, hBarWidth, 5, 6, 6);		
	}
}
