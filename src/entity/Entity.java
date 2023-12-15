package entity;

import java.awt.Graphics;

import javax.swing.Timer;

import engine.Rect;
import engine.Sprite;
import main.InputHandler;

public abstract class Entity extends InputHandler
{
	public int originX;
	public int originY;
	public double speed;
	public double walkSpeed;
	public double dashSpeed;
	public double size;
	public static String[] pose = {"UP", "DN", "LT", "RT"};
	
	public Rect spawnNode;
	public Sprite sprite;
	public int health;
	
	//for shooting
	String[] projectilePose = {""   ,    ""};
	
	public int bulletVelocity;
	int bW = 30;
	int bH = 30;	
	public Timer shotCooldown;
	int   		 cooldownTime;
}
