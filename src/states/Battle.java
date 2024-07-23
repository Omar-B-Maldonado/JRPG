package states;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import engine.Animation;
import entity.Entity;
import entity.Monk2;
import entity.Player;
import main.*;
import ui.BattleUI;
// TODO: fix battle sprites to allow different Nima poses
public class Battle extends InputHandler implements GameState 
{		
	GameStateManager stateManager;
	GameState 		 previousState;
	BattleUI		 UI;
	
	static Image currentPose,
	bg         = null,
	enemyImage = null;
	
	Entity enemy;
	
	//-----------------------------------------------------------------
	static Image 
	bgBridgeDusk, bgPlainsDusk, //backgrounds
	
	NimaDefend, NimaAttack, 	//Nima's poses
	
	Monk2Attack;
	
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
			bg    = bgPlainsDusk;
			enemyImage    =  Monk2Attack;
			enemyAttTimer = OverWorld.monk2.attTimer; //FIX THIS
		}
		
		Game.soundManager.loadMusic("Tension.wav");
		Game.soundManager.playMusic();
		
		double_slash 	  = new Animation("res/battle attacks/", "double_slash", 4, 4);
		shield       	  = new Animation("res/battle attacks/", "shield"      , 6, 3);
		currentAnimation  = null;
		animationFinished = true; //no animation is playing at the start
		
		currentPose = NimaAttack;
		
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
	    	  Game.soundManager.playSound("Slash");
	    	  OverWorld.monk2.hitFor(1);
	    	  
	    	  //System.out.println(OverWorld.monk2.health); 	  
	      }
	      
	      if (UI.choice.equals("defend") && animationFinished)
	      {
	    	  currentAnimation  = shield;
	    	  animationFinished = false;
	    	  Game.soundManager.playSound("Alert2");
	      }
	      
	      if (enemyAttTimer != null && !enemyAttTimer.isRunning()) enemyAttTimer.restart();
      }
	}
	
	public void render(Graphics2D pen) {
		pen.drawImage(bg,  0, 0, null);
		
		pen.drawImage(enemyImage, 0, 0, null);
		
		if (currentAnimation != null && !animationFinished && currentAnimation == shield) 
		{	
			pen.drawImage(currentAnimation.getCurrentImage(), 85 * Game.SCALE, 45 * Game.SCALE, 285, 285, null);
			if (currentAnimation.nextFrameIsFirstFrame()) 
			{
				animationFinished = true;
				UI.setChoice("");
			}	
		}
		
		pen.drawImage(currentPose, 0, 0, null);

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
	
	public boolean battleWon(){
		return enemy.currentHealth <= 0;
	}
	
	public boolean battleLost(){
		return OverWorld.player.currentHealth <= 0;
	}
	
	public void handleBattleWon() {
	  OverWorld.entities.remove(enemy);
  	  OverWorld.pressCooldown.start();
  	  
  	  Game.soundManager.stopMusic();
  	  Game.soundManager.loadMusic("Fight.wav");
  	  Game.soundManager.playMusic();
  	  
  	  if (enemyAttTimer != null && enemyAttTimer.isRunning()) enemyAttTimer.stop();
  	  
  	  stateManager.popState();//gets rid of battle
  	  stateManager.popState();//gets rid of underlying dialogue
	}
	
	public void handleBattleLost() {
		if (enemyAttTimer != null && enemyAttTimer.isRunning()) enemyAttTimer.stop();
		
		stateManager.popState();
		stateManager.popState();
		stateManager.pushState(Game.over);
	}
	
	public static void setNimaPose(String pose) {
		if      (pose == "attack") currentPose = NimaAttack;
		else if (pose == "defend") currentPose = NimaDefend;
	}

	public void loadResources() {
		bgBridgeDusk       = loadAndScale("battle backgrounds", "bridge_bg_0.png");	
		bgPlainsDusk       = loadAndScale("battle backgrounds", "plains_dusk.png");	
		
		Monk2Attack        = loadAndScale("battle sprites", "monk2_attack.png");
		
		NimaAttack         = loadAndScale("battle sprites", "nima_attack.png");
		NimaDefend 		   = loadAndScale("battle sprites", "nima_defend.png");
	}
	
	public Image loadAndScale(String folder, String filename) {
		URL resourceUrl = getClass().getClassLoader().getResource(folder + "/" + filename);
		if (resourceUrl == null) {
	        System.err.println("Resource not found: " + filename);
	        return null;
	    }
		ImageIcon icon   = new ImageIcon(resourceUrl);
		ImageIcon scaled = new ImageIcon(icon.getImage().getScaledInstance(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, Image.SCALE_FAST));
		return scaled.getImage();
	}
}
