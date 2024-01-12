package states;

import java.awt.Graphics;

import engine.Sprite;
import entity.Player;
import entity.Skeleton;
import main.Game;

public class BulletManager 
{
	public Sprite[] playerBullet = new Sprite[ 20];
	public Sprite[] skeletBullet = new Sprite[100];
		
	int p  =  0;
	int s  =  0;
	
	Player     player;
	Skeleton[] skelet;
	
	public BulletManager()
	{
		player   = OverWorld.player;
		skelet   = OverWorld.skeleton;
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
				playerBullet[k].knockBack(skelet[i].sprite, 25);
    			
    			skelet[i].move();	
    			
    			Game.soundManager.setSound("Kill.wav");
    			Game.soundManager.play();
    			
    			//skeles collide w/ player
		    	if (skelet[i].sprite.overlaps(player.sprite)) skelet[i].sprite.pushOutOf(player.sprite);
		    	
		    	//skeletons collide with each other
		    	for (int j = 0; j < skelet.length; j++) 
	            {if (i != j && skelet[j]!= null && skelet[i].sprite.overlaps(skelet[j].sprite)) skelet[i].sprite.pushOutOf(skelet[j].sprite);}
		    	
		    	//skeles collide w/ monk
		    	if (OverWorld.monk2 != null && skelet[i].sprite.overlaps(OverWorld.monk2.sprite)) skelet[i].sprite.pushOutOf(OverWorld.monk2.sprite);
	            	
		    	//skeles collide w/ skellington
		    	if (OverWorld.skellington != null && skelet[i].sprite.overlaps(OverWorld.skellington.sprite)) skelet[i].sprite.pushOutOf(OverWorld.skellington.sprite);
    			
    			playerBullet[k] = null;
    				skelet[i].hitFor(2);
    			if (skelet[i].health < 1) skelet[i] = null;
			}
		}
	}
	
	public void handleSkellingtonHitByPlayerBullet(int k) 
	{
		if (OverWorld.skellington != null && playerBullet[k] != null && playerBullet[k].overlaps(OverWorld.skellington.sprite))
			{
				playerBullet[k].knockBack(OverWorld.skellington.sprite, 25);
    			
				OverWorld.skellington.move();
    			
    			Game.soundManager.setSound("Kill.wav");
    			Game.soundManager.play();
    			
    			//skellington collides with monk2
    	    	if (OverWorld.monk2 != null && OverWorld.skellington.sprite.overlaps( OverWorld.monk2.sprite)) OverWorld.skellington.sprite.pushOutOf(OverWorld.monk2.sprite);
    	 	    
    	    	//skellington collides with player
    	    	if				    (OverWorld.skellington.sprite.overlaps(player.sprite)) OverWorld.skellington.sprite.pushOutOf(player.sprite);
    	    	
    	    	//skellington collides with skeletons
    		    for (int i = 0; i < skelet.length; i++) 
    		    {
    		    	if (skelet[i] != null && OverWorld.skellington.sprite.overlaps(skelet[i].sprite))
    		    	{
    		    		OverWorld.skellington.sprite.pushOutOf(skelet[i].sprite);
    		    	}
    		    }
    			
    			playerBullet[k] = null;
    			OverWorld.skellington.hitFor(1);
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
			
			if (player.health <= 0) Game.stateManager.pushState(Game.over);
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
