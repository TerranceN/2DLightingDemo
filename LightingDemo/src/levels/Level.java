package levels;

import entities.*;
import lighting.*;
import textures.*;
import util.Rectangle;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Level
{
	private LinkedList<Polygon> mPolygons = new LinkedList<Polygon>();
	private LinkedList<Light> mLights = new LinkedList<Light>();
	private LinkedList<BackgroundTexture> mBackgrounds = new LinkedList<BackgroundTexture>();
	
	public Level()
	{
		
	}
	
	public Level(String levelName)
	{
		
	}
	
	public void Load()
	{
		
	}
	
	public void Save()
	{
		
	}
	
	public void Delete()
	{
		ListIterator<BackgroundTexture> i = mBackgrounds.listIterator();
		
		while(i.hasNext())
		{
			BackgroundTexture current = i.next();
			
			current.Delete();
			i.remove();
		}
	}
	
	public LinkedList<Polygon> GetPolygons()
	{
		return mPolygons;
	}
	
	public void AddLight(Light newLight)
	{
		mLights.add(newLight);
	}
	
	public void RemoveLight(Light newLight)
	{
		mLights.remove(newLight);
	}
	
	public void AddPolygon(Polygon newPoly)
	{
		mPolygons.add(newPoly);
	}
	
	public void AddBackground(Texture t, Rectangle r)
	{
		ListIterator<BackgroundTexture> i = mBackgrounds.listIterator();
		
		while(i.hasNext())
		{
			BackgroundTexture current = i.next();
			
			if (current.GetTexture() == t)
			{
				current.AddRect(r);
				return;
			}
		}
		
		BackgroundTexture bt = new BackgroundTexture(t);
		bt.AddRect(r);
		
		mBackgrounds.add(bt);
	}
	
	public void DrawLights(SimplePolygon screen)
	{
		ListIterator<Light> i = mLights.listIterator();
		
		while(i.hasNext())
		{
			i.next().Draw(screen, mPolygons);
		}
	}
	
	public void Draw()
	{
		glColor3f(1, 1, 1);
		{
			ListIterator<BackgroundTexture> i = mBackgrounds.listIterator();
			
			while(i.hasNext())
			{
				i.next().Draw();
			}
		}
		
		glColor3f(0, 1, 0);
		{
			ListIterator<Polygon> i = mPolygons.listIterator();
			
			while(i.hasNext())
			{
				i.next().Draw();
			}
		}
		
		{
			ListIterator<Light> i = mLights.listIterator();
			
			while(i.hasNext())
			{
				i.next().DrawRepresentation();
			}
		}
		
		/*glColor3f(1, 0, 0);
		for (int i = 0; i < mPolygons.size(); i++)
		{
			mPolygons.get(i).DrawEdges();
		}*/
	}
}
