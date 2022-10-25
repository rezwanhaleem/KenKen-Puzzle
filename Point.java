package edu.nyit.csci.cp2.kenken;

public class Point 
{
	private int x;
	private int y;
	
	public Point( int x, int y )
	{
		this.x = x;
		this.y = y;
	}
	
	public Point( Point another )
	{
		x = another.getX();
		y = another.getY();
	}

	public int getX() 
	{
		return x;
	}

	public int getY() 
	{
		return y;
	}
	
	public boolean isNextTo(Point another)
	{
		if( x == another.getX() && y == another.getY())
			return false;
		else if(( Math.abs(x - another.getX()) == 1 && Math.abs(y - another.getY()) == 0) || ( Math.abs(x - another.getX()) == 0 && Math.abs(y - another.getY()) == 1))
			return true;
		else
			return false;
	}
	
	public Point north()
	{
		return new Point(x,y+1);
	}
	
	public Point northEast()
	{
		return new Point(x+1,y+1);
	}
	
	public Point northWest()
	{
		return new Point(x-1,y+1);
	}
	
	public Point east()
	{
		return new Point(x+1,y);
	}
	
	public Point west()
	{
		return new Point(x-1,y);
	}
	
	public Point south()
	{
		return new Point(x,y-1);
	}
	
	public Point southEast()
	{
		return new Point(x+1,y-1);
	}
	
	public Point southWest()
	{
		return new Point(x-1,y-1);
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof Point))
			return false;
		
		Point another = (Point)o;
		if( x == another.getX() && y == another.getY())
			return true;
		else
			return false;
	}
	
	public static double distanceFromOrigin(Point p)
	{
		return Math.sqrt(Math.pow(p.getX(),2)+Math.pow(p.getY(),2));
	}
	
	public String toString()
	{	
		return "( " + x + " , " + y +  " )";
	}
}
