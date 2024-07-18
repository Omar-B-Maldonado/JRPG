package states;

import java.awt.Graphics;
import java.util.Iterator;

import engine.Sprite;
import entity.Entity;
import entity.Monk2;
import entity.Player;
import entity.Skeleton;
import main.Game;

public class BulletManager 
{
	public Sprite[] playerBullets = new Sprite[ 20];
	public Sprite[] skeletBullets = new Sprite[100];
		
	int p  =  0;
	int s  =  0;
	
	Player     player;
	Skeleton[] skelet;
	
	public BulletManager()
	{
		player   = OverWorld.player;
		skelet   = OverWorld.skeletons;
	}
	
	public void createPlayerBullet(Sprite q, int vx, int vy, String sound)
	{
		if (p >= playerBullets.length) p = 0;
		
		playerBullets[p] = q; //give the bullet the specified sprite
		
		playerBullets[p].setVelocity(vx, vy);
        playerBullets[p].setMoving  ( true );
        
        Game.soundManager.playSound(sound);
		p++;
	}
	
	public void createSkeletBullet(Sprite q, int vx, int vy, String sound)
	{
		if (s >= skeletBullets.length) s = 0;
		
		skeletBullets[s] = q; //give the bullet the specified sprite
		
		skeletBullets[s].setVelocity(vx, vy);
        skeletBullets[s].setMoving  ( true );
        
        Game.soundManager.playSound(sound);
		s++;
	}
	
	public void updateBullets()
	{
		for (int i = 0; i < playerBullets.length; i++) if (playerBullets[i] != null){
			playerBullets[i].move();
			handleEntityHitByPlayerBullet(playerBullets[i], i);
		}
		for (int k = 0; k < skeletBullets.length; k++) if (skeletBullets[k] != null) 
		{
			skeletBullets[k].move();
			handlePlayerHitBySkeletBullet(skeletBullets[k], k);
			//handleMonkHitBySkeletBullet(k);
		}
	}
	
	public void drawBullets(Graphics pen)
	{
		for (int k = 0; k < playerBullets.length; k++) if (playerBullets[k] != null) playerBullets[k].draw(pen);
		for (int k = 0; k < skeletBullets.length; k++) if (skeletBullets[k] != null) skeletBullets[k].draw(pen);
	}
	
	public void handleEntityHitByPlayerBullet(Sprite playerBullet, int i) {
		Iterator<Entity> iterator = OverWorld.entities.iterator();
		while (iterator.hasNext()){
			
			Entity e = iterator.next();
			if (e instanceof Player) continue;
			if (e instanceof Monk2 && playerBullet.overlaps(e.sprite)) {
				Game.soundManager.playSound("Fx");
				playerBullets[i] = null;
				return;
			}
			if (playerBullet.overlaps(e.sprite)) {
				playerBullet.knockBack(e.sprite, 25);
				e.move();
				Game.soundManager.playSound("Kill");
    			playerBullets[i] = null;
    			e.hitFor(2);
    			if (e.isDead()) OverWorld.entities.remove(e);
    			return;
			}
		}
	}
	
	public void handlePlayerHitBySkeletBullet(Sprite skeletBullet, int k) 
	{
		if (skeletBullet.overlaps(player.sprite)){
			Game.soundManager.playSound("Hit");
			skeletBullets[k] = null;
			player.currentHealth--;
			
			if (player.isDead()) Game.stateManager.pushState(Game.over);
		}
	}
	
	public void pauseBullets()
	{
		for (int k = 0; k < playerBullets.length; k++) if (playerBullets[k] != null) playerBullets[k].setMoving(false);
		for (int k = 0; k < skeletBullets.length; k++) if (skeletBullets[k] != null) skeletBullets[k].setMoving(false);
	}
}
