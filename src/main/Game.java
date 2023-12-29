package main;

import javax.swing.*;

import states.*;

import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

public class Game extends InputHandler implements Runnable
{	
	private static final int ORIGINAL_SCREEN_WIDTH  = 256;
	private static final int ORIGINAL_SCREEN_HEIGHT = 192;
	public  static final int SCALE 					= 	3;
	public  static final int 		  SCREEN_WIDTH  = ORIGINAL_SCREEN_WIDTH  * SCALE; //256 x 3 = 768
	public  static final int 		  SCREEN_HEIGHT = ORIGINAL_SCREEN_HEIGHT * SCALE; //192 x 3 = 576
	
	private       Thread           gameThread;
	
	public static GameStatePanel   statePanel;
	public static GameStateManager stateManager;
	public static     SoundManager soundManager;
	
	public static MainMenu  	   mainMenu;
	public static OverWorld 	   overWorld;
	public static Dialogue		   dialogue;
	public static Battle    	   battle;
	public static Pause     	   pause;
	public static GameOver		   over;
	
	public static Font 			   customFont;
	
	int mouseX = -1;
	int mouseY = -1;
	
	public Game()
	{
		setTitle("JRPG Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable		 (false);
		setLocationRelativeTo(null );
		
		setBackground(Color.GREEN);

		addKeyListener(this);
		allowDragging (this);
		setFocusable  (true);
		requestFocus();	
		
		setUndecorated(true);
		setShape   (new Rectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT));//removes rounded corners
		
		
		statePanel = new GameStatePanel(SCREEN_WIDTH, SCREEN_HEIGHT); //makes a new panel that paints based on the game's state
		add(statePanel);	 										  //adds the panel to the frame
		
		init();
		
		setVisible(true);
	}
	
	public void init()
	{								
		try //from: https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
		{
		    //create the font to use
		    customFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/NormalFont.ttf"));
		    //set the font size
		    customFont = customFont.deriveFont(24f);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} 
		catch(IOException         e) {e.printStackTrace();}
		catch(FontFormatException e) {e.printStackTrace();}
		
		//make a state manager that communicates with the panel we made
		stateManager = new GameStateManager();
		soundManager = new SoundManager	   ();
		
		
		//create game states
		mainMenu     = new MainMenu ();
		overWorld    = new OverWorld();
		dialogue     = new Dialogue ();
		battle       = new Battle   ();
		pause        = new Pause    ();
		over		 = new GameOver ();
		
		//start the game in the mainMenu state
		stateManager.pushState(mainMenu);
		
		gameThread   = new Thread(this);
		gameThread.start();
	}
	
	public void run()
	{
		while(true) //game loop
		{	
			
			stateManager.update();
			statePanel.repaint ();
			
			try					{gameThread.sleep(15);} //~60fps
			catch(Exception x)  {					  }
		}
	}
	
	public static void main(String args[])
	{
		new Game();
	}
	
}
