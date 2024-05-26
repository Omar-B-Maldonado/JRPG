package states;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import main.*;

public class Pause extends InputHandler implements GameState 
{
	GameStateManager stateManager;
	
	GameState previousState;
	
	//TO DO: PAUSE ALL SHOTCOOLDOWN TIMERS WHEN GAME PAUSES
	
	public Pause()
	{
		
	}
	
	public void init() 
	{
		stateManager  = Game.stateManager;	
	}

	public void update() 
	{	
		if(pressing[ESC]) stateManager.popState();
	}

	public void render(Graphics2D pen) 
	{
		OverWorld.bulletManager.pauseBullets();
		pause(OverWorld.player);
		pause(OverWorld.skeletons);
		pause(OverWorld.skellington);
		
		drawPreviousState(pen);
		
		//placeholder pause screen
		pen.setColor(Color.WHITE);
		pen.setFont(Game.customFont);
		pen.drawString("PAUSED", 325, 300);
	}
	
	public void drawPreviousState(Graphics2D pen)
	{
		previousState = stateManager.getPreviousState();		//gets the previous state
		if (previousState != null) previousState.render(pen);	//renders the previous state if it exists
	}
	
	public static void pause(Entity guy)
	{
		if (guy != null) guy.sprite.setMoving(false);
	}
	
	public static void pause(Entity[] guys)
	{
		for (Entity guy : guys) pause(guy);
	}

}
