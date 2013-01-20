package gameStates;

import input.*;
import gameBase.*;
import entities.*;
import levels.*;
import util.*;
import textures.*;

import java.util.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.*;

import static org.lwjgl.opengl.GL11.*;

public class GameState_PolygonEditor extends GameState_Game
{	
	private enum EditType
	{
		Polygon,
		Vertex
	}
	
	private enum EditOperation
	{
		Select,
		Move
	}
	
	private boolean mIsChanged = false;
	
	private Rectangle mSelectRect = null;
	private Vector2 mMouseClickPosition = new Vector2();
	private LinkedList<Polygon> mSelectedPolys;
	private LinkedList<Polygon> mAreaSelectedPolys;
	private EditType mEditType;
	private EditOperation mEditOperation;
	private Texture mMoveTexture;
	private Texture mSelectTexture;
	
	public GameState_PolygonEditor(GameWindow window)
	{
		super(window);
	}

	public void Load()
	{
		super.Load();
		
		mMoveTexture = new Texture("Textures/Editor/MoveTexture.png");
		mSelectTexture = new Texture("Textures/Editor/SelectTexture.png");
		
		mEditType = EditType.Polygon;
		mEditOperation = EditOperation.Select;
		
		mLevel = new Level();
		mSelectedPolys = new LinkedList<Polygon>();
		mAreaSelectedPolys = new LinkedList<Polygon>();
		
		ComplexPolygon cPoly = new ComplexPolygon(this, new Vector2(75, 200));
		SimplePolygon poly = new SimplePolygon(this, new Vector2(0, 0));
		poly.AddVertex(new Vector2(-25, -25));
		poly.AddVertex(new Vector2(0, -25));
		poly.AddVertex(new Vector2(25, 0));
		poly.AddVertex(new Vector2(25, 25));
		poly.AddVertex(new Vector2(-5, 5));
		mLevel.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(50, 100));
		poly.AddVertex(new Vector2(-25, -25));
		poly.AddVertex(new Vector2(25, -25));
		poly.AddVertex(new Vector2(25, 25));
		poly.AddVertex(new Vector2(-25, 25));
		mLevel.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(275, 125));
		poly.AddVertex(new Vector2(-25, -250));
		poly.AddVertex(new Vector2(25, -250));
		poly.AddVertex(new Vector2(25, 300));
		poly.AddVertex(new Vector2(-25, 250));
		cPoly.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(0, 400));
		poly.AddVertex(new Vector2(-250, -25));
		poly.AddVertex(new Vector2(250, -25));
		poly.AddVertex(new Vector2(300, 25));
		poly.AddVertex(new Vector2(-250, 25));
		cPoly.AddPolygon(poly);
		cPoly.AddConnectedEdge(new Vector2(250, -25), new Vector2(300, 25));
		
		mLevel.AddPolygon(cPoly);
		
		mAmbientLighting = 1.0f;
		
		ZoomExtents();
	}
	
	public void UnLoad()
	{
		super.UnLoad();
		
		mLevel.Delete();
		mMoveTexture.Delete();
		mSelectTexture.Delete();
	}
	
	private Rectangle BoundingRectOfPoints(LinkedList<Vector2> points)
	{
		Rectangle r = new Rectangle();
		
		if (points.size() > 0)
		{
			Vector2 min = new Vector2(points.get(0).X, points.get(0).Y);
			Vector2 max = min.Copy();
			
			ListIterator<Vector2> i = points.listIterator();
			
			while(i.hasNext())
			{
				Vector2 current = i.next();
				
				if (current.X < min.X)
				{
					min.X = current.X;
				}
				else if (current.X > max.X)
				{
					max.X = current.X;
				}
				
				if (current.Y < min.Y)
				{
					min.Y = current.Y;
				}
				else if (current.Y > max.Y)
				{
					max.Y = current.Y;
				}
			}
			
			Vector2 diff = max.Minus(min);
			
			r = new Rectangle(min.X, min.Y, diff.X, diff.Y);
		}
			
		return r;
	}
	
	public void ZoomExtents()
	{
		Rectangle r = null;
		LinkedList<Vector2> verticies = new LinkedList<Vector2>();
		
		switch(mEditType)
		{
			case Polygon:
			{				
				ListIterator<Polygon> i = mSelectedPolys.listIterator();
				
				while(i.hasNext())
				{
					verticies.addAll(i.next().GetAbsoluteVerticies());
				}
			}			
			break;
		}
		
		if (verticies.size() == 0)
		{
			LinkedList<Polygon> polygons = mLevel.GetPolygons();
			
			ListIterator<Polygon> i = polygons.listIterator();
			
			while (i.hasNext())
			{
				verticies.addAll(i.next().GetAbsoluteVerticies());
			}			
		}
		
		r = BoundingRectOfPoints(verticies);
		
		float scaleX = mGameWindow.Width() / r.width;
		float scaleY = mGameWindow.Height() / r.height;
		
		if (scaleX > scaleY)
		{
			mCamera.ForceScaleFocus(scaleY * 0.9f);
		}
		else
		{
			mCamera.ForceScaleFocus(scaleX * 0.9f);
		}
		
		mCamera.ForceTranslationFocus(new Vector2(r.x + r.width / 2, r.y + r.height / 2));
		mCamera.ForceRotationFocus(0);
	}
	
	public boolean PointInAnyPoly(Vector2 point)
	{
		ListIterator<Polygon> i = mLevel.GetPolygons().listIterator();
		
		while(i.hasNext())
		{
			Polygon current = i.next();
			
			if (current.ContainsPoint(point))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void HandlePolygonSelection(Vector2 mousePos)
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		if (input.IsMouseHit(0))
		{
			boolean hitPoly = false;
			
			ListIterator<Polygon> i = mLevel.GetPolygons().listIterator();
			
			while(i.hasNext())
			{
				Polygon current = i.next();
				
				if (current.ContainsPoint(mousePos))
				{
					if (!input.IsKeyDown(Keyboard.KEY_LCONTROL))
					{
						mSelectedPolys.clear();
						mSelectedPolys.add(current);
					}
					else
					{
						if(!mSelectedPolys.contains(current))
						{
							mSelectedPolys.add(current);
						}
						else
						{
							mSelectedPolys.remove(current);
						}
					}
					
					hitPoly = true;
				}
			}
			
			if (!hitPoly && !input.IsKeyDown(Keyboard.KEY_LCONTROL))
			{
				mSelectedPolys.clear();
			}
			
			mMouseClickPosition = mousePos;
		}
		else if (input.IsMouseDown(0))
		{
			Vector2 diff = mousePos.Minus(mMouseClickPosition);
			
			if (diff.Length() > 5 / mCamera.GetScale())
			{				
				if (!(diff.X == 0 && diff.Y == 0))
				{
					mAreaSelectedPolys.clear();
					
					mSelectRect = new Rectangle(mMouseClickPosition.X, mMouseClickPosition.Y, diff.X, diff.Y);
					
					mSelectRect.Normalize();
					
					ListIterator<Polygon> i = mLevel.GetPolygons().listIterator();
					
					while(i.hasNext())
					{
						Polygon current = i.next();
						
						if (current.RectIntersects(mSelectRect))
						{							
							mAreaSelectedPolys.add(current);
						}
					}
				}
			}
		}
		else
		{
			if (mAreaSelectedPolys.size() > 0)
			{
				ListIterator<Polygon> i = mAreaSelectedPolys.listIterator();
				
				while(i.hasNext())
				{
					Polygon current = i.next();
					
					if (!mSelectedPolys.contains(current))
					{
						mSelectedPolys.add(current);
					}
				}
				
				mAreaSelectedPolys.clear();
			}
			mSelectRect = null;
		}
		
	}
	
	public boolean HandleVertexSelection(Vector2 mousePos)
	{
		LinkedList<Vector2> verticies = new LinkedList<Vector2>();
		
		{
			ListIterator<Polygon> i = mSelectedPolys.listIterator();
			
			while (i.hasNext())
			{
				verticies.addAll(i.next().GetAbsoluteVerticies());
			}
		}
		
		{
			ListIterator<Vector2> i = verticies.listIterator();
			
			while (i.hasNext())
			{
				Vector2 difference = i.next().Minus(mousePos);
				
				if (difference.Length() < 10 / mCamera.GetScale())
				{					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void MovePolygons(Vector2 mousePos)
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		if (input.IsMouseHit(0))
		{
			mMouseClickPosition = mousePos;
			
			if (!input.IsKeyDown(Keyboard.KEY_LMENU))
			{
				if (!PointInAnyPoly(mousePos))
				{
					if (!input.IsKeyDown(Keyboard.KEY_LCONTROL))
					{
						mSelectedPolys.clear();
					}
					
					mEditOperation = EditOperation.Select;
				}
			}
		}
		else if (input.IsMouseDown(0))
		{
			if (!mIsChanged)
			{
				mIsChanged = true;
				Display.setTitle(Display.getTitle() + "*");
			}
			
			Vector2 difference = mousePos.Minus(mMouseClickPosition);
			
			ListIterator<Polygon> i = mSelectedPolys.listIterator();
			
			while (i.hasNext())
			{
				Polygon current = i.next();
				
				current.SetPosition(current.GetPosition().Plus(difference));
			}
			
			mMouseClickPosition = mousePos;
		}
	}
	
	public void HandleInput()
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		if (input.IsKeyDown(Keyboard.KEY_ESCAPE))
		{
			UnLoad();
		}
		
		if (input.IsKeyDown(Keyboard.KEY_LCONTROL))
		{
			if (input.IsKeyHit(Keyboard.KEY_S) && mIsChanged)
			{
				mLevel.Save();
				mIsChanged = false;
				Display.setTitle(Display.getTitle().substring(0, Display.getTitle().length() - 1));
			}
		}
		
		if (input.IsKeyHit(Keyboard.KEY_Z))
		{
			ZoomExtents();
		}
		
		if (input.IsKeyDown(Keyboard.KEY_2))
		{
			mEditOperation = EditOperation.Move;
		}
		
		if (input.IsKeyDown(Keyboard.KEY_1))
		{
			mEditOperation = EditOperation.Select;
		}
		
		Vector2 mousePos = mCamera.ScreenToWorld(new Vector2(input.GetMouseX(), input.GetMouseY()));
		
		if (input.IsMouseHit(1))
		{
			mMouseClickPosition = mousePos;
		}
		else if (input.IsMouseDown(1))
		{
			Vector2 difference = mMouseClickPosition.Minus(mousePos);
			mCamera.SetTranslationFocus(mCamera.GetTranslation().Plus(difference));
		}
		
		int mouseWheel = input.GetMouseDeltaWheel();
		
		if (mouseWheel != 0)
		{
			mCamera.SetScaleFocus(mCamera.GetScale() * (float)Math.pow(2, mouseWheel));
		}
		
		switch (mEditOperation)
		{
			case Select:
			{
				switch (mEditType)
				{
					case Polygon:
					{
						HandlePolygonSelection(mousePos);
					}
					break;
				}
			}
			break;
			case Move:
			{
				switch (mEditType)
				{
					case Polygon:
					{
						MovePolygons(mousePos);
					}
					break;
				}
			}
			break;
		}
	}
	
	public void Update(GameTime gameTime)
	{
		HandleInput();

		super.Update(gameTime);
	}
	
	public void DrawScene()
	{
		glColor3f(1, 1, 1);
		mLevel.Draw();
		
		switch (mEditType)
		{
		case Polygon:
		{		
			glColor3f(1, 1, 1);
			
			{
				ListIterator<Polygon> i = mSelectedPolys.listIterator();
				
				while (i.hasNext())
				{
					i.next().DrawEdges();
				}
			}
			
			{
				ListIterator<Polygon> i = mAreaSelectedPolys.listIterator();
				
				while (i.hasNext())
				{
					i.next().DrawEdges();
				}
			}
		}
		break;
		}
		
		if (mSelectRect != null)
		{
			glColor4f(1, 1, 1, 0.4f);
			mSelectRect.Fill();
			
			glColor3f(1, 1, 1);
			mSelectRect.Draw();
		}
		
		glPushMatrix();
		{
			glLoadIdentity();
			
			Texture.Begin();
			{
				switch (mEditOperation)
				{
					case Select:
					{
						mSelectTexture.BindTexture();
						mSelectTexture.Draw(10, 10);
					}
					break;
					case Move:
					{
						mMoveTexture.BindTexture();
						mMoveTexture.Draw(10, 10);
					}
					break;
				}
			}
			Texture.End();
		}
		glPopMatrix();
	}
}
