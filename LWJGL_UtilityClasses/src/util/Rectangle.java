package util;

import static org.lwjgl.opengl.GL11.*;
import java.util.*;

public class Rectangle
{
	public float x = 0;
	public float y = 0;
	public float width = 0;
	public float height = 0;
	
	public Rectangle()
	{
	}
	
	public Rectangle(float newX, float newY, float newWidth, float newHeight)
	{
		x = newX;
		y = newY;
		width = newWidth;
		height = newHeight;
	}
	
	public void Normalize()
	{
		if (width < 0)
		{
			width = -width;
			x -= width;
		}
		
		if (height < 0)
		{
			height = -height;
			y -= height;
		}
	}
	
	public Rectangle Copy()
	{
		return new Rectangle(x, y, width, height);
	}
	
	public boolean ContainsPoint(float px, float py)
	{
		return ((px >= x)
				&& (px <= x + width)
				&& (py >= y)
				&& (py <= y + height));
	}
	
	public boolean IntersectsWith(Rectangle other)
	{
		return ((x <= other.x + other.width && x + width > other.x)
				&& (y <= other.y + other.height && y + height > other.y));
	}
	
	public boolean IntersectsWith(CollisionLine other)
	{
		ArrayList<CollisionLine> lines = new ArrayList<CollisionLine>();
		Vector2 topLeft = new Vector2(x, y);
		Vector2 topRight = new Vector2(x + width, y);
		Vector2 bottomRight = new Vector2(x + width, y + height);
		Vector2 bottomLeft = new Vector2(x, y + height);
		
		lines.add(new CollisionLine(topLeft, topRight));
		lines.add(new CollisionLine(topRight, bottomRight));
		lines.add(new CollisionLine(bottomRight, bottomLeft));
		lines.add(new CollisionLine(bottomLeft, topLeft));
		
		for (int i = 0; i < lines.size(); i++)
		{
			if (other.GetIntersectionWithLine(lines.get(i)) != null)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void Draw()
	{
		glBegin(GL_LINES);
		{
			glVertex2f(x, y);
			glVertex2f(x + width, y);
			
			glVertex2f(x + width, y);
			glVertex2f(x + width, y + height);
			
			glVertex2f(x + width, y + height);
			glVertex2f(x, y + height);
			
			glVertex2f(x, y + height);
			glVertex2f(x, y);
		}
		glEnd();
	}
	
	public void Fill()
	{
		glBegin(GL_QUADS);
		{
			glVertex2f(x, y);
			glVertex2f(x + width, y);
			glVertex2f(x + width, y + height);
			glVertex2f(x, y + height);
		}
		glEnd();
	}
}
