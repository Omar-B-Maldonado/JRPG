package entity;

import java.awt.Graphics;

import javax.swing.Timer;

import engine.Rect;
import engine.Sprite;
import main.InputHandler;
import states.OverWorld;
import ui.HealthBar;

public abstract class Entity extends InputHandler
{
	public int originX, originY;
	public double speed, walkSpeed, dashSpeed;
	public int size;
	public static String[] pose = {"UP", "DN", "LT", "RT"};
	
	public Rect spawnNode;
	public Sprite sprite;
	public int currentHealth;
	public int maxHealth;
	public boolean damaged;
	public HealthBar healthBar;
	
	//for shooting
	String[] projectilePose = {""   ,    ""};
	
	public int bulletVelocity;
	int bW = 30;
	int bH = 30;	
	public Timer shotCooldown;
	int   		 cooldownTime;
	
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
	
	public void fireAt(Sprite s) 
	{		
		double rise  = s.y + (s.h / 2) - this.sprite.y - (this.sprite.h / 2);
	    double run   = s.x + (s.w / 2) - this.sprite.x - (this.sprite.w / 2);
	    
	    double angle = Math.atan2(rise, run);
		int vx = (int) (bulletVelocity * Math.cos(angle));
        int vy = (int) (bulletVelocity * Math.sin(angle));
        Sprite r     = sprite;
        
        OverWorld.bulletManager.createSkeletBullet(new Sprite("Shuriken", projectilePose, 2, 8, (int) r.x + (int)(r.w * .5) -(int)(bW * .5), (int) r.y + (int)(r.h * .5 - bH * .5), bW, bH),
        		vx, vy, "Hit6.wav");
        
        shotCooldown.start();
        sprite.face(s);
	}
	
	public void initializeHealth(int maxHealth)
	{
		this.maxHealth = maxHealth;
		currentHealth  = maxHealth;
	}
	
	public void hitFor(int amount)
	{
		currentHealth -= amount;
		damaged = true;
		if (healthBar != null) healthBar.updateWidth(currentHealth);	
		
		//healthBar.width -= amount * healthBar.widthTo1DamageFactor;
	}
	
	
	public void collideWith(Rect thing)
	{
		if (this.sprite.overlaps(thing)) sprite.pushOutOf(thing);
	}
	
	//the below 3 collision methods all call the one above
	public void collideWith(Rect[] things) //for walls
	{
		for (Rect thing : things) if (thing != null) collideWith(thing);
	}
	
	public void collideWith(Entity guy)
	{
		if (guy != null) collideWith(guy.sprite);
	}
	
	public void collideWith(Entity[] entities)
	{
		for (Entity guy : entities) if (guy != null) collideWith(guy.sprite);
	}
	
	public boolean isDead()
	{
		return currentHealth <= 0;
	}
}
