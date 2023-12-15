package states;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;


import javax.swing.JTextArea;
import javax.swing.Timer;

import main.Game;
import main.GameStateManager;
import main.GameStatePanel;
import main.InputHandler;
import engine.Sprite;

public class Dialogue extends InputHandler implements GameState 
{
	GameStateManager stateManager;
	GameStatePanel     statePanel;
	GameState 		previousState;
	GameState			   battle;
	
	//Window
	int     x; int      y;
	int width; int height;
	
	String dialogues[] = new String[6];
	String currentDialogue = "";
	int dialogueIndex = 0;
	
	Timer letterByLetter;
	Timer pressCooldown;
	int cooldownTime;
	
	int i;
	String text;
	
	boolean displayingOptions;
	boolean drawSelectedBox;
	boolean endOfDialogue;
	
	String selected = null;
	String yes = "yes";
	String no  = "no";
	
	Image faceset = null;
	int soundCue;
	
	//-----------------------------------------------------------------------------------
	
	public void setDialogue() 
	{
		if (OverWorld.monk2 != null && OverWorld.enemy == OverWorld.monk2) 
		{
			dialogues[0] = "Hello, lad. What is cooking?";
			dialogues[1] = "So you've come to get a whooping?";
			dialogues[2] = "I used to be a sorcerer like you...";
			dialogues[3] = "but then I took an arrow to the shoe!";
			dialogues[4] = "Do you wanna fight?";
			dialogues[5] = "Dang, I thought you might.";
		}
	}
	
	public void speak()
	{
		text 				=    "";
		i    				=     0;
		selected            =  null;
		drawSelectedBox     = false;
		displayingOptions   = false;
		
		if (dialogueIndex >= dialogues.length) 
		{
			dialogueIndex   =     0;
			endOfDialogue   =  true;
		}
		else
		{
			currentDialogue = dialogues[dialogueIndex];
			dialogueIndex++;
			
			//add yes/no condition
			if (dialogueIndex == dialogues.length - 1) displayingOptions = true;
			
			pressCooldown.start();
			letterByLetter.start();
		}
	}
	
	@Override
	public void init() 
	{
		stateManager = Game.stateManager;
		statePanel	 = Game.statePanel;
		battle       = Game.battle;
	
		x = 32 * Game.SCALE;
		y = 16 * Game.SCALE;
		width  = Game.SCREEN_WIDTH - ((16 * Game.SCALE) * 4);
		height = 					   16 * Game.SCALE  * 4;
		
		soundCue = 0;
		
		cooldownTime  = 250;
		pressCooldown = new Timer(cooldownTime, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{pressCooldown.stop();}
		});
		
		letterByLetter = new Timer(50, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(text.equals(currentDialogue))
				{
					i = 0;
					letterByLetter.stop();
				}	
				else 
				{
					char character[] = currentDialogue.toCharArray();
					String c = String.valueOf(character[i]);
					text += c;//append can only take strings as a parameter
					i++;
					
					soundCue++;
					if(soundCue==2) //used to delay how often the sound is played
					{
						Game.soundManager.setSound("Voice3.wav");
						Game.soundManager.play();
						soundCue = 0;
					}
					
				}
			}
		});
		
		endOfDialogue = false;
		loadFaceset();
		setDialogue();
		speak();
	}

	@Override
	public void update() 
	{
		if (displayingOptions && pressing[_A] && !pressing[_D]) 
		{
			if (selected == yes || selected == null) playOptionSwitchedSound();
			selected = no;
		}
		if (displayingOptions && pressing[_D] && !pressing[_A]) 
		{
			if (selected == no  || selected == null) playOptionSwitchedSound();
			selected = yes;
		}
		if (selected != null) drawSelectedBox = true;
		
		if (pressing [ENTER] && !pressCooldown.isRunning()) 
		{
			if(letterByLetter.isRunning())
			{
				letterByLetter.stop();
				text = currentDialogue; //reveals full text
				playDialogueSkippedSound();
				pressCooldown.restart();
			}
			else if (!displayingOptions) 
			{
				if (endOfDialogue) 
				{
					stateManager.popState();
					OverWorld.pressCooldown.start();
				}
				else speak();
			}
			else if (displayingOptions)
			{
				if (selected == yes) 
				{
					playYesSound();
					
					Game.soundManager.stopMusic();
					stateManager.pushState(battle);
				}
				if (selected ==  no) 
				{
					playNoSound();
					speak();
				}
			}
		}
	}

	@Override
	public void render(Graphics pen) 
	{
		drawPreviousState  (pen);
		drawDialogueWindow (pen);
		drawText           (pen);	
		drawFaceSet        (pen);	
	}
	
	public void loadFaceset() 
	{
		if (OverWorld.monk2 != null && OverWorld.enemy == OverWorld.monk2) 
		{
			faceset = Toolkit.getDefaultToolkit().getImage("res/facesets/monk2Faceset.png");
			faceset = faceset.getScaledInstance(38 * Game.SCALE, 38 * Game.SCALE, Image.SCALE_FAST);
		}
	}
	
	public void playDialogueSkippedSound()
	{
		Game.soundManager.setSound("Voice4.wav");
		Game.soundManager.play();
	}
	
	public void playOptionSwitchedSound()
	{
		Game.soundManager.setSound("Menu2.wav");
		Game.soundManager.play();
	}
	
	public void playYesSound()
	{
		Game.soundManager.setSound("Accept2.wav");
		Game.soundManager.play();
	}
	
	public void playNoSound()
	{
		Game.soundManager.setSound("Success2.wav");
		Game.soundManager.play();
	}
	
	//------------------------------ DRAW METHODS ----------------------------------
	
	public void drawPreviousState(Graphics pen)
	{
		previousState = stateManager.getPreviousState();
		if (previousState != null) previousState.render(pen);
		
		OverWorld.bulletManager.pauseBullets();
		
		//pause skeletons
		for (int i = 0; i < OverWorld.skeleton.length;      i++) if (OverWorld.skeleton[i]      != null) OverWorld.skeleton[i].sprite.setMoving(false);
				
	}
	
	public void drawDialogueWindow(Graphics pen)
	{
		//draws black text box
		pen.setColor(new Color(0, 0, 0, 210));//opaque black
		pen.fillRoundRect(x, y, width, height, 35, 35);
				
		//draws white border
		pen.setColor(Color.WHITE);
		((Graphics2D) pen).setStroke(new BasicStroke(4));
		pen.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public void drawText(Graphics pen) 
	{
		pen.setFont(pen.getFont().deriveFont(Font.PLAIN, 19F));
		pen.drawString(text, x + 13 * Game.SCALE, y + 25 * Game.SCALE);
		if (displayingOptions && !letterByLetter.isRunning()) 
		{
			pen.drawString(no,  x + 25 * Game.SCALE, y + 40 * Game.SCALE);
			pen.drawString(yes, x + 50 * Game.SCALE, y + 40 * Game.SCALE);
		}
		if (drawSelectedBox && selected ==  no) pen.drawRect(x + 22 * Game.SCALE, y + 33 * Game.SCALE, 14 * Game.SCALE, 10 * Game.SCALE);
		if (drawSelectedBox && selected == yes) pen.drawRect(x + 47 * Game.SCALE, y + 33 * Game.SCALE, 16 * Game.SCALE, 10 * Game.SCALE);	
	}
	
	public void drawFaceSet(Graphics pen)
	{
		pen.drawImage(faceset, x + (16 + 120) * Game.SCALE, y + 12 * Game.SCALE, null);
		pen.drawRect(x+(17 + 120)*Game.SCALE, y+12*Game.SCALE, 38 * Game.SCALE, 38 * Game.SCALE);	
	}
	//------------------------------ DRAW METHODS ----------------------------------
	
}
	
	
	
	
