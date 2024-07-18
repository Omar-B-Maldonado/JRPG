package states;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Animation;
import entity.Entity;
import entity.Monk2;
import entity.Player;
import main.*;
import ui.BattleUI;

public class Battle extends InputHandler implements GameState 
{		
	private final int MONK2_ATTACK_SPRITE_WIDTH = 68  * Game.SCALE;
	private final int MONK2_ATTACK_SPRITE_HEIGHT= 98  * Game.SCALE;
	
	private final int NIMA_ATTACK_SPRITE_WIDTH  = 123 * Game.SCALE;
	private final int NIMA_ATTACK_SPRITE_HEIGHT = 157 * Game.SCALE;
	
	GameStateManager stateManager;
	GameState 		 previousState;
	BattleUI		 UI;
	
	Image bg         = null;
	Image enemyImage = null;
	Entity enemy;
	
	//-----------------------------------------------------------------
	Image bgBridgeDusk;				Image bgBridgeDuskScaled;
	Image bgPlainsDusk;				Image bgPlainsDuskScaled;
	
	Image NimaAttack;				Image NimaAttackScaled;	
	Image Monk2Attack;				Image Monk2AttackScaled;
	
	//-----------------------------------------------------------------
	Image textBox;					Image textBoxScaled;
	Image healthBox;				Image healthBoxScaled;
	Image dialogueBox;				Image dialogueBoxScaled;
	
	Animation double_slash;
	Animation shield;
	Animation currentAnimation;
	public static boolean animationFinished;
	
	Timer enemyAttTimer;
	
	public Battle() 
	{
		loadResources();
		UI = new BattleUI();
	}
	
	public void init() 
	{
		stateManager	  = Game.stateManager;
		
		enemy = OverWorld.getInteractor();
		if (enemy instanceof Monk2){
			bg    = bgPlainsDuskScaled;
			enemyImage    =  Monk2AttackScaled;
			enemyAttTimer = OverWorld.monk2.attTimer; //FIX THIS
		}
		
		Game.soundManager.loadMusic("Tension.wav");
		Game.soundManager.playMusic();
		
		double_slash 	  = new Animation("res/battle attacks/", "double_slash", 4, 4);
		shield       	  = new Animation("res/battle attacks/", "shield"      , 6, 3);
		currentAnimation  = null;
		animationFinished = true; //no animation is playing at the start
		
		if (enemyAttTimer != null) enemyAttTimer.start();
		
		UI.init();
	}

	public void update() 
	{		    
		   if (battleWon ()) handleBattleWon ();
      else if (battleLost()) handleBattleLost();
      else
      {
    	  UI.update();
    	  
	      if (UI.choice.equals("attack") && animationFinished) 
	      {
	    	  currentAnimation  = double_slash;
	    	  animationFinished = false;
	    	  
	    	  playHitSound();
	    	  OverWorld.monk2.hitFor(1);
	    	  
	    	  //System.out.println(OverWorld.monk2.health); 	  
	      }
	      
	      if (UI.choice.equals("defend") && animationFinished)
	      {
	    	  currentAnimation  = shield;
	    	  animationFinished = false;
	    	  
	    	  playBlockSound();
	      }
	      
	      if (enemyAttTimer != null && !enemyAttTimer.isRunning()) enemyAttTimer.restart();
      }
	}
	
	public void render(Graphics2D pen)
	{
		pen.drawImage(bg,  0, 0, null);
		
		pen.drawImage(enemyImage, 162 * Game.SCALE, 48 * Game.SCALE, null);
		
		if (currentAnimation != null && !animationFinished && currentAnimation == shield) 
		{	
			pen.drawImage(currentAnimation.getCurrentImage(), 85 * Game.SCALE, 45 * Game.SCALE, 285, 285, null);
			if (currentAnimation.nextFrameIsFirstFrame()) 
			{
				animationFinished = true;
				UI.setChoice("");
			}	
		}
		
		pen.drawImage(NimaAttackScaled, 15 * Game.SCALE, 35 * Game.SCALE, null);

		if (currentAnimation != null && !animationFinished && currentAnimation == double_slash) 
		{
			pen.drawImage(currentAnimation.getCurrentImage(), 175 * Game.SCALE, 55 * Game.SCALE, 160, 160, null);
			if (currentAnimation.nextFrameIsFirstFrame()) 
			{
				animationFinished = true;
				UI.setChoice("");
			}
		}
		
		UI.render(pen);

		OverWorld.player.drawHealth(pen); //MAYBE GIVE PLAYER A SEPARATE HEALTH FOR BATTLES??
		
		OverWorld.monk2.healthBar.draw(pen);
		OverWorld.monk2.drawAttackTimer(pen);	
	}
	
	public void playHitSound()
	{
		Game.soundManager.setSound("Slash.wav");
		Game.soundManager.play();
	}
	
	public void playBlockSound()
	{
		Game.soundManager.setSound("Alert2.wav");
		Game.soundManager.play();
	}
	
	public boolean battleWon(){
		return enemy.currentHealth <= 0;
	}
	
	public boolean battleLost(){
		return OverWorld.player.currentHealth <= 0;
	}
	
	public void handleBattleWon()
	{
	  OverWorld.entities.remove(enemy);
  	  OverWorld.pressCooldown.start();
  	  
  	  Game.soundManager.stopMusic();
  	  Game.soundManager.loadMusic("Fight.wav");
  	  Game.soundManager.playMusic();
  	  
  	  if (enemyAttTimer != null && enemyAttTimer.isRunning()) enemyAttTimer.stop();
  	  
  	  stateManager.popState();//gets rid of battle
  	  stateManager.popState();//gets rid of underlying dialogue
	}
	
	public void handleBattleLost()
	{
		if (enemyAttTimer != null && enemyAttTimer.isRunning()) enemyAttTimer.stop();
		
		stateManager.popState();
		stateManager.popState();
		stateManager.pushState(Game.over);
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
