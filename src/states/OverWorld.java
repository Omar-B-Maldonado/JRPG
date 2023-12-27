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
	GameStateManager stateManager;
	public static BulletManager 	bulletManager;
	Battle battle;
	Pause   pause;
	Camera camera;
	
	//instantiate in Game class
	Dialogue dialogue = new Dialogue();
	
	Image background; Image backgroundScaled;
	
	public static Entity enemy;
	
	
	
	public static Player   player;
	public static Monk2    monk2;
	public static Skeleton[] skeleton = new Skeleton[4];
	public static Skellington skellington;
	
	//  for testing  //
	public static Timer pressCooldown;
	int cooldownTime = 400;
	boolean inDialogueState;
	//	-----------  //
	
	public OverWorld() {}
	
	@Override
	public void init() 
	{
		stateManager  = Game.stateManager;
		battle        = Game.battle;
		pause         = Game.pause;
		
		//instantiate entities
		player        = new   Player(400, 600);
		monk2         = new    Monk2(200, 300);
		
		skeleton[0]   = new Skeleton(130, 240);
		skeleton[1]   = new Skeleton(265, 240);
		skeleton[2]   = new Skeleton(130, 300);
		skeleton[3]   = new Skeleton(265, 300);
		
		skellington   = new Skellington(220, 350);
		
		bulletManager = new BulletManager();
			
		background    = Toolkit.getDefaultToolkit().getImage("res/map01.png");
		
		pressCooldown = new Timer(cooldownTime, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{pressCooldown.stop();}
		});
		
		Game.soundManager.loadMusic("Fight.wav");
		Game.soundManager.playMusic();
		
		Camera.setPosition((int)player.sprite.x + (int)(player.sprite.w / 2) - (int)(Game.SCREEN_WIDTH / 2), (int)player.sprite.y + (int)(player.sprite.h / 2) - (int)(Game.SCREEN_HEIGHT / 2));
	}
	
	@Override
	public void update() 
	{
		enemy           = null;
		inDialogueState = false;
		
		if (pressing[_P]) stateManager.pushState (pause);
		  
	    //--------------------------------PLAYER MOVEMENT AND COLLISION---------//
	    player.update();
	    
	    //player collides with skeletons
	    for (int i = 0; i < skeleton.length; i++) 
	    {
	    	if (skeleton[i] != null && player.sprite.overlaps(skeleton[i].sprite))
	    	{
	    		player.sprite.pushOutOf(skeleton[i].sprite);
	    	}
	    }
	    //player collides with skellington
	    if (skellington != null && player.sprite.overlaps(skellington.sprite)) player.sprite.pushOutOf(skellington.sprite);
	    
	    //player collides with monk2
	    if (monk2       != null && player.sprite.overlaps(      monk2.sprite)) player.sprite.pushOutOf(      monk2.sprite);
	    //---------------------------------------------------------------------//
	 	  
	    
	    //--------------------------SKELLINGTON MOVEMENT AND COLLISION---------//
	    if (skellington != null) 
	    {
	    	skellington.update();
	    	
	    	//skellington collides with monk2
	    	if (monk2 != null && skellington.sprite.overlaps( monk2.sprite)) skellington.sprite.pushOutOf(monk2.sprite);
	 	    
	    	//skellington collides with player
	    	if				    (skellington.sprite.overlaps(player.sprite)) skellington.sprite.pushOutOf(player.sprite);
	    	
	    	//skellington collides with skeletons
		    for (int i = 0; i < skeleton.length; i++) 
		    {
		    	if (skeleton[i] != null && skellington.sprite.overlaps(skeleton[i].sprite))
		    	{
		    		skellington.sprite.pushOutOf(skeleton[i].sprite);
		    	}
		    }
	    	//handle player talks to skellington
	 	    if (player.sprite.isInTalkingRangeOf(skellington.sprite) && player.sprite.isFacing(skellington.sprite) && pressing[ENTER] && !pressCooldown.isRunning())
	 	    {
	 	    	skellington.sprite.face(player.sprite);
	 	    	enemy = skellington;
	 	    	stateManager.pushState(dialogue);
	 	    	inDialogueState = true;
	 	    }
	    }
	    //---------------------------------------------------------------//
	    
	    //---------------------------- MONK2 -------------------------// 
	    if (monk2 != null) 
	    {
	    	monk2.update();
	    	
	    	//handle player talks to monk2
	 	    if (player.sprite.isInTalkingRangeOf(monk2.sprite) && player.sprite.isFacing(monk2.sprite) && pressing[ENTER] && !pressCooldown.isRunning())
	 	    {
	 	    	monk2.sprite.face(player.sprite);
	 	    	enemy = monk2;
	 	    	stateManager.pushState(dialogue);
	 	    	inDialogueState = true;
	 	    }
	    }	
	    //-----------------------------------------------------------//
	    
	    //---------------------SKELETON MOVEMENT AND COLLISION-----------------------//
	    for (int i = 0; i < skeleton.length; i++)
	    {
	    	if (skeleton[i]!= null) 
	    	{
	    		skeleton[i].update();
	    	
		    	//skeles collide w/ player
		    	if (skeleton[i].sprite.overlaps(player.sprite)) skeleton[i].sprite.pushOutOf(player.sprite);
		    	
		    	//skeletons collide with each other
		    	for (int j = 0; j < skeleton.length; j++) 
	            {
	                if (i != j && skeleton[j]!= null && skeleton[i].sprite.overlaps(skeleton[j].sprite)) 
	                {
	                    skeleton[i].sprite.pushOutOf(skeleton[j].sprite);
	                }
	            }
		    	//skeles collide w/ monk
		    	if (monk2 != null && skeleton[i].sprite.overlaps(monk2.sprite)) skeleton[i].sprite.pushOutOf(monk2.sprite);
	            	
		    	//skeles collide w/ skellington
		    	if (skellington != null && skeleton[i].sprite.overlaps(skellington.sprite)) skeleton[i].sprite.pushOutOf(skellington.sprite);
	    	}
	    }
	    bulletManager.updateBullets();
	    updateCamera();
	  }

	@Override
	public void render(Graphics pen) 
	{
        pen.drawImage(background, - (400 * Game.SCALE) - ((int)Camera.x), - (400 * Game.SCALE) - ((int)Camera.y), 800 * Game.SCALE, 800 * Game.SCALE, null);
        
        bulletManager.drawBullets(pen);
        
        //draw skeletons
        for (int i = 0; i < skeleton.length; i++) if (skeleton[i] != null) skeleton[i].draw(pen);
        
        if (monk2 		!= null) 	   monk2.draw(pen);
        if (skellington != null) skellington.draw(pen);
        player.draw(pen);
        
        //draw skeleton health bars
        for (int i = 0; i < skeleton.length; i++)
        {
        	if (skeleton[i] != null && skeleton[i].damaged) skeleton[i].drawHealthBar(pen);
        }
        
        //draw skellington health bar
        if 	   (skellington != null && skellington.damaged) skellington.drawHealthBar(pen);
        
        if (!inDialogueState) //DRAW UI
        {
        	player.drawHeartContainers(pen);
            player.drawHearts		  (pen);
        }
	}
	
	public void updateCamera()
	{
		//Camera.setPosition((int)player.sprite.x + (int)(player.sprite.w / 2) - (int)(Game.SCREEN_WIDTH / 2), (int)player.sprite.y + (int)(player.sprite.h / 2) - (int)(Game.SCREEN_HEIGHT / 2));
		
		double targetX = (int) player.sprite.x + (int) (player.sprite.w / 2) - (int) (Game.SCREEN_WIDTH / 2);
	    double targetY = (int) player.sprite.y + (int) (player.sprite.h / 2) - (int) (Game.SCREEN_HEIGHT / 2);

	    //gradually move the camera towards the target position
	    Camera.setPosition(Camera.x + (targetX - Camera.x) * 0.1, Camera.y + (targetY - Camera.y) * 0.1);
	    
	}
}
