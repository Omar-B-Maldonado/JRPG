package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Camera;
import engine.Rect;
import engine.Sprite;
import engine.Wall;
import states.OverWorld;
import ui.HealthBar;

public class Skellington extends Entity
{
	public Skellington(int x, int y)
	{
		originX = x; originY = y;
		initializeHealth(6);
		walkSpeed    =  2.0;
		dashSpeed    =  2.6;
		size         =   40;
		
		spawnNode    = new Rect  (originX, originY, 1, 1);
		sprite       = new Sprite("skeleton", pose, 4, x, y, size, size);
		
		healthBar = new HealthBar((int)sprite.x + 4, (int)sprite.y - 15, 24, 9, 7, maxHealth);
		healthBar.setColor(Color.GREEN);
	}
	public void update()
	{
		sprite.moving = false;
		
		distanceFromPlayer = (Math.abs(sprite.x - OverWorld.player.sprite.x) + Math.abs(sprite.y - OverWorld.player.sprite.y));
		
		if (distanceFromPlayer < 280) runFrom(OverWorld.player.sprite, dashSpeed);
		
		move();
		
		healthBar.setPosition((int)sprite.x +  4, (int)sprite.y - 15);
	}
	
	public void move()
	{
		sprite.applyFriction();
		sprite.move();
	}
	
	public void handleCollisions()
	{
		collideWith(OverWorld.monk2);
    	collideWith(OverWorld.player);
    	collideWith(OverWorld.skeletons);
	    collideWith(OverWorld.walls);
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
}
