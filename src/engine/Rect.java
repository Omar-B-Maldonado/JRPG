package engine;

import java.awt.*;

import entity.Player;
import states.OverWorld;

public class Rect 
{
	public double	x; public double y;											//the top left corner of an axis-aligned rectangle
	public double	w; public double h;											//the width and height of a rectangle
	
	double vx = 0;											//velocity in the x-direction
	double vy = 0;											//velocity in the y-direction
	
	Color c = Color.black;									//if no-one tells us otherwise, default color is black
	
	boolean held = false;
	
	//------------------------------------------------------//
	
	public Rect(int x, int y, int w, int h)					//rectangle constructor
	{
		this.x = x;	this.y = y;
		
		this.w = w;	this.h = h;
	}
	
	public Rect(int x, int y, int w, int h, Color c)					//rectangle constructor
	{
		this(x, y, w, h);
		
		setColor(c);
	}
	
	//------------------------------------------------------//
	
	public boolean contains(int mx, int my)					//checks if the passed mouse pressed
	{														//(x, y) coordinates are inside this rect
		return	(
		
		(mx > this.x		 ) &&
		(mx < this.x + this.w) &&
		
		(my > this.y		 ) &&
		(my < this.y + this.h)
		
				);
	}
	
	//------------------------------------------------------//
	
	public boolean overlaps(Rect r) 						//checks if the passed Rect overlaps this rectangle
	{
		return (
		
		(r.x		<= this.x + this.w)	&&					
		(r.x + r.w  >= this.x		 )	&&					
		
		(r.y		<= this.y + this.h)	&&					
		(r.y + r.h  >= this.y         )						
		
				);
	}
	
	//------------------------------------------------------//
	
	public void physicsOFF()
	{
		this.setVelocity(0, 0);
	}
	
	//------------------------------------------------------//
	
	public void grab() 				{held = true;}
	
	public void drop() 				{held = false;}
	
	//------------------------------------------------------//
	
	public void resizeBy(int dw, int dh)
	{
		w += dw;
		
		h += dh;
	}
	
	//------------------------------------------------------//
	
	public void setX(double x) 		{this.x = x;}
		
	public void setY(double y)		{this.y = y;}
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	//------------------------------------------------------//
		
	public void setColor(Color c)	{this.c = c;}
	
	//------------------------------------------------------//
	
	public void setVelocity(int vx, int vy)
	{
		this.vx = vx;
		
		this.vy = vy;
	}
	
	public void setHorizontalVelocity(int vx)
	{
		this.vx = vx;
	}
	public void setVerticalVelocity(int vy)
	{
		this.vy = vy;
	}
	
	//------------------------------------------------------//
	
	public void move()										//for moving via velocity change
	{
		x += vx;
		y += vy;
	}
	
	public void jump(int dy)										//for moving via velocity change
	{
		vy -= dy;
	}
	
	public void goUP(double dy)
	{
		vy = -dy;
	}
	
	public void goDN(double dy)
	{
		vy = dy;
	}
	
	public void goLT(double dx)
	{
		vx = -dx;
	}
	
	public void goRT(double dx)
	{
		vx = dx;
	}
	
	public void stopVerticalMovement() {
	    vy = 0;
	}

	public void stopHorizontalMovement() {
	    vx = 0;
	}
	
	//------------------------------------------------------//
	
	public void moveUP(double dy)
	{
		y -= dy;
	}
	
	public void moveDN(double dy)
	{
		y += dy;
	}
	
	public void moveLT(double dx)
	{
		x -= dx;
	}
	
	public void moveRT(double dx)
	{
		x += dx;
	}
	
	//------------------------------------------------------//
	
	public void bounceOff(Rect r)
	{
		if(wasAbove(r)  || wasBelow(r)) bounceV();
		
		if(wasLeftOf(r) || wasRightOf(r)) bounceH();
	}
	
	//------------------------------------------------------//
	
	public boolean isAbove(Rect r)
	{
		return y < r.y - h + 1;
	}
	
	public boolean isBelow(Rect r)
	{
		return y > r.y + r.h - 1;
	}
	
	public boolean isLeftOf(Rect r)
	{
		return x < r.x - w + 1;
	}
	
	public boolean isRightOf(Rect r)
	{
		return x > r.x + r.w - 1;
	}
	//------------------------------------------------------//
	
	public void chase(Rect r)
	{
		if (r.isAbove  (this)) goUP(4);
		if (r.isBelow  (this)) goDN(4);
		if (r.isLeftOf (this)) goLT(4);
		if (r.isRightOf(this)) goRT(4);
	}
	//------------------------------------------------------//
	
	
	public void pushOutOf(Rect r)
	{
		if(wasAbove  (r)) pushAbove  (r);
		
		if(wasBelow  (r)) pushBelow  (r);

		if(wasLeftOf (r)) pushLeftOf (r);
		
		if(wasRightOf(r)) pushRightOf(r);
	}
	
	public boolean wasAbove(Rect r)
	{
		return y - vy < r.y - h + 1;
	}
	
	public boolean wasBelow(Rect r)
	{
		return y - vy > r.y + r.h - 1;
	}
	
	public boolean wasLeftOf(Rect r)
	{
		return x - vx < r.x - w + 1;
	}
	
	public boolean wasRightOf(Rect r)
	{
		return x - vx > r.x + r.w - 1;
	}

	//------------------------------------------------------//
	
	public void pushAbove(Rect r)
	{
		y = r.y - h - 1;
	}
	
	public void pushBelow(Rect r)
	{
		y = r.y + r.h + 1;
	}
	
	public void pushLeftOf(Rect r)
	{
		x = r.x - w - 1;
	}
	
	public void pushRightOf(Rect r)
	{
		x = r.x + r.w + 1;
	}
	
	//------------------------------------------------------//
	
	public boolean isTouching(Rect r)
	{
		return (y == r.y - h - 1) || (y == r.y + r.h + 1) || (x == r.x - w - 1) || (x == r.x + r.w + 1);
	}
	
	public boolean isInTalkingRangeOf(Rect r)
	{
		double topBound    = r.y - h - 5;
		double bottomBound = r.y + r.h + 5;
		double rightBound  = r.x + r.w + 5;
		double leftBound   = r.x - w - 5;
		
		return (topBound <= y && y < bottomBound) && (leftBound <= x && x < rightBound);
	}
	
	public void knockBack(Rect r)
	{
		if(wasAbove  (r)) r.goDN(20);
		if(wasBelow  (r)) r.goUP(20);

		if(wasLeftOf (r)) r.goRT(20);
		if(wasRightOf(r)) r.goLT(20);
		
	}
	
	public void bounceV()			
	{
		vy = -vy * 0.6;
		
		if(Math.abs(vy) < 0.3) vy = 0;
	}
	
	public void bounceH()			
	{
		vx = -vx * 0.6;
		
		if(Math.abs(vy) < 0.3) vx = 0;
	}
	
	//------------------------------------------------------//
	
	public void stopsFalling() 				{vy = 0	   ;}	//makes you stop in your tracks vertically
	
	public void applyFrictionWithFloor()	{vx = vx*.6;}	//makes you stop in your tracks horizontally
	
	public void applyFrictionHorizontally()	{vx = vx*.7;}	//makes you stop in your tracks horizontally
	
	public void applyFrictionVertically()	{vy = vy*.7;}	//makes you stop in your tracks horizontally
	
	public void applyFriction()	
	{
		vx = vx * .7;
		vy = vy * .7;
	}
	
	//------------------------------------------------------//
	
	public boolean groundedOnAny(Rect[] r)
	{
		for(int i = 0; i < r.length; i++)
		{
			if(y + h == r[i].y - 1) return true;
		}
		
		return false;
	}
	
	//------------------------------------------------------//
	
//	public void draw(Graphics pen) 							//draws the rectangle
//	{
//		pen.setColor(c);
//		
//		pen.drawRect((int)x, (int)y, (int)w, (int)h);
//	}
	
	public void draw(Graphics pen)
	{
		pen.setColor(c);
		
		pen.drawRect((int)(x - Camera.x), (int)(y - Camera.y), (int)w, (int)h);
	}
	
	
	//------------------------------------------------------//
}
