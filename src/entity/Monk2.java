package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import engine.Sprite;
import states.OverWorld;

public class Monk2 extends Entity
{
	int attTimerWidth;
	public Timer attTimer;
	
	public Monk2(int x, int y)
	{	
		walkSpeed  = 3.0;
		dashSpeed  = 6.0;
		size       =  40;
		sprite     = new Sprite("monk2", Entity.pose, 1, x, y, size, size);
		health     =  40;
		
		hBarContainerX     = 496;
		hBarContainerY     =  76;
		hBarWidth          = 200;
		hBarContainerWidth = hBarWidth + 8;
		
		hBarTo1DamageFactor = hBarWidth / health;
		
		attTimerWidth    = hBarWidth;
		
		attTimer = new Timer(22, new ActionListener() 
		{
			@Override public void actionPerformed(ActionEvent e) 
			{
				attTimerWidth --;
				
				if (attTimerWidth <=0) 
				{
					OverWorld.player.hitFor(3);
					attTimerWidth = 200;
				}
			}
		});
	}
	
	public void update()
	{
		sprite.setPose("DN");
	}
	
	public void draw(Graphics pen) 
	{      
        sprite.draw(pen);
    }
	
	public void drawBattleHealth(Graphics pen)
	{
		//health bar container
		pen.setColor(Color.DARK_GRAY);
		pen.fillRoundRect(hBarContainerX, hBarContainerY, hBarContainerWidth, 20, 15, 15);
				
		//health bar
		pen.setColor(Color.MAGENTA);
		pen.fillRoundRect(hBarContainerX + 4, hBarContainerY + 4, hBarWidth, 12, 15, 15);
	}
	
	public void drawAttackTimer(Graphics pen)
	{
		//AttTimer bar
		pen.setColor(Color.BLUE);
		pen.fillRoundRect(hBarContainerX, hBarContainerY + 24, hBarContainerWidth - 3, 8, 10, 10);
		pen.setColor(Color.CYAN);
		pen.fillRoundRect(hBarContainerX + 4, hBarContainerY + 26, attTimerWidth, 4, 10, 10);
	}
}
