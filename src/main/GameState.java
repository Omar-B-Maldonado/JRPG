package main;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public interface GameState 
{
	void init();
	void update();
	void render(Graphics pen);
}
