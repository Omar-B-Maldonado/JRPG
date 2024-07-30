package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Sprite;
import states.OverWorld;
import ui.HealthBar;

public class Monk2 extends Entity
{
	
	public Monk2(int x, int y)
	{	
		super("monk2", defaultPose, 1, x, y, defaultSize, defaultSize);
		initializeHealth(40);
		setSpeeds(3, 6);
		
		healthBar = new HealthBar(496, 76, 208, 10, 15, maxHealth);	
		
		attackTimerWidth    = healthBar.barWidth;
		
		attackTimer = new Timer(22, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{
				attackTimerWidth --;
			}
		});
	}
	
	public void update()
	{
		faceDN();
	}
	
	public void handleCollisions()
	{
		
	}
	
	public void draw(Graphics pen) 
	{      
        sprite.draw(pen);
    }

	public void drawAttackTimer(Graphics pen)
	{
		//AttTimer bar
		pen.setColor(Color.BLUE);
		pen.fillRoundRect(healthBar.containerX, healthBar.containerY + 24, healthBar.containerWidth - 3, 8, 10, 10);
		pen.setColor(Color.CYAN);
		pen.fillRoundRect(healthBar.containerX + 4, healthBar.containerY + 26, attackTimerWidth, 4, 10, 10);
	}
}
