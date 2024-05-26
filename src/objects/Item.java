package objects;

import java.awt.Graphics2D;
import java.awt.Image;

import engine.Camera;
import engine.Rect;

public class Item extends Rect
{
public Image image;
	
	public boolean inInventory = false;
	public boolean stackable   = false;

	private String itemType = "";
	private String  subType = "";
	private String     name = "";
	
	public Item(String name, int x, int y, int w, int h)
	{
		super(x, y, w, h);
		this.name  = name;
	}
	
	public void equip() 
	{
		inInventory = true;
	}
	
	public void unequip()
	{
		inInventory = false;
	}
	
	public boolean isStackable()
	{
		return stackable;
	}
	
	public void setWeapon() 
	{
		setItemType("weapon");
	}
	
	protected void setItemType(String itemType) 
	{
		this.itemType = itemType;
	}
	
	protected void setSubType(String subType)
	{
		this.subType = subType;
	}
	
	public String getItemType()
	{
		return itemType;
	}
	
	public String getSubType()
	{
		return subType;
	}
	
	public void draw(Graphics2D pen) 
	{
		if(!inInventory) pen.drawImage(image, (int)(x - Camera.x), (int)(y - Camera.y), (int)w, (int)h, null);
		super.draw(pen);
	}
}
