package main;

import javax.swing.*;
import java.awt.*;

public class GameStatePanel extends JPanel
{
	
	private static final int PANEL_WIDTH = 800;
	private static final int PANEL_HEIGHT = 600;
	
	private GameState currentState;
	
	public GameStatePanel()
	{
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setDoubleBuffered(true);
		setFocusable(true);
		requestFocus();
	}
	
	public void setState(GameState state)
	{
		this.currentState = state;
	}
	
	protected void paintComponent(Graphics pen)
	{
		super.paintComponent(pen);;
		if(currentState != null)
		{
			currentState.render(pen);
		}
	}
}
