package engine;

import java.awt.*;

public class Rect 
{
	public double x, y, w ,h;								//the x, y, width, and height of a rectangle
	public double halfW, halfH;
	public double vx = 0;									//velocity in the x-direction
	public double vy = 0;									//velocity in the y-direction
	
	Color c = Color.black;									//if no-one tells us otherwise, default color is black
	
	public boolean held = false;
	
	public Rect(int x, int y, int w, int h)					
	{
		setPosition(x, y);	
		setSize(w, h);
	}
	
	public Rect(int x, int y, int w, int h, Color c)
	{
		this(x, y, w, h);
		
		setColor(c);
	}
	
	//checks if the passed mouse coordinates are inside this Rect
	public boolean contains(int mx, int my)
	{														
		return	(
			(mx > this.x		 ) &&
			(mx < this.x + this.w) &&
			
			(my > this.y		 ) &&
			(my < this.y + this.h)
		);
	}
	
	//checks if this Rect overlaps the passed Rect r
	public boolean overlaps(Rect r) 						
	{
		return (
		
			(r.x		<= this.x + this.w)	&&					
			(r.x + r.w  >= this.x		 )	&&					
			
			(r.y		<= this.y + this.h)	&&					
			(r.y + r.h  >= this.y         )						
		
		);
	}
	
	public void physicsOFF()
	{
		this.setVelocity(0, 0);
	}
	
	public void grab() 				{held = true;}
	
	public void drop() 				{held = false;}
	
	public void resizeBy(int dw, int dh)
	{
		w += dw;
		
		h += dh;
	}
	
	public void setSize(double w, double h)
	{
		this.w = w; this.h = h;
		halfW = w/2; halfH = h/2;
	}
	
	public void setX(double x) 		{this.x = x;}
		
	public void setY(double y)		{this.y = y;}
	
	public void setPosition(double x, double y)
	{
		setX(x);
		setY(y);
	}
		
	public void setColor(Color c)	{this.c = c;}
	
	
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
	
	public void moveBy(int dx, int dy)
	{
		x += dx;
		y += dy;
	}
	
	public void move()										//for moving via velocity change
	{
		x += vx;
		y += vy;
	}
	
	public void jump(int dy)								//for moving via velocity change
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
		double topBound    = r.y - h - 6;
		double bottomBound = r.y + r.h + 6;
		double rightBound  = r.x + r.w + 6;
		double leftBound   = r.x - w - 6;
		
		return (topBound <= y && y < bottomBound) && (leftBound <= x && x < rightBound);
	}
	
	public void knockBack(Rect r, int amount)
	{
		if(wasAbove  (r)) r.goDN(amount);
		if(wasBelow  (r)) r.goUP(amount);

		if(wasLeftOf (r)) r.goRT(amount);
		if(wasRightOf(r)) r.goLT(amount);	
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
