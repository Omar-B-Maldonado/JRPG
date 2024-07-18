package objects;

import java.awt.Toolkit;

import main.Game;

public class Potion extends Item
{

	public Potion(String name, int x, int y) 
	{
		super(name, x, y, 18, 22);
		setItemType("potion");
		setSubType(name);
		stackable = true;
		image = Toolkit.getDefaultToolkit().getImage("res/items/" + name + ".png");
	}
}
