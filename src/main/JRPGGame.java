package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JRPGGame extends JFrame implements Runnable
{
	private Thread gameThread;
	private GameStatePanel statePanel;
	
	public static KeyDictionary keyHandler;
	
	public static GameStateManager stateManager;
	
	public static PlayState playState;
	public static BattleState battleState;
	public static PauseState pauseState;
	
	public JRPGGame()
	{
		setTitle("JRPG Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setSize(800, 600);
		setLocationRelativeTo(null);	//centers the frame
		setResizable(false);
	
		init();
	}
	
	public void init()
	{
		keyHandler = new KeyDictionary();
		addKeyListener(keyHandler);
		setFocusable(true);
		requestFocus();
		setVisible(true);									
		
		statePanel = new GameStatePanel();	//makes a new panel that paints based on the game's state
		getContentPane().add(statePanel);	//adds the panel to the frame
		
		stateManager = new GameStateManager(statePanel);	//makes a state manager that communicates wiwth the panel
		
		playState    = new PlayState();
		battleState  = new BattleState();
		pauseState   = new PauseState();
		
		stateManager.pushState(playState);	//start the game in the playState
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void run()
	{
		while(true) //game loop
		{
			
			stateManager.update();
			
			statePanel.repaint();
			
			try					{gameThread.sleep(15);} //~60fps
			
			catch(Exception x)  {					  }
		}
	}
	
	
	public static void main(String[] args) 
	{
		new JRPGGame();
	}

}
