package states;

import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public interface GameState 
{
	void init();
	void update();
	void render(Graphics2D pen);
}