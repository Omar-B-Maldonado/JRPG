package ui;

import java.awt.Graphics2D;

import objects.Item;

public class Inventory 
{
	public Item selectedItem = null;
	public Item[] items;
	public int[] amount;
	
	public Inventory()
	{
		items  = new Item[4];
		amount = new  int[4];
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
		if(amount[slot--] <= 1) items[slot] = null;
	}
	
	public void draw(Graphics2D pen)
	{
		/* To do */
	}
}
