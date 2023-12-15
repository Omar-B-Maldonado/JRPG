package engine;

import main.Game;
import states.OverWorld;

public class Camera
{
	public static double x = 0;
	public static double y = 0;
	public static double z = 0;
	
	public void setX(double x) {Camera.x = x;}
	public void setY(double y) {Camera.y = y;}
	
	public static void setPosition(double x, double y)
	{
		Camera.x = x;
		Camera.y = y;
	}
	
	public static void moveBy(double vx, double vy)
	{
		Camera.x += vx;
		Camera.y += vy;
	}
	
	public static void goLT(double dx)
	{
		x -= dx;
	}

	public static void goRT(double dx)
	{
		x += dx;
	}

	public static void goIN(double dz)
	{
		z += dz;
	}

	public static void goOT(double dz)
	{
		z -= dz;
	}

	public static void goUP(double dy)
	{
		y -= dy;
	}

	public static void goDN(double dy)
	{
		y += dy;
	}

}