package gameStates;

import levels.*;
import textures.*;
import gameBase.*;
import entities.*;
import lighting.*;
import camera.*;
import util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

public class GameState_Game extends GameState
{
	private Texture mShadowMask;
	private FrameBufferObject mShadowMaskObject;
	
	protected Camera mCamera;
	
	protected Level mLevel;
	
	protected float mAmbientLighting;
	
	public GameState_Game(GameWindow window)
	{
		super(window);
	}
	
	public void Load()
	{
		mCamera = new Camera(GetGameWindow());
		
		Light.Load(mGameWindow.Width(), mGameWindow.Height());
		ComplexPolygon.Load(mGameWindow.Width(), mGameWindow.Height());
		
		mShadowMask = new Texture(mGameWindow.Width(), mGameWindow.Height());

		try
		{
			mShadowMaskObject = new FrameBufferObject(mShadowMask);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		mCamera.ForceScaleFocus((mGameWindow.Width() / 1600.0f + mGameWindow.Height() / 900.0f) / 2);
		
		mAmbientLighting = 0.0f;
	}
	
	public void UnLoad()
	{
		super.UnLoad();
		
		mShadowMaskObject.Delete();
		mShadowMask.Delete();
		
		Light.UnLoad();
		ComplexPolygon.UnLoad();
	}
	
	public Camera GetCamera()
	{
		return mCamera;
	}
	
	public void Update(GameTime gameTime)
	{
		mCamera.Update(gameTime);
	}
	
	public Level GetLevel()
	{
		return mLevel;
	}
	
	public void CalculateLighting()
	{
		mShadowMaskObject.Begin();
		{
			glPushAttrib(GL_COLOR_BUFFER_BIT);
			{
				mShadowMaskObject.Clear();
					
				// additive blending
				glBlendFunc(GL_ONE, GL_ONE);
				
				// Draw lights:
				
				// ambient lighting
				glPushMatrix();
				{
					glLoadIdentity();
					
					glColor4f(1, 1, 1, mAmbientLighting);
					glBegin(GL_QUADS);
					{
						glVertex2f(0, 0);
						glVertex2f(mGameWindow.Width(), 0);
						glVertex2f(mGameWindow.Width(), mGameWindow.Height());
						glVertex2f(0, mGameWindow.Height());
					}
					glEnd();
				}
				glPopMatrix();
				
				Vector2 middleScreen = mCamera.ScreenToWorld(
						new Vector2(mGameWindow.Width(), mGameWindow.Height()).DividedBy(2));
				
				Vector2 topLeft = mCamera.ScreenToWorld(new Vector2(0, 0));
				Vector2 topRight = mCamera.ScreenToWorld(new Vector2(mGameWindow.Width(), 0));
				Vector2 bottomRight = mCamera.ScreenToWorld(new Vector2(mGameWindow.Width(), mGameWindow.Height()));
				Vector2 bottomLeft = mCamera.ScreenToWorld(new Vector2(0, mGameWindow.Height()));
				
				SimplePolygon screen = new SimplePolygon(this, middleScreen);
				screen.AddVertexAbsolute(topLeft);
				screen.AddVertexAbsolute(topRight);
				screen.AddVertexAbsolute(bottomRight);
				screen.AddVertexAbsolute(bottomLeft);
				
				mLevel.DrawLights(screen);
	
				// blend lights with shadow:
				glPushMatrix();
				{
					glLoadIdentity();
					
					glBlendFuncSeparate(GL_ONE, GL_ZERO, GL_ONE_MINUS_DST_ALPHA, GL_ZERO);
					
					glColor3f(0, 0, 0);
					glBegin(GL_QUADS);
					{
						glVertex2f(0, 0);
						glVertex2f(mGameWindow.Width(), 0);
						glVertex2f(mGameWindow.Width(), mGameWindow.Height());
						glVertex2f(0, mGameWindow.Height());
					}
					glEnd();
				}
				glPopMatrix();
			}
			glPopAttrib();
		}
		mShadowMaskObject.End();
	}
	
	public void DrawScene()
	{
		
	}
	
	public void Draw()
	{
		mCamera.Set();
		
		/*mShadowMaskObject.Begin();
		{
			glPushAttrib(GL_COLOR_BUFFER_BIT);
			{
				mShadowMaskObject.Clear();
				
				glPushMatrix();
				{
					glLoadIdentity();
					
					glColor3f(0, 0, 0);
					glBegin(GL_QUADS);
					{
						glVertex2f(0, 0);
						glVertex2f(mGameWindow.Width(), 0);
						glVertex2f(mGameWindow.Width(), mGameWindow.Height());
						glVertex2f(0, mGameWindow.Height());
					}
					glEnd();
				}
				glPopMatrix();
			}
			glPopAttrib();
		}
		mShadowMaskObject.End();*/
		
		CalculateLighting();
		
		DrawScene();
		
		// draw mask over scene to occlude shadows
		glPushMatrix();
		{
			glLoadIdentity();
			
			glColor4f(1, 1, 1, 1);
			Texture.Begin();
			{
				mShadowMask.BindTexture();
				mShadowMask.Draw(0, 0, 1, -1);
			}
			Texture.End();
		}
		glPopMatrix();
	}
}
