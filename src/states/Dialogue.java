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
import entity.Entity;
import entity.Monk2;
import entity.Skellington;

public class Dialogue extends InputHandler implements GameState 
{
	// dialogue system derived from https://www.youtube.com/@RyiSnow
	
	GameStateManager stateManager;
	GameStatePanel     statePanel;
	GameState 		previousState;
	GameState			   battle;
	
	Entity npc;
	
	//Window
	int x, y, width, height;
	
	String dialogues[] = new String[6];
	String currentDialogue = "";
	int dialogueIndex;
	
	Timer letterByLetter;
	Timer  pressCooldown;
	int     cooldownTime;
	
	int       i;
	String text;
	
	boolean displayingOptions;
	boolean drawSelectedBox;
	boolean endOfDialogue;
	
	String selected = null;
	String yes = "yes";
	String no  = "no";
	
	Image faceset = null;
	int soundCue;
	//NEED TO MAKE IT SO OPTION SWITCHING ISNT ALLOWED AND SOUND FOR IT
	//DOESN'T PLAY UNLESS THE TEXT IS FULLY REVEALED
	//-----------------------------------------------------------------------------------
	
	public void setDialogue() 
	{
		if (npc instanceof Monk2) 
		{
			dialogues[0] = "Hello, lad. What is cooking?";
			dialogues[1] = "So you've come to get a whooping?";
			dialogues[2] = "I used to be a sorcerer like you...";
			dialogues[3] = "but then I took an arrow to the shoe!";
			dialogues[4] = "Do you wanna fight?";
			dialogues[5] = "Dang, I thought you might.";
		}
		
		else if (npc instanceof Skellington) 
		{
			dialogues[0] = "Leave me alone!";
			dialogues[1] = "I'm not like the other skeletons!";
			dialogues[2] = "I'm Skellington!";
			dialogues[3] = "Sorry if that was rude...";
			dialogues[4] = "But I don't want to be near you...";
			dialogues[5] = "it's nothing personal.";
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
			
			//add yes/no condition for certain NPCs
			if (npc instanceof Monk2 && dialogueIndex == dialogues.length - 1)
			{
				displayingOptions = true;
			}
			
			pressCooldown.start();
			letterByLetter.start();
		}
	}
	
	public void init() 
	{
		stateManager = Game.stateManager;
		statePanel	 = Game.statePanel;
		battle       = Game.battle;
		
		npc = OverWorld.getInteractor();
		
		x = 32 * Game.SCALE;
		y = 16 * Game.SCALE;
		width  = Game.SCREEN_WIDTH - ((16 * Game.SCALE) * 4);
		height = 					   16 * Game.SCALE  * 4;
		
		dialogueIndex = 0;
		soundCue 	  = 0;
		
		cooldownTime  = 250;
		pressCooldown = new Timer(cooldownTime, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{pressCooldown.stop();}
		});
		
		//
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
						playSound("Voice3");
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

	public void update() 
	{
		if (displayingOptions && pressing[_A] && !pressing[_D]) 
		{
			if (selected == yes || selected == null) playSound("OptionSwitched");
			selected = no;
		}
		if (displayingOptions && pressing[_D] && !pressing[_A]) 
		{
			if (selected == no  || selected == null) playSound("OptionSwitched");
			selected = yes;
		}
		if (selected != null) drawSelectedBox = true;
		
		if (pressing [ENTER] && !pressCooldown.isRunning()) 
		{
			if(letterByLetter.isRunning())
			{
				letterByLetter.stop();
				text = currentDialogue; //reveals full text
				playSound("DialogueSkipped");
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
					playSound("Yes");
					
					Game.soundManager.stopMusic();
					stateManager.pushState(battle);
				}
				if (selected ==  no) 
				{
					playSound("No");
					speak();
				}
			}
		}
	}

	public void render(Graphics2D pen) 
	{
		drawPreviousState  (pen);
		drawDialogueWindow (pen);
		drawText           (pen);	
		drawFaceSet        (pen);	
	}
	
	public void loadFaceset() //this can probably be optimized if we have the entities name string used
	{
		faceset = Toolkit.getDefaultToolkit().getImage("res/facesets/" + npc.name + "Faceset.png");
		faceset = faceset.getScaledInstance(38 * Game.SCALE, 38 * Game.SCALE, Image.SCALE_FAST);
	}
	
	public void playSound(String fileName){
		Game.soundManager.playSound(fileName);
	}
	
	//------------------------------ DRAW METHODS ----------------------------------
	
	public void drawPreviousState(Graphics2D pen)
	{
		previousState = stateManager.getPreviousState();
		if (previousState != null) 
		{	
			OverWorld.bulletManager.pauseBullets();
			
			Pause.pause(OverWorld.player);
			Pause.pause(OverWorld.skeletons);
			Pause.pause(OverWorld.skellington);
			
			previousState.render(pen);
		}
	}
	
	public void drawDialogueWindow(Graphics2D pen)
	{
		//draws black text box
		pen.setColor(new Color(0, 0, 0, 210));//opaque black
		pen.fillRoundRect(x, y, width, height, 35, 35);
				
		//draws white border
		pen.setColor(Color.WHITE);
		((Graphics2D) pen).setStroke(new BasicStroke(4));
		pen.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public void drawText(Graphics2D pen) 
	{
		pen.setFont(pen.getFont().deriveFont(Font.PLAIN, 19F));
		
		//text is drawn here
		pen.drawString(text, x + 13 * Game.SCALE, y + 25 * Game.SCALE);
		
		if (displayingOptions && !letterByLetter.isRunning()) 
		{
			pen.drawString(no,  x + 25 * Game.SCALE, y + 40 * Game.SCALE);
			pen.drawString(yes, x + 50 * Game.SCALE, y + 40 * Game.SCALE);
			
			if (drawSelectedBox && selected ==  no) pen.drawRect(x + 22 * Game.SCALE, y + 33 * Game.SCALE, 14 * Game.SCALE, 10 * Game.SCALE);
			if (drawSelectedBox && selected == yes) pen.drawRect(x + 47 * Game.SCALE, y + 33 * Game.SCALE, 16 * Game.SCALE, 10 * Game.SCALE);	
		}
	}
	
	public void drawFaceSet(Graphics2D pen)
	{
		pen.drawImage(faceset, x + (16 + 120) * Game.SCALE, y + 12 * Game.SCALE, null);
		pen.drawRect(x+(17 + 120)*Game.SCALE, y+12*Game.SCALE, 38 * Game.SCALE, 38 * Game.SCALE);	
	}
	//------------------------------ DRAW METHODS ----------------------------------
}
	
	
	
	
