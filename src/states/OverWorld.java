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
	
	Dialogue dialogue;
	Battle   battle;
	Camera   camera;
	Pause    pause;
	
	
	Image background; Image backgroundScaled;
	
	public static Entity enemy; //for battle reference
	
	public static Player      player;
	public static Monk2       monk2;
	public static Skeleton[]  skeleton = new Skeleton[4];
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
		dialogue	  = Game.dialogue;
		battle        = Game.battle;
		pause         = Game.pause;
		
		//instantiate entities
		player        = new   Player   (400, 600);
		monk2         = new    Monk2   (200, 300);
		skeleton[0]   = new Skeleton   (130, 240);
		skeleton[1]   = new Skeleton   (265, 240);
		skeleton[2]   = new Skeleton   (130, 300);
		skeleton[3]   = new Skeleton   (265, 300);
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
	    
	    for (int i = 0; i < skeleton.length; i++)
	    {
	    	if (skeleton[i]!= null) 
	    	{
	    		skeleton[i].update();
		    	skeleton[i].handleCollisions();
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
		double targetX = (int) player.sprite.x + (int) (player.sprite.w / 2) - (int) (Game.SCREEN_WIDTH / 2);
	    double targetY = (int) player.sprite.y + (int) (player.sprite.h / 2) - (int) (Game.SCREEN_HEIGHT / 2);

	    //gradually move the camera towards the target position
	    Camera.setPosition(Camera.x + (targetX - Camera.x) * 0.1, Camera.y + (targetY - Camera.y) * 0.1);
	    
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
