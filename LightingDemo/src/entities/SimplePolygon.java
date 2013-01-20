package entities;

import gameStates.*;
import util.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.*;

public class SimplePolygon extends Polygon
{
	private ComplexPolygon mParent = null;
	
	private LinkedList<Vector2> mVerticies = new LinkedList<Vector2>();
	private LinkedList<Edge> mExcludeEdges = new LinkedList<Edge>();
	
	public SimplePolygon(GameState_Game newGameState, Vector2 newPosition)
	{
		super(newGameState, newPosition);
	}
	
	public void SetParent(ComplexPolygon p)
	{
		mParent = p;
	}
	
	public void AddVertex(Vector2 vert)
	{
		mVerticies.add(vert);
	}
	
	public void AddVertexAbsolute(Vector2 vert)
	{
		mVerticies.add(vert.Minus(mPosition));
	}
	
	public void AddExcludeEdge(Vector2 a, Vector2 b)
	{
		mExcludeEdges.add(new Edge(a, b));
	}
	
	public Vector2 GetVert(int index)
	{
		return mPosition.Plus(mVerticies.get(index));
	}
	
	public boolean ShouldExcludeEdge(Edge e)
	{
		for (int i = 0; i < mExcludeEdges.size(); i++)
		{
			if (e.Equals(mExcludeEdges.get(i)))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public LinkedList<Vector2> GetAbsoluteVerticies()
	{
		LinkedList<Vector2> verts = new LinkedList<Vector2>();
		
		ListIterator<Vector2> i = mVerticies.listIterator();
		
		while(i.hasNext())
		{
			verts.add(mPosition.Plus(i.next()));
		}
		
		return verts;
	}
	
	public LinkedList<Vector2> GetRelativeVerticies()
	{
		return mVerticies;
	}
	
	public Rectangle GetBoundingRect()
	{
		float minX = GetVert(0).X;
		float minY = GetVert(0).Y;
		float maxX = minX;
		float maxY = minX;
		
		for (int i = 1; i < mVerticies.size(); i++)
		{
			Vector2 vert = GetVert(i);
			
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
		
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}
	
	public boolean RectIntersects(Rectangle rect)
	{
		Vector2 previous = GetVert(mVerticies.size() - 1);
		
		for (int i = 0; i < mVerticies.size(); i++)
		{
			Vector2 current = GetVert(i);
			CollisionLine line = new CollisionLine(previous, current);
			
			if (rect.ContainsPoint(current.X, current.Y))
			{
				return true;
			}
			
			if (rect.IntersectsWith(line))
			{
				return true;
			}
			
			previous = current;
		}
		
		if (ContainsPoint(new Vector2(rect.x + rect.width / 2, rect.y + rect.height / 2)))
		{			
			return true;
		}

		return false;
	}
	
	public boolean ContainsPoint(Vector2 point)
	{
		Vector2 previous = GetVert(mVerticies.size() - 1);
		
		for (int i = 0; i < mVerticies.size(); i++)
		{
			Vector2 current = GetVert(i);
			Line line = new Line(previous, current);
			
			if (line.WhatSide(point) != line.WhatSide(mPosition))
			{
				return false;
			}
			
			previous = current;
		}
		
		
		return true;
	}
	
	public void DrawShadows(Vector2 source, float size)
	{
		if (mVerticies.size() >= 3)
		{
			ArrayList<Edge> shadowEdges = new ArrayList<Edge>();
			
			Vector2 last = GetVert(mVerticies.size() - 1);
			Vector2 lastOriginal = mVerticies.get(mVerticies.size() - 1);
			
			for (int i = 0; i < mVerticies.size(); i++)
			{
				Vector2 current = GetVert(i);
				
				if (last != null)
				{
					Edge e = new Edge(lastOriginal, mVerticies.get(i));
					
					boolean shouldDrawEdge = true;
					
					if (mParent != null)
					{
						shouldDrawEdge = !mParent.ShouldExcludeEdge(e);
					}
					
					if (shouldDrawEdge)
					{
						Line edge = new Line(last, current);
						
						Vector2 difference = current.Minus(last);
						
						Vector2 midpoint = current.Plus(last).DividedBy(2);
						
						Vector2 normal = new Vector2(-difference.Y, difference.X);
						
						if (edge.WhatSide(mPosition) == edge.WhatSide(midpoint.Plus(normal)))
						{
							normal = new Vector2(difference.Y, -difference.X);
						}
						
						Vector2 lightRay = midpoint.Minus(source);
						
						if (normal.Dot(lightRay) > 0)
						{
							shadowEdges.add(new Edge(last, current));
						}
					}
				}
				
				last = current;
				lastOriginal = mVerticies.get(i);
			}
			
			glColor3f(1, 1, 1);
			
			glBegin(GL_QUADS);
			{
				for (int i = 0; i < shadowEdges.size(); i++)
				{
					Edge current = shadowEdges.get(i);
					
					Vector2 midpoint = current.point1.Plus(current.point2).DividedBy(2);
					Vector2 lightRay = midpoint.Minus(source);
					
					float shadowLength = size - lightRay.Length();
					
					if (shadowLength > 0)
					{
						Vector2 lightRay1 = current.point1.Minus(source);
						Vector2 result1 = current.point1.Plus(lightRay1.Times(shadowLength));
						
						Vector2 lightRay2 = current.point2.Minus(source);
						Vector2 result2 = current.point2.Plus(lightRay2.Times(shadowLength));
						
						glVertex2f(current.point1.X, current.point1.Y);
						glVertex2f(result1.X, result1.Y);
						glVertex2f(result2.X, result2.Y);
						glVertex2f(current.point2.X, current.point2.Y);
					}
				}
			}
			glEnd();
		}
	}
	
	public void Draw()
	{
		glBegin(GL_POLYGON);
		{
			for (int i = 0; i < mVerticies.size(); i++)
			{
				Vector2 vertex = GetVert(i);
				
				glVertex2f(vertex.X, vertex.Y);
			}
		}
		glEnd();
	}
	
	public void DrawVerticies()
	{
		glColor3f(1, 0.549019608f, 0);
		DrawVertex(mPosition);
		
		glColor3f(0.1f, 0.1f, 1);
		for (int i = 0; i < mVerticies.size(); i++)
		{
			DrawVertex(GetVert(i));
		}
		
	}
	
	public void DrawVertex(Vector2 vertex)
	{
		float size = 5 / mGameState.GetCamera().GetScale();
		
		glPushMatrix();
		{
			glTranslatef(vertex.X, vertex.Y, 0);
			
			glBegin(GL_QUADS);
			{
				glVertex2f(-size, -size);
				glVertex2f(size, -size);
				glVertex2f(size, size);
				glVertex2f(-size, size);
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	private void AddGLVertex(Vector2 p)
	{
		glVertex2f(p.X, p.Y);
	}
	
	private void DrawThickLine(Vector2 p1, Vector2 p2, float thickness)
	{
		glBegin(GL_QUADS);
		{
			Vector2 diff = p1.Minus(p2);
			Vector2 offset = new Vector2(diff.Y, -diff.X).GetNormalized().Times(thickness / 2);
			
			AddGLVertex(p1.Plus(offset));
			AddGLVertex(p1.Minus(offset));
			AddGLVertex(p2.Minus(offset));
			AddGLVertex(p2.Plus(offset));
		}
		glEnd();
	}
	
	public void DrawEdges()
	{
		Vector2 last = GetVert(mVerticies.size() - 1);
		
		ListIterator<Vector2> i = mVerticies.listIterator();
		while (i.hasNext())
		{
			Vector2 vertex = mPosition.Plus(i.next());
			
			DrawThickLine(vertex, last, 2 / mGameState.GetCamera().GetScale());
			
			last = vertex;
		}
	}
	
	public static class Edge
	{
		public Vector2 point1;
		public Vector2 point2;
		
		public Edge(Vector2 first, Vector2 second)
		{
			point1 = first;
			point2 = second;
		}
		
		public boolean Equals(Edge other)
		{
			return (point1.Equals(other.point1) && point2.Equals(other.point2))
				|| (point1.Equals(other.point2) && point2.Equals(other.point1));
		}
	}
}
