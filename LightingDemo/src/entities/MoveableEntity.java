package entities;

import gameBase.*;
import gameStates.*;
import util.*;

public class MoveableEntity extends Entity
{
	protected Vector2 mPosition;
	protected Vector2 mVelocity;
	
	public MoveableEntity(GameState_Game newGameState, Vector2 newPosition)
	{
		super(newGameState);
		mPosition = newPosition;
		mVelocity = new Vector2();
	}
	
	public Vector2 GetPosition()
	{
		return mPosition;
	}
	
	public Vector2 GetVelocity()
	{
		return mVelocity;
	}
	
	public void SetPosition(Vector2 newPosition)
	{
		mPosition.SetEqual(newPosition);
	}
	
	public void SetVelocity(Vector2 newVelocity)
	{
		mVelocity.SetEqual(newVelocity);
	}
	
	public void Move(GameTime gameTime)
	{
		mPosition.PlusEquals(mVelocity.Times(gameTime.GetSpeedFactor()));
	}
	
	public void Friction(GameTime gameTime, float amount)
	{
		mVelocity.DividedByEquals(amount * gameTime.GetSpeedFactor());
	}
}
