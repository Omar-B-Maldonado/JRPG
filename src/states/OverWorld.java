package states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.Timer;

import engine.*;
import entity.*;
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
	
	public static Wall[] walls;
	
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
		
		createWalls();
		
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
		
		updateCamera();
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
	public void render(Graphics2D pen) 
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
        for(Skeleton s : skeletons) if (s != null && s.damaged) s.healthBar.draw(pen);
        
        if (skellington != null && skellington.damaged) skellington.healthBar.draw(pen);
        
        if (!inDialogueState) player.drawHealth(pen);
        //-----------
        
        //for (Wall wall : walls) wall.draw(pen);
	}
	
	public void updateCamera()
	{
		double targetX = player.sprite.x + (player.sprite.halfW) - (Game.SCREEN_HALF_WIDTH);
	    double targetY = player.sprite.y + (player.sprite.halfH) - (Game.SCREEN_HALF_HEIGHT);

	    Camera.setPosition(targetX, targetY);
	    //gradually move the camera towards the target position:
	    //Camera.setPosition(Camera.x + (targetX - Camera.x) * 0.1, Camera.y + (targetY - Camera.y) * 0.1);
	}
	
	public void handleDialogueFor(Entity npc)
	{
		if (player.isInTalkingRangeOf(npc) && player.isFacing(npc) && pressing[ENTER] && !pressCooldown.isRunning())
 	    {
 	    	npc.sprite.face(player.sprite);
 	    	enemy = npc;
 	    	stateManager.pushState(dialogue);
 	    	inDialogueState = true;
 	    }
	}
	
	public void createWalls()
	{
		walls   = new Wall[]
		{
				new Wall(1072, 377, 100, 100),
				new Wall(1072, 372, 100, 100),
				new Wall(1072, 367, 100, 100),
				new Wall(1072, 357, 100, 100),
				new Wall(1072, 347, 100, 100),
					
				new Wall(278, -1082, 91, 47),
				new Wall(1064, 333, 83, 111),
				new Wall(337, -1047, 54, 64),
				new Wall(616, -990, 61, 93),
				new Wall(374, -990, 243, 50),
				new Wall(-686, -1103, 35, 94),
				new Wall(-767, -1035, 100, 46),
				new Wall(-450, -1159, 36, 106),
				new Wall(-651, -1074, 220, 39),
				new Wall(666, -792, 277, 47),
				new Wall(651, -896, 69, 129),
				new Wall(835, -752, 66, 105),
				new Wall(884, -676, 89, 120),
				new Wall(931, -561, 101, 153),
				new Wall(977, -415, 88, 55),
				new Wall(592, 207, 52, 48),
				new Wall(640, 157, 62, 82),
				new Wall(687, 109, 61, 59),
				new Wall(733, 61, 165, 83),
				new Wall(897, 108, 243, 40),
				new Wall(1120, -22, 45, 139),
				new Wall(1070, -165, 91, 150),
				new Wall(1026, -362, 81, 200),
				new Wall(-435, -1129, 740, 44),
				new Wall(-311, 1023, 105, 84),
				new Wall(-466, 926, 114, 58),
				new Wall(-512, 969, 259, 63),
				new Wall(-696, 1019, 210, 36),
				new Wall(-967, 972, 279, 69),
				new Wall(-861, 720, 41, 238),
				new Wall(-205, 1069, 218, 47),
				new Wall(-35, 1021, 239, 52),
				new Wall(201, 971, 174, 81),
				new Wall(323, 1017, 95, 63),
				new Wall(364, 1066, 108, 65),
				new Wall(467, 1110, 340, 46),
				new Wall(781, 1064, 97, 67),
				new Wall(488, 430, 88, 78),
				new Wall(829, 1021, 95, 50),
				new Wall(877, 973, 95, 61),
				new Wall(925, 923, 121, 56),
				new Wall(975, 599, 70, 323),
				new Wall(636, 549, 356, 50),
				new Wall(542, 509, 345, 39),
				new Wall(-814, -49, 41, 768),
				new Wall(-816, -1008, 46, 910),
				new Wall(529, 242, 383, 24),
				new Wall(916, 242, 45, 192),
				new Wall(528, 421, 409, 47),
		};
	}
}
