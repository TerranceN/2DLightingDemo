package lighting;

import java.util.*;

import entities.*;
import gameStates.*;
import textures.*;
import util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class Light
{
	private static FrameBufferObject mTempBufferObject;
	private static Texture mTempBuffer;
	
	private GameState_Game mGame;
	
	private Texture mLightTexture;
	private Vector2 mPosition;
	private float mScaleX;
	private float mScaleY;
	private float mAngle;
	private float mPivotX;
	private float mPivotY;
	private float mIntensity;
	
	public static void Begin()
	{
		mTempBufferObject.Begin();
	}
	
	public static void End()
	{
		mTempBufferObject.End();
	}
	
	public static void LoadBuffer(int width, int height) throws Exception
	{
		if (mTempBuffer != null)
			mTempBuffer.Delete();
		
		mTempBuffer = new Texture(width, height);
		mTempBufferObject.AssignTexture(mTempBuffer);
	}
	
	public static void Load(int width, int height)
	{
		try
		{
			mTempBufferObject = new FrameBufferObject();
			LoadBuffer(width, height);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void UnLoad()
	{
		mTempBufferObject.Delete();
		mTempBuffer.Delete();
	}
	
	public Light(Texture newTexture, Vector2 position)
	{
		this(newTexture, position, 0.0f, 1, 1);
	}
	
	public Light(Texture newTexture, Vector2 position, float newIntensity)
	{
		this(newTexture, position, 0.0f, 1, 1, 0);
	}
	
	public Light(Texture newTexture, Vector2 position, float newIntensity, float newScaleX, float newScaleY)
	{
		this(newTexture, position, newIntensity, newScaleX, newScaleY, 0);
	}
	
	public Light(Texture newTexture, Vector2 position, float newIntensity, float newScaleX, float newScaleY, float newAngle)
	{
		mPosition = position;
		mLightTexture = newTexture;
		mScaleX = newScaleX;
		mScaleY = newScaleY;
		mAngle = newAngle;
		mPivotX = mLightTexture.Width() / 2;
		mPivotY = mLightTexture.Height() / 2;
		mIntensity = newIntensity;
	}
	
	public void SetPosition(Vector2 newPosition)
	{
		mPosition.SetEqual(newPosition);
	}
	
	public void SetAngle(float newAngle)
	{
		mAngle = newAngle;
	}
	
	public void SetIntensity(float newIntensity)
	{
		mIntensity = newIntensity;
	}
	
	public Rectangle GetRect()
	{
		return new Rectangle(mPosition.X - mPivotX * mScaleX,
				mPosition.Y - mPivotY * mScaleY,
				2 * mPivotX * mScaleX,
				2 * mPivotY * mScaleY);
	}
	
	public void ComputeShadows(LinkedList<Polygon> polygons)
	{		
		mTempBufferObject.Begin();
		{
			mTempBufferObject.Clear();
			
			glPushAttrib(GL_COLOR_BUFFER_BIT);
			{
				// additive blending
				glBlendFunc(GL_ONE, GL_ONE);
				
				glColor3f(1, 1, 1);
				Texture.Begin();
				{
					mLightTexture.BindTexture();
					mLightTexture.Draw(mPosition.X, mPosition.Y, mScaleX, mScaleY, mAngle, mPivotX, mPivotY);
				}
				Texture.End();
				
				// TODO: calculate shadows
				// TODO: occlude shadows using this blending function
				glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ZERO, GL_ONE_MINUS_SRC_ALPHA);
				
				glColor3f(1, 1, 1);
				
				ListIterator<Polygon> i = polygons.listIterator();
				
				while(i.hasNext())
				{
					Polygon current = i.next();
					
					if (current.RectIntersects(GetRect()))
					{
						current.DrawShadows(mPosition, mLightTexture.Width() * mScaleX);
					}
				}
			}
			glPopAttrib();
		}
		mTempBufferObject.End();
	}

	public void DrawLight()
	{
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
	
	public void Draw(SimplePolygon screen, LinkedList<Polygon> polygons)
	{
		if (screen.RectIntersects(GetRect()))
		{
			ComputeShadows(polygons);
			glColor4f(1, 1, 1, mIntensity);
			DrawLight();
		}
	}
	
	public void DrawRepresentation()
	{
		float radius = 10;
		int sides = 10;
		float angle = (float)Math.PI * 2 / sides;
		
		glColor3f(1, 0, 0);
		
		glBegin(GL_TRIANGLE_FAN);
		{
			glVertex2f(mPosition.X, mPosition.Y);
			for(int i = 0; i < sides; i++)
			{
				glVertex2f(mPosition.X + (float)Math.cos(angle * i) * radius, mPosition.Y + (float)Math.sin(angle * i) * radius);
			}
			glVertex2f(mPosition.X + radius, mPosition.Y);
		}
		glEnd();
	}
}
