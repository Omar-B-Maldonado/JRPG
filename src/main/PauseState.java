package main;

import java.awt.Color;
import java.awt.Graphics;

public class PauseState implements GameState 
{
	GameStateManager stateManager;
	KeyDictionary keyHandler;
	
	GameState previousState;
	
	@Override
	public void init() 
	{
		stateManager = JRPGGame.stateManager;
		keyHandler = JRPGGame.keyHandler;
	}

	@Override
	public void update() 
	{	
		if(keyHandler.esc_pressed)	stateManager.popState();
	}

	@Override
	public void render(Graphics pen) 
	{
		previousState = stateManager.getPreviousState();		//gets the previous state if there is one
		if (previousState != null) previousState.render(pen);	//renders the previous state if it exists
		
		//placeholder pause screen paint stuff
		pen.setColor(Color.ORANGE);
		pen.fillRect(70, 70, 400, 100);
		
	}

}
