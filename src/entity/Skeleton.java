package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Camera;
import engine.Rect;
import engine.Sprite;
import main.Game;
import states.OverWorld;

public class Skeleton extends Entity
{
	
	public Skeleton(int x, int y)
	{
		originX = x; originY = y;
		
		health       =    6;
		walkSpeed    =  2.5;
		dashSpeed    =  3.0;
		size         = 40.0;
		
		spawnNode    = new Rect(originX, originY, 1, 1);
		sprite       = new Sprite("skeleton", pose, 4, (int)x, (int)y, (int)size, (int)size);
		
		bulletVelocity = 5;
		cooldownTime   = 600;
		shotCooldown   = new Timer(cooldownTime, new ActionListener() 
		{ 
			@Override public void actionPerformed(ActionEvent e) {shotCooldown.stop();}
        });
		
		
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
		
		if (this.isInRangeOfPlayer(325, 0)) 
		{
			if (!shotCooldown.isRunning() && isInRangeOfPlayer(285, 85)) fireAt(OverWorld.player.sprite);
			
			if      (distanceFromPlayer < 175) runFrom(OverWorld.player.sprite, dashSpeed);
			
			else if (distanceFromPlayer > 255)  follow(OverWorld.player.sprite, dashSpeed);	
		}
		else if (this.isAwayFromPlayerByMoreThan(325)) follow(spawnNode, walkSpeed);
		
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
		//skele collides w/ player
    	if (this.sprite.overlaps(OverWorld.player.sprite)) this.sprite.pushOutOf(OverWorld.player.sprite);
    	
    	//skele collides with other skeles
    	for (int j = 0; j < OverWorld.skeleton.length; j++) 
        {
            if (this != OverWorld.skeleton[j] && OverWorld.skeleton[j]!= null && this.sprite.overlaps(OverWorld.skeleton[j].sprite)) 
            {
                this.sprite.pushOutOf(OverWorld.skeleton[j].sprite);
            }
        }
    	//skele collides w/ monk
    	if (OverWorld.monk2       != null && this.sprite.overlaps(OverWorld.monk2.sprite)) 		 this.sprite.pushOutOf(OverWorld.monk2.sprite);
        	
    	//skele collides w/ skellington
    	if (OverWorld.skellington != null && this.sprite.overlaps(OverWorld.skellington.sprite)) this.sprite.pushOutOf(OverWorld.skellington.sprite);
	}
	
	double distanceFromPlayer;
	
	public boolean isInRangeOfPlayer(int upperBound, int lowerBound)
	{
		return (upperBound >= distanceFromPlayer && distanceFromPlayer >= lowerBound);
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
		pen.setColor(Color.RED);
		pen.fillRoundRect(hBarContainerX + 2 - (int)Camera.x, hBarContainerY + 2 - (int)Camera.y, hBarWidth, 5, 6, 6);
			
	}
}
