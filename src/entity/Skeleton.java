package entity;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Rect;
import engine.Sprite;
import main.Game;
import states.OverWorld;

public class Skeleton extends Entity
{
	public Skeleton(int x, int y)
	{
		originX = x; originY = y;
		health       =   6;
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
	}
	public void update()
	{
		sprite.moving = false;
		
		distanceFromPlayer = (Math.abs(sprite.x - OverWorld.player.sprite.x) + Math.abs(sprite.y - OverWorld.player.sprite.y));
		
		if (this.isInRangeOfPlayer(325, 0)) 
		{
			if (!shotCooldown.isRunning() && isInRangeOfPlayer(285, 85)) fireAt(OverWorld.player.sprite);
			
			if (distanceFromPlayer < 175) runFrom(OverWorld.player.sprite, dashSpeed);
			
			if (distanceFromPlayer > 255)  follow(OverWorld.player.sprite, dashSpeed);	
		}
		if (this.isAwayFromPlayerByMoreThan(335)) follow(spawnNode, walkSpeed);
		
		sprite.applyFriction();
		sprite.move();
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
	
	public void follow(Rect s, double speed)
	{
		if(s.isAbove  (sprite))   sprite.moveUP(speed);
		if(s.isBelow  (sprite))   sprite.moveDN(speed);
		if(s.isLeftOf (sprite))   sprite.moveLT(speed);
		if(s.isRightOf(sprite))   sprite.moveRT(speed);
	}
	
	public void runFrom(Rect s, double speed)
	{
		if(s.isAbove  (sprite))   sprite.moveDN(speed);
		if(s.isBelow  (sprite))   sprite.moveUP(speed);
		if(s.isLeftOf (sprite))   sprite.moveRT(speed);
		if(s.isRightOf(sprite))   sprite.moveLT(speed);
	}
	
	private void fireAt(Sprite s) 
	{	
		double rise  = s.y + (s.h / 2) - this.sprite.y - (this.sprite.h / 2);
	    double run   = s.x + (s.w / 2) - this.sprite.x - (this.sprite.w / 2);
		
	    //the trig came from chatGPT
	    double angle = Math.atan2(rise, run);
		int vx = (int) (bulletVelocity * Math.cos(angle));
        int vy = (int) (bulletVelocity * Math.sin(angle));
        Sprite r     = sprite;
        
        OverWorld.bulletManager.createSkeletBullet(new Sprite("Shuriken", projectilePose, 2, 8, (int) r.x + (int)(r.w * .5) -(int)(bW * .5), (int) r.y + (int)(r.h * .5 - bH * .5), bW, bH),
        		vx, vy, "Hit6.wav");
        
        shotCooldown.start();
        sprite.face(s);
	}
	
	public void draw(Graphics pen)
	{
		sprite.draw(pen);
	}
}
