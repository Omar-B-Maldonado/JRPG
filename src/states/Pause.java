package states;

import java.awt.Color;
import java.awt.Graphics;

import entity.Player;
import main.*;

public class Pause extends InputHandler implements GameState 
{
	GameStateManager stateManager;
	
	GameState previousState;
	
	public Pause()
	{

	}
	
	@Override
	public void init() 
	{
		stateManager  = Game.stateManager;	
	}

	@Override
	public void update() 
	{	
		if(pressing[ESC]) stateManager.popState();
	}

	@Override
	public void render(Graphics pen) 
	{
		OverWorld.player.sprite.setMoving(false);
		
		OverWorld.bulletManager.pauseBullets();
		
		//pause skeletons
		for (int i = 0; i < OverWorld.skeleton.length; i++) if (OverWorld.skeleton[i] != null) OverWorld.skeleton[i].sprite.setMoving(false);
		
		drawPreviousState(pen);
		
		//placeholder pause screen
		pen.setColor(Color.WHITE);
		pen.setFont(Game.customFont);
		pen.drawString("PAUSED", 325, 300);
	}
	
	public void drawPreviousState(Graphics pen)
	{
		previousState = stateManager.getPreviousState();		//gets the previous state
		if (previousState != null) previousState.render(pen);	//renders the previous state if it exists
	}

}
