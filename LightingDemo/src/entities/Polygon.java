package entities;

import java.util.LinkedList;

import gameStates.*;
import textures.*;
import util.*;

public abstract class Polygon extends MoveableEntity
{
	public Polygon(GameState_Game newGameState, Vector2 newPosition)
	{
		super(newGameState, newPosition);
	}

	public abstract void DrawShadows(Vector2 source, float size);
	
	public abstract boolean RectIntersects(Rectangle otherRect);
	
	public abstract LinkedList<Vector2> GetRelativeVerticies();
	
	public abstract LinkedList<Vector2> GetAbsoluteVerticies();
	
	public abstract Rectangle GetBoundingRect();
	
	public abstract boolean ContainsPoint(Vector2 point);
	
	public abstract void DrawEdges();
	
	public abstract void DrawVerticies();
}
