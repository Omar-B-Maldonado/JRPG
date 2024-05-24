package states;


import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.*;
import main.*;

public class GameOver extends InputHandler implements GameState
{	
	
	@Override
	public void init() 
	{
		Game.soundManager.stopMusic();
		Game.soundManager.loadMusic("Lament.wav");
		Game.soundManager.playMusic();
	}

	@Override
	public void update() 
	{
		if (pressing[ENTER]) 
		{
			Game.soundManager.setSound("Success1.wav");
			Game.soundManager.play     				();
			
			Game.soundManager.stopMusic				();
			
			Game.stateManager.popState 				();	//gets rid of game over screen
			Game.stateManager.popState 				();	//gets rid of old overWorld
			Game.stateManager.pushState(Game.overWorld);//pushes new overWorld
		}
	}

	public void render(Graphics2D pen) 
	{
		pen.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		pen.setColor(Color.WHITE);
		pen.setFont(Game.customFont);
		
		pen.drawString("Game Over", 315, 280);
		
		pen.drawString("press enter to restart", 252, 350);
		
	}
}

