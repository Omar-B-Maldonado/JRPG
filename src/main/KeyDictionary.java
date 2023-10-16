package main;
import java.awt.event.*;

public class KeyDictionary implements KeyListener 
{
	public static boolean up_pressed   = false;				//when the program starts
	public static boolean dn_pressed   = false;				//nobody is pressing anything
	public static boolean lt_pressed   = false;
	public static boolean rt_pressed   = false;
	
	public static boolean up_arrow_pressed = false;
	public static boolean dn_arrow_pressed = false;
	public static boolean lt_arrow_pressed = false;
	public static boolean rt_arrow_pressed = false;
	
	public static boolean p_pressed = false;
	public static boolean dash_pressed = false;
	public static boolean esc_pressed = false;
	
	//------------------------------------------------------//
	
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();

		if(code == KeyEvent.VK_W)  		 up_pressed = true;
		if(code == KeyEvent.VK_S)  		 dn_pressed = true;
		if(code == KeyEvent.VK_A)  		 lt_pressed = true;
		if(code == KeyEvent.VK_D)  		 rt_pressed = true;
		
		if(code == KeyEvent.VK_UP)  		 up_arrow_pressed = true;
		if(code == KeyEvent.VK_DOWN)  		 dn_arrow_pressed = true;
		if(code == KeyEvent.VK_LEFT)  		 lt_arrow_pressed = true;
		if(code == KeyEvent.VK_RIGHT)  		 rt_arrow_pressed = true;
		
		if(code == KeyEvent.VK_P)  		 p_pressed  = true;
		if(code == KeyEvent.VK_SHIFT)  dash_pressed = true;
		if(code == KeyEvent.VK_ESCAPE) esc_pressed  = true;
	}
	
	//------------------------------------------------------//
	
	public void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W)   	 up_pressed = false;
		if(code == KeyEvent.VK_S)   	 dn_pressed = false;
		if(code == KeyEvent.VK_A)   	 lt_pressed = false;
		if(code == KeyEvent.VK_D)  		 rt_pressed = false;	
		
		if(code == KeyEvent.VK_UP)  		 up_arrow_pressed = false;
		if(code == KeyEvent.VK_DOWN)  		 dn_arrow_pressed = false;
		if(code == KeyEvent.VK_LEFT)  		 lt_arrow_pressed = false;
		if(code == KeyEvent.VK_RIGHT)  		 rt_arrow_pressed = false;
		
		if(code == KeyEvent.VK_P)  		 p_pressed  = false;
		if(code == KeyEvent.VK_SHIFT)  dash_pressed = false;
		if(code == KeyEvent.VK_ESCAPE) esc_pressed  = false;
	}
	
	//------------------------------------------------------//
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	//------------------------------------------------------//
	
}
