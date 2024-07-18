package ui;

import java.awt.Graphics2D;

import main.Game;
import objects.Item;

public class Inventory 
{
	public Item selectedItem = null;
	public Item[] items;
	public int[] amount;
	
	int numSlots = 4;
	int slotSize = 32 * Game.SCALE; //pixels
	int width = slotSize * numSlots;
	int halfWidth = width/2;
	
	public Inventory()
	{
		items  = new Item[numSlots];
		amount = new  int[numSlots];
	}
	
	/* This method returns false if the item was not able to be added to the inventory.
	 * The item's type is first checked so it can be added to the appropriate slot. */
	public boolean addItem(Item newItem) 
	{
		for(int i = 0; i < items.length; i++) 
		{
				if(items[i] == null) 
				{ 
					items[i] = newItem;
					amount[i]++;
					newItem.equip();
					return true;
				}
				else if(items[i].getSubType().equals(newItem.getSubType()) && newItem.isStackable()) 
				{
					amount[i]++;
					newItem.equip();
					return true;
				}
		}
		return false;
	}
	
	/* This method sets the item in the specified inventory slot to null
	 * if its amount is 1 or less, and decrements the amount afterwards */
	public void removeItem(int slot) 
	{
		if(amount[slot]-- <= 1) items[slot] = null;
	}
	
	public void draw(Graphics2D pen)
	{
		pen.drawRoundRect(Game.SCREEN_HALF_WIDTH - this.halfWidth, Game.SCREEN_HEIGHT - 20 - slotSize,
						  width, slotSize, 0, 0);
	}
}
