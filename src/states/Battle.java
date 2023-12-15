package states;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Animation;
import entity.Player;
import main.*;

public class Battle extends InputHandler implements GameState 
{		
	private final int MONK2_ATTACK_SPRITE_WIDTH = 68  * Game.SCALE;
	private final int MONK2_ATTACK_SPRITE_HEIGHT= 98  * Game.SCALE;
	
	private final int NIMA_ATTACK_SPRITE_WIDTH  = 123 * Game.SCALE;
	private final int NIMA_ATTACK_SPRITE_HEIGHT = 157 * Game.SCALE;
	
	GameStateManager stateManager;
	GameState 		 previousState;
	BattleUI		 UI;
	
	Image bg    = null;
	Image enemy = null;
	
	//-----------------------------------------------------------------
	Image bgBridgeDusk;				Image bgBridgeDuskScaled;
	Image bgPlainsDusk;				Image bgPlainsDuskScaled;
	
	Image NimaAttack;				Image NimaAttackScaled;	
	Image Monk2Attack;				Image Monk2AttackScaled;
	
	//-----------------------------------------------------------------
	Image textBox;					Image textBoxScaled;
	Image healthBox;				Image healthBoxScaled;
	Image dialogueBox;				Image dialogueBoxScaled;
	
	
	
	static Animation double_slash = new Animation("res/battle attacks/", "double_slash", 4, 5);
	static Animation shield       = new Animation("res/battle attacks/", "shield"      , 6, 3);
	
	public static Animation currentAnimation;
	
	//testing
	boolean   hitSoundPlayed = false;
	boolean blockSoundPlayed = false;
	
	public Battle() 
	{
		loadResources();
		UI = new BattleUI();
	}
	
	@Override
	public void init() 
	{
		stateManager	  = Game.stateManager;
		
		if (OverWorld.enemy == OverWorld.monk2) 
		{
			bg    = bgPlainsDuskScaled;
			enemy =  Monk2AttackScaled;
		}
		
		Game.soundManager.loadMusic("Tension.wav");
		Game.soundManager.playMusic();
		
		UI.init();
	}

	@Override
	public void update() 
	{		    
      if (battleWon())
      {
    	  OverWorld.monk2 = null;
    	  OverWorld.pressCooldown.start();
    	  
    	  Game.soundManager.stopMusic();
    	  Game.soundManager.loadMusic("Fight.wav");
    	  Game.soundManager.playMusic();
    	  
    	  stateManager.popState();
    	  stateManager.popState();
      }
      
      UI.update();
      
      if (UI.choice.equals("attack")) 
      {
    	  currentAnimation = double_slash;
    	  
    	  if (!hitSoundPlayed) 
    	  {
    		  playHitSound(); 
	    	  OverWorld.monk2.hitFor(1);
	    	  System.out.println(OverWorld.monk2.health);
    	  }
      }
      
      if (UI.choice.equals("defend"))
      {
    	  currentAnimation = shield;
    	  
    	  if (!blockSoundPlayed)
    	  {
    		  playBlockSound(); 
    	  } 
      }
      
      if (UI.choice.equals(      "")) 
      {
    	  currentAnimation = null;
    	    hitSoundPlayed = false;
    	  blockSoundPlayed = false;
      }
      
      
	}
	
	public void playHitSound()
	{
		Game.soundManager.setSound("Slash.wav");
		Game.soundManager.play();
		hitSoundPlayed = true;
	}
	
	public void playBlockSound()
	{
		Game.soundManager.setSound("Alert2.wav");
		Game.soundManager.play();
		blockSoundPlayed = true;
	}
	
	public boolean battleWon()
	{
		return OverWorld.monk2.health <= 0;
	}

	@Override
	public void render(Graphics pen)
	{
		pen.drawImage(bg,  0, 0, null);
		
		pen.drawImage(enemy, 162 * Game.SCALE, 48 * Game.SCALE, null);
		
		if (currentAnimation != null && currentAnimation == shield) 
		{
			pen.drawImage(currentAnimation.getCurrentImage(), 85 * Game.SCALE, 45 * Game.SCALE, 285, 285, null);
		}
		
		pen.drawImage(NimaAttackScaled, 15 * Game.SCALE, 35 * Game.SCALE, null);
		
		UI.render(pen);
		
		if (currentAnimation != null) 
		{
			if (currentAnimation == double_slash) pen.drawImage(currentAnimation.getCurrentImage(), 175 * Game.SCALE, 55 * Game.SCALE, 160, 160, null);
		}
	}
	
	public void loadResources()
	{
		//get each image, then scale each image
		bgBridgeDusk       = Toolkit.getDefaultToolkit().getImage("res/battle backgrounds/bridge_bg_0.png");	
		bgBridgeDuskScaled = bgBridgeDusk.getScaledInstance(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, Image.SCALE_FAST);	
		
		bgPlainsDusk       = Toolkit.getDefaultToolkit().getImage("res/battle backgrounds/plains_dusk.png");	
		bgPlainsDuskScaled = bgPlainsDusk.getScaledInstance(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, Image.SCALE_FAST);	
		
		Monk2Attack        = Toolkit.getDefaultToolkit().getImage("res/battle sprites/monk2_battle_attack.png");
		Monk2AttackScaled  = Monk2Attack.getScaledInstance(MONK2_ATTACK_SPRITE_WIDTH, MONK2_ATTACK_SPRITE_HEIGHT, Image.SCALE_FAST);
		
		NimaAttack         = Toolkit.getDefaultToolkit().getImage("res/battle sprites/nima_battle_attack.png");
		NimaAttackScaled   = NimaAttack.getScaledInstance(NIMA_ATTACK_SPRITE_WIDTH, NIMA_ATTACK_SPRITE_HEIGHT, Image.SCALE_FAST);
	}
}
