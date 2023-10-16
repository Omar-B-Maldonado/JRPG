package main;

import java.awt.Color;
import java.awt.Graphics;

public class PlayState implements GameState
{
	GameStateManager stateManager;
	KeyDictionary keyHandler;
	BattleState battleState;
	PauseState pauseState;
	
	int playerX;
	int playerY;
	int moveSpeed;
	
	private boolean up_pressed = false;
	private boolean dn_pressed = false;
	private boolean lt_pressed = false;
	private boolean rt_pressed = false;
	
	public PlayState() 
	{
		
	}
	
	@Override
	public void init() 
	{
		keyHandler   = JRPGGame.keyHandler;
		stateManager = JRPGGame.stateManager;
		battleState  = JRPGGame.battleState;
		pauseState   = JRPGGame.pauseState;
		
		playerX = 50;
		playerY = 50;
		moveSpeed = 5;
	}
	
	@Override
	public void update() 
	{
		if (keyHandler.p_pressed)    stateManager.pushState(pauseState);
	    if (keyHandler.dash_pressed) stateManager.pushState(battleState);
			
		if (keyHandler.up_pressed) playerY -= moveSpeed;
        if (keyHandler.dn_pressed) playerY += moveSpeed;
        if (keyHandler.lt_pressed) playerX -= moveSpeed;
        if (keyHandler.rt_pressed) playerX += moveSpeed;
	}

	@Override
	public void render(Graphics pen) 
	{
		// Clear the screen by filling it with a background color
        pen.setColor(Color.BLUE);
        pen.fillRect(0, 0, 800, 600);

        // Draw a rectangle in a different color
        pen.setColor(Color.RED);
        pen.fillRect(playerX, playerY, 50, 50);
 
	}
}
