package entities;

import entities.*;
import gameStates.*;
import textures.*;
import util.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class ComplexPolygon extends Polygon
{
	private static FrameBufferObject mTempBufferObject;
	private static Texture mTempBuffer;
	
	private LinkedList<SimplePolygon> mPolygons = new LinkedList<SimplePolygon>();
	private LinkedList<SimplePolygon.Edge> mEdges = new LinkedList<SimplePolygon.Edge>();
	
	public static void Load(int width, int height)
	{
		InitTexture(width, height);
		
		try
		{
			if (mTempBufferObject == null)
			{
				mTempBufferObject = new FrameBufferObject();
			}
			
			mTempBufferObject.AssignTexture(mTempBuffer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void InitTexture(int width, int height)
	{
		if (mTempBuffer != null)
		{
			mTempBuffer.Delete();
		}
		
		mTempBuffer = new Texture(width, height);
	}
	
	public static void UnLoad()
	{
		if (mTempBuffer != null)
		{
			mTempBuffer.Delete();
		}
		
		mTempBufferObject.Delete();
	}
	
	public ComplexPolygon(GameState_Game newGameState, Vector2 newPosition)
	{
		super(newGameState, newPosition);
	}
	
	public void AddPolygon(SimplePolygon p, Vector2 p1, Vector2 p2)
	{
		AddPolygon(p);
		AddConnectedEdge(p1, p2);
	}
	
	public void AddPolygon(SimplePolygon p)
	{
		mPolygons.add(p);
	}
	
	public void AddConnectedEdge(Vector2 p1, Vector2 p2)
	{
		mEdges.add(new SimplePolygon.Edge(p1, p2));
	}
	
	public void SetPosition(Vector2 newPosition)
	{
		Vector2 difference = newPosition.Minus(mPosition);
		
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while (i.hasNext())
		{
			SimplePolygon current = i.next();
			
			current.SetPosition(current.GetPosition().Plus(difference));
		}
		
		super.SetPosition(newPosition);
	}
	
	public LinkedList<Vector2> GetAbsoluteVerticies()
	{
		LinkedList<Vector2> verts = new LinkedList<Vector2>();
		
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			verts.addAll(i.next().GetAbsoluteVerticies());
		}
		
		return verts;
	}
	
	public LinkedList<Vector2> GetRelativeVerticies()
	{
		LinkedList<Vector2> verts = new LinkedList<Vector2>();
		
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			verts.addAll(i.next().GetRelativeVerticies());
		}
		
		return verts;
	}
	
	public Rectangle GetBoundingRect()
	{
		float minX = mPolygons.get(0).GetVert(0).X;
		float minY = mPolygons.get(0).GetVert(0).Y;
		float maxX = minX;
		float maxY = minX;
		
		for (int i = 0; i < mPolygons.size(); i++)
		{
			for (int j = 1; i < mPolygons.get(i).GetAbsoluteVerticies().size(); j++)
			{
				Vector2 vert = mPolygons.get(i).GetVert(j);
				
				if (vert.X < minX)
				{
					minX = vert.X;
				}
				else if (vert.X > maxX)
				{
					maxX = vert.X;
				}
				
				if (vert.Y < minY)
				{
					minY = vert.Y;
				}
				else if (vert.Y > maxY)
				{
					maxY = vert.Y;
				}
			}
		}
		
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}
	
	public boolean ShouldExcludeEdge(SimplePolygon.Edge e)
	{
		for (int i = 0; i < mEdges.size(); i++)
		{
			if (e.Equals(mEdges.get(i)))
			{
				return true;
			}
		}
		
		return false;
	}
	public void DrawShadows(Vector2 source, float size)
	{			
		mTempBufferObject.Begin();
		{
			mTempBufferObject.Clear();
			
			glPushAttrib(GL_COLOR_BUFFER_BIT);
			{
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				
				for (int i = 0; i < mPolygons.size(); i++)
				{
					mPolygons.get(i).DrawShadows(source, size);
				}
				
				glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA);
				glColor3f(1, 1, 1);
				
				for (int i = 0; i < mPolygons.size(); i++)
				{
					mPolygons.get(i).Draw();
				}
			}
			glPopAttrib();
		}
		mTempBufferObject.End();
		
		glColor3f(1, 1, 1);
		
		glPushMatrix();
		{
			glLoadIdentity();
			Texture.Begin();
			{
				mTempBuffer.BindTexture();
				mTempBuffer.Draw(0, 0, 1, -1);
			}
			Texture.End();
		}
		glPopMatrix();
	}

	public boolean RectIntersects(Rectangle otherRect)
	{
		for (int i = 0; i < mPolygons.size(); i++)
		{
			if (mPolygons.get(i).RectIntersects(otherRect))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean ContainsPoint(Vector2 point)
	{
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			if (i.next().ContainsPoint(point))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void Draw()
	{
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			i.next().Draw();
		}
	}
	
	public void DrawEdges()
	{
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			i.next().DrawEdges();
		}
	}
	
	public void DrawVerticies()
	{
		ListIterator<SimplePolygon> i = mPolygons.listIterator();
		
		while(i.hasNext())
		{
			i.next().DrawVerticies();
		}
	}
}
