package ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import main.Game;
import main.InputHandler;
import states.Battle;

public class BattleUI extends InputHandler
{	
	private final int BUTTON_WIDTH				= 33  * Game.SCALE;
	private final int BUTTON_HEIGHT				= 11  * Game.SCALE;
	private final int ARROW_SIZE				= 13  * Game.SCALE;	
	//-----------------------------------------------------------------
	Image itemButtonUnpressed;			Image itemButtonUnpressedScaled;
	Image itemButtonPressed;			Image itemButtonPressedScaled;
	
	final int itemButtonX = Game.SCREEN_WIDTH  - (55 * Game.SCALE);
	final int itemButtonY = Game.SCREEN_HEIGHT - (21 * Game.SCALE);
	
	Image itemButtonImage;
	//-----------------------------------------------------------------
	Image attackButtonUnpressed;		Image attackButtonUnpressedScaled;
	Image attackButtonPressed;			Image attackButtonPressedScaled;
	
	final int attackButtonX = Game.SCREEN_WIDTH  - ((55 + 70) * Game.SCALE);
	final int attackButtonY = Game.SCREEN_HEIGHT - ( 21       * Game.SCALE);
	
	Image attackButtonImage;
	//-----------------------------------------------------------------
	Image defendButtonUnpressed;		Image defendButtonUnpressedScaled;
	Image defendButtonPressed;			Image defendButtonPressedScaled;
	
	final int defendButtonX = Game.SCREEN_WIDTH  - ((55 + 35) * Game.SCALE);
	final int defendButtonY = Game.SCREEN_HEIGHT - ( 21       * Game.SCALE);
	
	Image defendButtonImage;
	//-----------------------------------------------------------------
	Image arrow;						Image arrowScaled;
	Image arrowImage;
	
	boolean optionSwitchAllowed;
	boolean choiceAllowed;
	
	int   arrowDescendCount;
	Timer arrowBobTimer;
	Timer buttonRefreshTimer;
	
	int arrowX;
	int arrowY;
	
	boolean bumpSoundPlayed;
	
	public String choice;
	
	public BattleUI() 
	{
		arrowBobTimer = new Timer(200, new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent e) 
			{
				if (arrowDescendCount >= 3)
				{
					setArrowYBasedOnX();
					arrowDescendCount = 0;
				}
				else 
				{
					arrowY += (2 * Game.SCALE);
					arrowDescendCount++;
				}
			}
		});//1000ms == 1 second
		
		buttonRefreshTimer = new Timer(300, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				itemButtonImage   =   itemButtonUnpressedScaled;
				defendButtonImage = defendButtonUnpressedScaled;
				attackButtonImage = attackButtonUnpressedScaled;
				
				setArrowYBasedOnX();
				arrowDescendCount = 0;
				arrowBobTimer.start();
				
				buttonRefreshTimer.stop();	
			}
		});//1000ms == 1 second
		
		loadResources();
	}
	
	public void init() 
	{
		choiceAllowed = false;
		choice = "";
		setArrowXY(attackButtonX, attackButtonY);
		arrowDescendCount = 0;
		arrowBobTimer.start();
		
		arrowImage        = 				arrowScaled;
		itemButtonImage   =   itemButtonUnpressedScaled;
		attackButtonImage = attackButtonUnpressedScaled;
		defendButtonImage = defendButtonUnpressedScaled;
		
		bumpSoundPlayed = false;
	}
	
	public void update() 
	{
		
		//----------------------UPDATE ARROW CODE-----------------------------
	      if (!pressing[_D] && !pressing[_A]) 
	      {
	    	  optionSwitchAllowed  =  true;
	    	  bumpSoundPlayed      = false;
	      }
	     
	      if (optionSwitchAllowed) handleOptions();
	      updatePose();
	      
	      //-------------------------HANDLE BUTTON PRESS RESPONSE------------------------
	      if (!pressing[ENTER] && Battle.animationFinished) choiceAllowed = true;
	      
	      if (choiceAllowed && (arrowOnItem()) && pressing[ENTER]) 
	      {
	    	  itemButtonImage   = itemButtonPressedScaled;
	    	  choice 			= "item";
	    	  choiceAllowed     = false;
	    	  
	    	  arrowY = itemButtonY - 8 * Game.SCALE;
	    	  arrowBobTimer.stop();
	    	  
	    	  buttonRefreshTimer.restart();
	      }
	      
	      else if (choiceAllowed && (arrowOnDefend()) && pressing[ENTER]) 
	      {
	    	  defendButtonImage = defendButtonPressedScaled;
	    	  choice 			= "defend";
	    	  choiceAllowed     = false;
	    	  
	    	  arrowY = defendButtonY - 8 * Game.SCALE;
	    	  arrowBobTimer.stop();
	    	  
	    	  buttonRefreshTimer.restart();
	      }
	      
	      else if (choiceAllowed && (arrowOnAttack()) && pressing[ENTER]) 
	      {
	    	  attackButtonImage = attackButtonPressedScaled;
	    	  choice            = "attack";
	    	  choiceAllowed     = false;
	    	  
	    	  arrowY = attackButtonY - 8 * Game.SCALE;
	    	  arrowBobTimer.stop();
	    	  
	    	  buttonRefreshTimer.restart();
	      }
	      //---------------------------------------------------------------------------
	}
	
	public void render(Graphics pen)
	{	
		pen.drawImage(  itemButtonImage,   itemButtonX,   itemButtonY, null);
		pen.drawImage(defendButtonImage, defendButtonX, defendButtonY, null);
		pen.drawImage(attackButtonImage, attackButtonX, attackButtonY, null);
		
		pen.drawImage(arrowImage, arrowX + 9 * Game.SCALE, arrowY, null);	
	}
	
	public void updatePose() {
		if      (arrowOnAttack()) Battle.setNimaPose("attack");
		else if (arrowOnDefend()) Battle.setNimaPose("defend");
	}
	
	public void handleOptions()
	{
		if (pressing[_D]) 
	    {
			  if      (arrowOnAttack()) 
			  {
				  setArrowXY(defendButtonX, defendButtonY);
				  resetArrowVMovement(); 
		    	  playArrowMoveSound();
			  }
			  else if (arrowOnDefend()) 
			  {
				  Battle.setNimaPose("defend");
				  setArrowXY(  itemButtonX,   itemButtonY); 
				  resetArrowVMovement();
		    	  playArrowMoveSound();
			  }  
			  else  //arrowOnItem()
			  {
				  setArrowXY(  itemButtonX,   itemButtonY);
				  resetArrowVMovement();
				  playArrowBumpSound();
			  }
		  	  	optionSwitchAllowed = false;
	      }
		
		if (pressing[_A]) 
	    {
			  if      (arrowOnItem()) 
			  {
				  setArrowXY(defendButtonX, defendButtonY);
				  resetArrowVMovement();
		    	  playArrowMoveSound();
			  }
			  else if (arrowOnDefend()) 
			  {
				  setArrowXY(attackButtonX, attackButtonY);
				  resetArrowVMovement();
		    	  playArrowMoveSound();
			  }
			  else  //arrowOnAttack()
			  {
				  setArrowXY(attackButtonX, attackButtonY);
				  resetArrowVMovement();
				  playArrowBumpSound();
			  }
		  	  	optionSwitchAllowed = false;
	      }
	}
	
	public boolean choiceAllowed()
	{
		if (!pressing[ENTER] && !buttonRefreshTimer.isRunning() && Battle.animationFinished) return true;
		else return false;
	}
	
	public void setChoice(String choice) {this.choice = choice;}
	
	public boolean arrowOnItem  () {return arrowX ==   itemButtonX;}
	public boolean arrowOnAttack() {return arrowX == attackButtonX;}
	public boolean arrowOnDefend() {return arrowX == defendButtonX;}
	
	public void setArrowXY(int x, int y)
	{
		setArrowX(x);
		setArrowYAbove(y);
	}
	
	public void setArrowX(int x) {arrowX = x;}
	public void setArrowYAbove(int y) {arrowY = y - (20 * Game.SCALE);}
	public void setArrowYBasedOnX()
	{
		if (arrowX == attackButtonX) setArrowYAbove(attackButtonY);
		if (arrowX == defendButtonX) setArrowYAbove(defendButtonY);
		if (arrowX ==   itemButtonX) setArrowYAbove  (itemButtonY);
	}
	
	public void playArrowMoveSound()
	{
		Game.soundManager.setSound("Menu1.wav");
   	  	Game.soundManager.play();
	}
	
	public void playArrowBumpSound()
	{
		if(!bumpSoundPlayed) 
		{
			Game.soundManager.setSound("MiniImpact.wav");
		    Game.soundManager.play();
		    bumpSoundPlayed = true;
		}	
	}
	
	public void resetArrowVMovement()
	{
		//moves arrow to top of pseudo animation & restarts descent
		arrowDescendCount = 0;
		arrowBobTimer.restart();
	}
	
	public void loadResources()
	{
		//get each image, then scale each image
		arrow 	   					= Toolkit.getDefaultToolkit().getImage("res/battle ui/arrow.png");	
		arrowScaled   			 	= arrow.getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_FAST);
			
		itemButtonUnpressed 	    = Toolkit.getDefaultToolkit().getImage("res/battle ui/item_button_0.png");	
		itemButtonUnpressedScaled   = itemButtonUnpressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);
		
		attackButtonUnpressed 	    = Toolkit.getDefaultToolkit().getImage("res/battle ui/attack_button_0.png");	
		attackButtonUnpressedScaled = attackButtonUnpressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);
		
		defendButtonUnpressed 	    = Toolkit.getDefaultToolkit().getImage("res/battle ui/defend_button_0.png");	
		defendButtonUnpressedScaled = defendButtonUnpressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);
		
		itemButtonPressed 	        = Toolkit.getDefaultToolkit().getImage("res/battle ui/item_button_1.png");	
		itemButtonPressedScaled     = itemButtonPressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);
		
		attackButtonPressed 	    = Toolkit.getDefaultToolkit().getImage("res/battle ui/attack_button_1.png");	
		attackButtonPressedScaled   = attackButtonPressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);
		
		defendButtonPressed 	    = Toolkit.getDefaultToolkit().getImage("res/battle ui/defend_button_1.png");	
		defendButtonPressedScaled   = defendButtonPressed.getScaledInstance(BUTTON_WIDTH, BUTTON_HEIGHT, Image.SCALE_FAST);		
	}
	
}
