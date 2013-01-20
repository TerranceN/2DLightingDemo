package entities;

import gameBase.*;
import gameStates.*;

import java.util.*;

public abstract class Entity
{
	protected GameWindow mGameWindow;
	protected GameState_Game mGameState;
	
	public Entity(GameState_Game newGameState)
	{
		mGameState = newGameState;
		mGameWindow = mGameState.GetGameWindow();
	}
	
	public static void UpdateEntityList(ArrayList<Entity> list, GameTime gameTime)
	{
		for (int i = list.size() - 1; i >= 0; i--)
		{
			Entity e = list.get(i);
			
			e.Update(gameTime);
			
			if (!e.Alive())
			{
				list.remove(e);
			}
		}
	}
	
	public void DrawEntityList(ArrayList<Entity> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Entity e = list.get(i);
			
			if (e.Alive())
			{
				e.Draw();
			}
		}
	}
	
	public void DrawLightingEntityList(ArrayList<Entity> list, ArrayList<SimplePolygon> polygons)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Entity e = list.get(i);
			
			if (e.Alive())
			{
				e.DrawLighting(polygons);
			}
		}
	}
	
	private boolean mIsAlive = true;
	
	public void Kill()
	{
		mIsAlive = false;
	}
	
	public boolean Alive()
	{
		return mIsAlive;
	}
	
	public void Update(GameTime gameTime)
	{
		
	}
	
	public void Draw()
	{
		
	}
	
	public void DrawLighting(ArrayList<SimplePolygon> polygons)
	{
		
	}
}
