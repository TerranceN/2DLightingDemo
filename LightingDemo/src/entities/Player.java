package entities;

import java.util.ArrayList;

import gameBase.*;
import gameStates.*;
import input.*;
import util.*;
import textures.*;
import levels.Level;
import lighting.*;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

public class Player extends MoveableEntity
{
	private static Texture mTexture;
	private static Texture mFlashlightTexture;
	private static Texture mCircleLightTexture;
	private final float ACCELERATION = 0.8f;
	
	private Light mLight;
	private Light mFlashlight;
	private ArrayList<Light> mLights = new ArrayList<Light>();
	private SimplePolygon mPoly;
	
	private boolean mFlashLightOn = false;
	
	float mAngle = 0;
	
	public static void Load()
	{
		mTexture = new Texture("Textures/Ship-Outline4.png");
		mFlashlightTexture = new Texture("Textures/LightCone5.png");
		mCircleLightTexture = new Texture("Textures/RadialLight5.png");
	}
	
	public static void UnLoad()
	{
		mTexture.Delete();
		mFlashlightTexture.Delete();
		mCircleLightTexture.Delete();
	}
	
	public Player(GameState_Game newGameState, Vector2 newPosition)
	{
		super(newGameState, newPosition);
		
		mLight = new Light(mCircleLightTexture, new Vector2(), 0.5f, 2.0f, 2.0f);
		mFlashlight = new Light(mFlashlightTexture, new Vector2(), 1, 3, 4.0f);
		
		Level level = mGameState.GetLevel();
		//level.AddLight(mLight);
		
		mPoly = new SimplePolygon(mGameState, mPosition);
		mPoly.AddVertex(new Vector2(-5, -10));
		mPoly.AddVertex(new Vector2(5, -10));
		mPoly.AddVertex(new Vector2(10, -5));
		mPoly.AddVertex(new Vector2(10, 5));
		mPoly.AddVertex(new Vector2(5, 10));
		mPoly.AddVertex(new Vector2(-5, 10));
		mPoly.AddVertex(new Vector2(-10, -5));
		mPoly.AddVertex(new Vector2(-10, 5));
		
		level.AddPolygon(mPoly);
	}
	
	public float GetAngle()
	{
		return mAngle;
	}
	
	public void HandleInput(GameTime gameTime)
	{		
		InputHandler input = mGameWindow.GetInputHandler();
		Level level = mGameState.GetLevel();
		
		Vector2 mousePosition = new Vector2(input.GetMouseX(), input.GetMouseY());
		mousePosition = mGameState.GetCamera().ScreenToWorld(mousePosition);
		
		mAngle = (float)Math.toDegrees(Math.atan2(mousePosition.Y - mPosition.Y, mousePosition.X - mPosition.X)) + 90;
		
		Vector2 direction = new Vector2();
		
		if (input.IsKeyDown(Keyboard.KEY_W))
		{
			direction.PlusEquals(mGameState.GetCamera().GetViewMatrix().Forwards());
		}
		
		if (input.IsKeyDown(Keyboard.KEY_A))
		{
			direction.PlusEquals(mGameState.GetCamera().GetViewMatrix().Left());
		}
		
		if (input.IsKeyDown(Keyboard.KEY_S))
		{
			direction.PlusEquals(mGameState.GetCamera().GetViewMatrix().Backwards());
		}
		
		if (input.IsKeyDown(Keyboard.KEY_D))
		{
			direction.PlusEquals(mGameState.GetCamera().GetViewMatrix().Right());
		}
		
		mVelocity.PlusEquals(direction.GetNormalized().Times(ACCELERATION));
		
		if (input.IsMouseHit(1))
		{
			Light newLight = new Light(mCircleLightTexture, mousePosition, 0.5f, 2, 2);
			mLights.add(newLight);
			level.AddLight(newLight);
		}
		
		if (input.IsKeyHit(Keyboard.KEY_C))
		{
			for (int i = 0; i < mLights.size(); i++)
			{
				level.RemoveLight(mLights.get(i));
			}
			
			mLights.clear();
		}
		
		if (input.IsKeyHit(Keyboard.KEY_T))
		{
			mFlashLightOn = !mFlashLightOn;
			
			if (mFlashLightOn)
			{
				level.AddLight(mFlashlight);
			}
			else
			{
				level.RemoveLight(mFlashlight);
			}
		}
	}

	public void Update(GameTime gameTime)
	{
		HandleInput(gameTime);
		
		Move(gameTime);
		Friction(gameTime, 1.2f);
		
		mLight.SetPosition(mPosition);
		mFlashlight.SetPosition(mPosition.Plus(Vector2.FromAngle((float)Math.toRadians(mAngle - 90)).Times(20)));
		mFlashlight.SetAngle(mAngle);
	}
	
	public void DrawLighting(Rectangle screenRect, ArrayList<SimplePolygon> polygons)
	{
		/*mLight.Draw(screenRect, polygons);
		
		if (mFlashLightOn)
		{
			mFlashlight.Draw(screenRect, polygons);
		}
		
		for (int i = 0; i < mLights.size(); i++)
		{
			mLights.get(i).Draw(screenRect, polygons);
		}*/
	}
	
	public void Draw()
	{
		glColor3f(1, 1, 1);
		
		Texture.Begin();
		{
			mTexture.BindTexture();
			mTexture.Draw(mPosition.X, mPosition.Y, 0.375f, 0.375f, mAngle, 50, 60);
		}
		Texture.End();
	}
}
