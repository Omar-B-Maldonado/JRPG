package entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.Timer;

import engine.Rect;
import engine.Sprite;
import engine.Wall;
import main.Game;
import main.InputHandler;
import states.*;

public class Player extends Entity
{
	public int maxHeartContainers = 4;
	
	Image heartContainer;
	Image fullHeart;
	
	int   heartX, heartY;
	
	public Player(int x, int y)
	{	
		super("nima", defaultPose, 4, x, y, defaultSize, defaultSize);
		setSpeeds(3, 6);
		
		bulletVelocity = 7;
		cooldownTime = 400;
		shotCooldown = new Timer(cooldownTime, new ActionListener() 
		{ 
			@Override public void actionPerformed(ActionEvent e) {shotCooldown.stop();}
        });
		
		initializeHealth(maxHeartContainers * 4);; //3 hearts, each with 4 quarters
		
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
	        speed = speed *.7;
	    }
	    
	    if (pressing[_W]) sprite.moveUP(speed);
	    if (pressing[_S]) sprite.moveDN(speed);
	    if (pressing[_A]) sprite.moveLT(speed);
	    if (pressing[_D]) sprite.moveRT(speed);
	    
	    if (shooting() && !shotCooldown.isRunning()) fire();
	    
	    move();  
	}
	
	public void handleCollisions()
	{ 
	    Iterator<Entity> iterator = OverWorld.entities.iterator();
		while (iterator.hasNext()){
			Entity e = iterator.next();
			if (e instanceof Player) continue;
			collideWith(e);
		}
	    collideWith(OverWorld.walls);
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
			if (currentHealth >= i * 4) pen.drawImage(fullHeart, heartX, heartY, 16 * Game.SCALE, 16 * Game.SCALE, null);
			else //only draw leading heart
			{
				int leadingHealth  = 4 - ((i * 4) - currentHealth);
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
        		vx, vy, "Magic1");
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
