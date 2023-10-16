package main;

import java.awt.Color;
import java.awt.Graphics;

public class BattleState implements GameState 
{
	GameStateManager stateManager;
	PauseState pauseState;
	
	int testPlayerX;
	int testPlayerY;
	
	int testPlayer2X;
	int testPlayer2Y;
	
	KeyDictionary keyHandler = JRPGGame.keyHandler;
	private boolean up_pressed = false;
	private boolean dn_pressed = false;
	private boolean lt_pressed = false;
	private boolean rt_pressed = false;
	
	public BattleState() 
	{
		
	}
	
	@Override
	public void init() 
	{
		stateManager = JRPGGame.stateManager;
		pauseState   = JRPGGame.pauseState;
		
		testPlayerX = 450;
		testPlayerY = 60;
		
		testPlayer2X = 60;
		testPlayer2Y = 450;
	}

	@Override
	public void update() 
	{
		if (keyHandler.p_pressed) stateManager.pushState(pauseState);
		
		if (keyHandler.up_pressed) testPlayerY -= 5;
        if (keyHandler.dn_pressed) testPlayerY += 5;
        if (keyHandler.lt_pressed) testPlayerX -= 5;
        if (keyHandler.rt_pressed) testPlayerX += 5;
        
        if (keyHandler.up_arrow_pressed) testPlayer2Y -= 5;
        if (keyHandler.dn_arrow_pressed) testPlayer2Y += 5;
        if (keyHandler.lt_arrow_pressed) testPlayer2X -= 5;
        if (keyHandler.rt_arrow_pressed) testPlayer2X += 5;
        
        if(testPlayerY > 400) stateManager.popState();
	}

	@Override
	public void render(Graphics pen)
	{	
		pen.setColor(Color.DARK_GRAY);
		pen.fillRect(0, 0, 600, 450);
		
		pen.setColor(Color.WHITE);
		pen.fillRect(testPlayerX, testPlayerY, 50, 50);
		pen.fillRect(testPlayer2X, testPlayer2Y, 50, 50);
	}
}
