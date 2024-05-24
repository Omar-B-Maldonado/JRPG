package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import engine.Camera;
import main.Game;

public class HealthBar 
{
	public int containerX, containerY;
	public int containerWidth, containerHeight;

	public int barWidth, height;
	public int widthTo1DamageFactor;
	public Color color;
	public int arc;
	
	private double maxHealth;
	
	public HealthBar(int x, int y, int w, int h, int arc, int maxHealth)
	{
		setPosition(x, y);
		containerWidth  = w;
		containerHeight = h;
		this.arc = arc;
		
		this.maxHealth = (double) maxHealth; //100% 
		updateWidth(maxHealth);
		color = Color.red;
		
		widthTo1DamageFactor = barWidth / maxHealth;
	}
	
	public void updateWidth(int currentHealth) 
	{
		barWidth = (int)(((double)currentHealth / maxHealth) * containerWidth);
	}
	
	public void setPosition(int x, int y)
	{
		containerX = x;
		containerY = y;
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public void draw(Graphics2D pen)
	{
		int offsetX = (int)Camera.x;
		int offsetY = (int)Camera.y;
		if (Game.stateManager.getCurrentState() == Game.battle)
		{
			offsetX = 0;
			offsetY = 0;
		}
		Stroke oldStroke = pen.getStroke();
			
		//health bar container
		pen.setColor(Color.BLACK);
		pen.fillRoundRect(containerX - offsetX, containerY - offsetY, containerWidth, containerHeight, arc, arc);	
		
		//health bar
		pen.setColor(color);
		pen.fillRoundRect(containerX - offsetX, containerY - offsetY, barWidth, containerHeight, arc, arc);
		
		//outline
		pen.setStroke(new BasicStroke(2));
		pen.setColor(Color.BLACK);
		pen.drawRoundRect(containerX - offsetX, containerY - offsetY, containerWidth, containerHeight, arc, arc);	
		
		pen.setStroke(oldStroke);
	}
	
	public void drawHealthBar(Graphics pen)
	{		
		
			
	}
}
