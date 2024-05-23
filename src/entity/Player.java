package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Rect;
import engine.Sprite;
import main.Game;
import main.InputHandler;
import states.*;

public class Player extends Entity
{
	public final double screenX   = Game.SCREEN_WIDTH  / 2 - 8;
	public final double screenY   = Game.SCREEN_HEIGHT / 2 - 8;
	
	public int maxHeartContainers = 4;
	
	Image heartContainer;
	Image fullHeart;
	
	int   heartX, heartY;
	
	public Player(int x, int y)
	{	
		originX = x; originY = y;
		walkSpeed    =  3.0;
		dashSpeed    =  5.5;
		size         =   40;
		sprite       = new Sprite("nima", pose, 4, x, y, size, size);
		
		bulletVelocity = 7;
		cooldownTime = 400;
		shotCooldown = new Timer(cooldownTime, new ActionListener() 
		{ 
			@Override public void actionPerformed(ActionEvent e) {shotCooldown.stop();}
        });
		
		health = maxHeartContainers * 4; //3 hearts, each with 4 quarters
		
		//THIS WILL BE MOVED TO AN OVERWORLD UI CLASS IN THE FUTURE
		heartContainer  = Toolkit.getDefaultToolkit().getImage("res/overworld ui/heart_container.png");
		fullHeart		= Toolkit.getDefaultToolkit().getImage("res/overworld ui/full_heart.png");
		heartX 			= 10 * Game.SCALE;
		heartY			= /*Game.SCREEN_HEIGHT - 16 * Game.SCALE -*/ 10;
	}
	
	public void update()
	{
		sprite.moving = false; //stops sprite animation when player is not moving
		
	    //set speed based on whether or not you're pressing shift
	    if (pressing[SHIFT]) speed = dashSpeed;
	    else                 speed = walkSpeed;
	    
		//adjust the speed when moving diagonally
	    if ((pressing[_W] && (pressing[_A] ^ pressing[_D])) ||
	        (pressing[_S] && (pressing[_A] ^ pressing[_D])) )
	    //^ is java's XOR operator, not allowing both A and D to be pressed prevents the speed from changing when both are pressed
	    {
	        speed = (speed * 8/10);
	    }
	    
	    if (pressing[_W]) sprite.moveUP(speed);
	    if (pressing[_S]) sprite.moveDN(speed);
	    if (pressing[_A]) sprite.moveLT(speed);
	    if (pressing[_D]) sprite.moveRT(speed);
	    
	    if (shooting() && !shotCooldown.isRunning()) fire();
	    
	    move();  
	}
	
	public void move()
	{
		sprite.applyFriction();
		sprite.move();
	}
	
	public void handleCollisions()
	{
		//player collides with skeletons
	    for (Skeleton s : OverWorld.skeletons) if (s != null && this.sprite.overlaps(s.sprite))
	    {
	    	this.sprite.pushOutOf(s.sprite);
	    }
	    
	    //player collides with skellington
	    if (OverWorld.skellington != null && this.sprite.overlaps(OverWorld.skellington.sprite)) this.sprite.pushOutOf(OverWorld.skellington.sprite);
	    
	    //player collides with monk2
	    if (OverWorld.monk2       != null && this.sprite.overlaps(      OverWorld.monk2.sprite)) this.sprite.pushOutOf(      OverWorld.monk2.sprite);
	}
	
	public void draw(Graphics pen) 
	{
		sprite.draw(pen);
    }
	
	//THIS WILL BE MOVED TO AN OVERWORLD UI CLASS IN THE FUTURE//
	public void drawHeartContainers(Graphics pen)
	{
		for (int i = 0; i < maxHeartContainers; i++)
		{
			pen.drawImage(heartContainer, heartX, heartY, 16 * Game.SCALE, 16 * Game.SCALE, null);
			
			heartX += 17 * Game.SCALE;
		}
		heartX = 10 * Game.SCALE;
	}
	
	public void drawHearts(Graphics pen)
	{
		
		for (int i = 1; i <= maxHeartContainers; i++)
		{
			if (health >= i * 4) pen.drawImage(fullHeart, heartX, heartY, 16 * Game.SCALE, 16 * Game.SCALE, null);
			else //only draw leading heart
			{
				int leadingHealth  = 4 - ((i * 4) - health);
				if (leadingHealth != 0) pen.drawImage(Toolkit.getDefaultToolkit().getImage("res/overworld ui/heart_" + leadingHealth + ".png"), heartX, heartY, 16 * Game.SCALE, 16 * Game.SCALE, null);
				heartX = 10 * Game.SCALE;
				return;
			}
			heartX += 17 * Game.SCALE;
		}
		heartX = 10 * Game.SCALE;
	}
	
	public void drawHealth(Graphics pen)
	{
		drawHeartContainers(pen);
		drawHearts(pen);
	}
	
	public void fireBullet(Sprite r, int vx, int vy)
	{
        OverWorld.bulletManager.createPlayerBullet(
        		new Sprite("ShurikenMagic", projectilePose, 2, 8, (int) r.x + (int)(r.w * .5) -(int)(bW * .5), (int) r.y + (int)(r.h * .5 - bH * .5), bW, bH),
        		vx, vy, "Magic1.wav");
	}
	
	private void fire() 
	{	
		if (pressing[RT] && !(pressing[UP] || pressing[DN] || pressing[LT])) fireBullet(sprite,  bulletVelocity, 0); //only right
        if (pressing[LT] && !(pressing[UP] || pressing[DN] || pressing[RT])) fireBullet(sprite, -bulletVelocity, 0); //only left
        
        if (pressing[DN] && !(pressing[LT] || pressing[RT]|| pressing[UP])) fireBullet(sprite, 0,  bulletVelocity); //only down
        if (pressing[UP] && !(pressing[LT] || pressing[RT]|| pressing[DN])) fireBullet(sprite, 0, -bulletVelocity); //only up    
       
        if (pressing[UP] && pressing[RT] && !(pressing[LT] || pressing[DN])) fireBullet(sprite,  bulletVelocity*8/10, -bulletVelocity*8/10); //only right & up
        if (pressing[DN] && pressing[RT] && !(pressing[LT] || pressing[UP])) fireBullet(sprite,  bulletVelocity*8/10,  bulletVelocity*8/10); //only right & down
        
        if (pressing[DN] && pressing[LT] && !(pressing[UP] || pressing[RT])) fireBullet(sprite, -bulletVelocity*8/10,  bulletVelocity*8/10); //left & down
        if (pressing[UP] && pressing[LT] && !(pressing[RT] || pressing[DN])) fireBullet(sprite, -bulletVelocity*8/10, -bulletVelocity*8/10); //left & up
        
        shotCooldown.start();
	}
	
	public boolean shooting() 
	{
		return (pressing[RT] || pressing[LT] || pressing[DN] || pressing[UP]);
	}	
}
