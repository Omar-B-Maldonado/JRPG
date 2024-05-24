package states;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.*;

import main.*;

public class MainMenu extends InputHandler implements GameState
{	
	
	public MainMenu()
	{
		
	}
	
	@Override
	public void init() 
	{
		Game.soundManager.loadMusic("Good Time.wav");
		Game.soundManager.playMusic();
	}

	@Override
	public void update() 
	{
		if (pressing[ENTER]) 
		{
			Game.soundManager.setSound("Success1.wav");
			Game.soundManager.play();
			
			Game.soundManager.stopMusic();
			Game.stateManager.pushState(Game.overWorld);		
		}
		
	}

	public void render(Graphics2D pen) 
	{
		pen.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		pen.setColor(Color.WHITE);
		pen.setFont(Game.customFont);
		pen.drawString("Press enter to start", 270, 300);
		
	}

}
