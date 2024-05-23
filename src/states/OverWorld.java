package states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Camera;
import engine.Sprite;
import entity.Entity;
import entity.Monk2;
import entity.Player;
import entity.Skeleton;
import entity.Skellington;
import main.*;

public class OverWorld extends InputHandler implements GameState
{
				GameStateManager  stateManager;
	public static  BulletManager bulletManager;
	
	Camera camera;

	GameState dialogue, battle, pause;
	
	Image background, backgroundScaled;
	
	public static Entity	  enemy; //for battle reference
	
	public static Player      player;
	public static Monk2       monk2;
	public static Skellington skellington;
	public static Skeleton[]  skeletons;
	
	//  for testing  //
	public static Timer pressCooldown;
	int cooldownTime = 400;
	boolean inDialogueState;
	//	-----------  //
	
	@Override
	public void init() 
	{
		stateManager  = Game.stateManager;
		dialogue	  = Game.dialogue;
		battle        = Game.battle;
		pause         = Game.pause;
		
		//instantiate entities
		player        = new   Player   (400, 600);
		monk2         = new    Monk2   (200, 300);
		skellington   = new Skellington(220, 350);
		skeletons     = new Skeleton[]
		{
			new Skeleton   (130, 240),
			new Skeleton   (265, 240),
			new Skeleton   (130, 300),
			new Skeleton   (265, 300)
		};
		
		bulletManager = new BulletManager();
			
		background    = Toolkit.getDefaultToolkit().getImage("res/map01.png");
		
		pressCooldown = new Timer(cooldownTime, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{pressCooldown.stop();}
		});
		
		Game.soundManager.loadMusic("Fight.wav");
		Game.soundManager.playMusic();
		
		Camera.setPosition((int)player.sprite.x + (int)(player.sprite.w / 2) - (int)(Game.SCREEN_WIDTH  / 2),
						   (int)player.sprite.y + (int)(player.sprite.h / 2) - (int)(Game.SCREEN_HEIGHT / 2));
	}
	
	@Override
	public void update() 
	{
		enemy           = null;
		inDialogueState = false;
		
		if (pressing[_P]) stateManager.pushState(pause);
		 
	    player.update();
	    player.handleCollisions();
	 	     
	    if (skellington != null) 
	    {
	    	skellington.update			();
	    	skellington.handleCollisions();
	    	handleDialogueFor(skellington);
	    }
	    
	    if (monk2 != null) 
	    {
	    	monk2.update		  ();	
	    	handleDialogueFor(monk2);
	    }	
	    
	    for (Skeleton s : skeletons) if (s != null) 
	    {
	    	s.update();
		    s.handleCollisions(); 	
	    }
	    
	    bulletManager.updateBullets();
	    updateCamera();
	  }

	@Override
	public void render(Graphics pen) 
	{
        pen.drawImage(background, - (400 * Game.SCALE) - ((int)Camera.x), - (400 * Game.SCALE) - ((int)Camera.y), 800 * Game.SCALE, 800 * Game.SCALE, null);
        
        bulletManager.drawBullets(pen);
          
        //DRAW ENTITIES
        for (Skeleton s : skeletons) if (s != null) s.draw(pen);
        
        if (monk2 		!= null) 	   monk2.draw(pen);
        if (skellington != null) skellington.draw(pen);
        player.draw(pen);
        //------------
        
        //DRAW HEALTH
        for(Skeleton s : skeletons) if (s != null && s.damaged) s.drawHealthBar(pen);
        
        if (skellington != null && skellington.damaged) skellington.drawHealthBar(pen);
        
        if (!inDialogueState) player.drawHealth(pen);
        //-----------
	}
	
	public void updateCamera()
	{
		double targetX = player.sprite.x + (player.sprite.w / 2) - (Game.SCREEN_WIDTH  / 2);
	    double targetY = player.sprite.y + (player.sprite.h / 2) - (Game.SCREEN_HEIGHT / 2);

	    Camera.setPosition(targetX, targetY);
	    //gradually move the camera towards the target position:
	    //Camera.setPosition(Camera.x + (targetX - Camera.x) * 0.1, Camera.y + (targetY - Camera.y) * 0.1);
	}
	
	public void handleDialogueFor(Entity npc)
	{
		if (player.sprite.isInTalkingRangeOf(npc.sprite) && player.sprite.isFacing(npc.sprite) && pressing[ENTER] && !pressCooldown.isRunning())
 	    {
 	    	npc.sprite.face(player.sprite);
 	    	enemy = npc;
 	    	stateManager.pushState(dialogue);
 	    	inDialogueState = true;
 	    }
	}
}
