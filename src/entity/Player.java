package entity;

import java.awt.Graphics;
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
	public final double screenX = Game.SCREEN_WIDTH  / 2 - 8;
	public final double screenY = Game.SCREEN_HEIGHT / 2 - 8;
	
	public Player(int x, int y)
	{	
		originX = x; originY = y;
		walkSpeed    =  3.0;
		dashSpeed    =  5.5;
		size         = 40.0;
		sprite       = new Sprite("nima", pose, 4, (int)x, (int)y, (int)size, (int)size);
		
		bulletVelocity = 7;
		cooldownTime = 400;
		shotCooldown = new Timer(cooldownTime, new ActionListener() 
		{ 
			@Override public void actionPerformed(ActionEvent e) {shotCooldown.stop();}
        });
		health = 2;
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
	    
	    sprite.applyFriction();
		sprite.move();
	    
	}
	
	public void draw(Graphics pen) 
	{      
		sprite.draw(pen);
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
