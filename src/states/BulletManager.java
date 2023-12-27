package states;

import java.awt.Graphics;

import javax.swing.Timer;

import engine.Sprite;
import entity.Monk2;
import entity.Player;
import entity.Skeleton;
import entity.Skellington;
import main.Game;

public class BulletManager 
{
	public Sprite[] playerBullet = new Sprite[ 20];
	public Sprite[] skeletBullet = new Sprite[100];
		
	int p  =  0;
	int s  =  0;
	
	Player     player;
	Skeleton[] skelet;
	Monk2		monk2;
	Skellington skell;
	
	
	GameOver gameOver;
	
	public BulletManager()
	{
		gameOver = Game.over;
		player   = OverWorld.player;
		skelet   = OverWorld.skeleton;
		monk2    = OverWorld.monk2;
		skell    = OverWorld.skellington;
	}
	
	public void createPlayerBullet(Sprite q, int vx, int vy, String sound)
	{
		if (p >= playerBullet.length) p = 0;
		
		playerBullet[p] = q; //give the bullet the specified sprite
		
		playerBullet[p].setVelocity(vx, vy);
        playerBullet[p].setMoving  ( true );
        
        Game.soundManager.setSound(sound);
        Game.soundManager.play();
		
		p++;
	}
	
	public void createSkeletBullet(Sprite q, int vx, int vy, String sound)
	{
		if (s >= skeletBullet.length) s = 0;
		
		skeletBullet[s] = q; //give the bullet the specified sprite
		
		skeletBullet[s].setVelocity(vx, vy);
        skeletBullet[s].setMoving  ( true );
        
        Game.soundManager.setSound(sound);
        Game.soundManager.play();
		
		s++;
	}
	
	public void updateBullets()
	{
		for (int k = 0; k < playerBullet.length; k++) if (playerBullet[k] != null) 
		{
			playerBullet[k].move();
			
			handleSkeletHitByPlayerBullet(k);
			
			handleSkellingtonHitByPlayerBullet(k);
			
			handleMonkHitByPlayerBullet(k);
		}
		for (int k = 0; k < skeletBullet.length; k++) if (skeletBullet[k] != null) 
		{
			skeletBullet[k].move();
			
			handlePlayerHitBySkeletBullet(k);
			
			//handleMonkHitBySkeletBullet(k);
		}
	}
	
	public void drawBullets(Graphics pen)
	{
		for (int k = 0; k < playerBullet.length; k++) if (playerBullet[k] != null) playerBullet[k].draw(pen);
		for (int k = 0; k < skeletBullet.length; k++) if (skeletBullet[k] != null) skeletBullet[k].draw(pen);
	}
	
	public void handleSkeletHitByPlayerBullet(int k) 
	{
		for (int i = 0; i < skelet.length; i++)
		{
			if (skelet[i] != null && playerBullet[k] != null && playerBullet[k].overlaps(skelet[i].sprite))
			{
				playerBullet[k].knockBack(skelet[i].sprite, 40);
    			
    			skelet[i].move();	
    			
    			Game.soundManager.setSound("Kill.wav");
    			Game.soundManager.play();
    			
    			//skeles collide w/ player
		    	if (skelet[i].sprite.overlaps(player.sprite)) skelet[i].sprite.pushOutOf(player.sprite);
		    	
		    	//skeletons collide with each other
		    	for (int j = 0; j < skelet.length; j++) 
	            {if (i != j && skelet[j]!= null && skelet[i].sprite.overlaps(skelet[j].sprite)) skelet[i].sprite.pushOutOf(skelet[j].sprite);}
		    	
		    	//skeles collide w/ monk
		    	if (monk2 != null && skelet[i].sprite.overlaps(monk2.sprite)) skelet[i].sprite.pushOutOf(monk2.sprite);
	            	
		    	//skeles collide w/ skellington
		    	if (skell != null && skelet[i].sprite.overlaps(skell.sprite)) skelet[i].sprite.pushOutOf(skell.sprite);
    			
    			playerBullet[k] = null;
    				skelet[i].subtractHealth(1);
    			if (skelet[i].health < 1) skelet[i] = null;
			}
		}
	}
	
	public void handleSkellingtonHitByPlayerBullet(int k) 
	{
		if (OverWorld.skellington != null && playerBullet[k] != null && playerBullet[k].overlaps(OverWorld.skellington.sprite))
			{
				playerBullet[k].knockBack(OverWorld.skellington.sprite, 40);
    			
				OverWorld.skellington.sprite.move();
				OverWorld.skellington.sprite.applyFriction();
    			
    			Game.soundManager.setSound("Kill.wav");
    			Game.soundManager.play();
    			
    			playerBullet[k] = null;
    			OverWorld.skellington.subtractHealth(1);
    			if (OverWorld.skellington.health < 1) OverWorld.skellington = null;
			}
	}
	
	public void handlePlayerHitBySkeletBullet(int k) 
	{
		if (skeletBullet[k] != null && skeletBullet[k].overlaps(player.sprite)) 
		{
			Game.soundManager.setSound("Hit.wav");
			Game.soundManager.play();
			
			skeletBullet[k] = null;
			player.health--;
			
			if (player.health < 1) Game.stateManager.pushState(gameOver);
		}
	}
	
	public void handleMonkHitBySkeletBullet(int k)
	{
		if (OverWorld.monk2 != null && skeletBullet[k] != null && skeletBullet[k].overlaps(OverWorld.monk2.sprite)) 
		{
			skeletBullet[k] = null;
		}
	}
	
	public void handleMonkHitByPlayerBullet(int k)
	{
		if (OverWorld.monk2 != null && playerBullet[k] != null && playerBullet[k].overlaps(OverWorld.monk2.sprite))
		{
			Game.soundManager.setSound("Fx.wav");
			Game.soundManager.play();
			
			playerBullet[k] = null;
		}
	}
	
	public void pauseBullets()
	{
		for (int k = 0; k < playerBullet.length; k++) if (playerBullet[k] != null) playerBullet[k].setMoving(false);
		for (int k = 0; k < skeletBullet.length; k++) if (skeletBullet[k] != null) skeletBullet[k].setMoving(false);
		
	}
}
