package menus;

import input.*;
import gameStates.*;
import bitmapFonts.*;
import textures.*;
import util.Rectangle;

import java.util.*;
import static org.lwjgl.opengl.GL11.*;

public class Menu
{
	private GameState_Menu mGameState;
	private BitmapFont mFont;
	private ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();
	float mScaleX = 1;
	float mScaleY = 1;
	float startingY = 100;
	int mSelected = -1;
	float spacingY = 0;
	Texture t;
	
	public Menu(GameState_Menu newGameState)
	{
		mGameState = newGameState;
		mFont = new BitmapFont("Fonts/Calibri.fnt");
		//spacingY = mFont.StringHeight(mScaleY);
		t = new Texture("Textures/Selector.png");
	}
	
	public void AddItem(MenuItem newItem)
	{
		mMenuItems.add(newItem);
	}
	
	public void ResetTransparency()
	{
		for (int i = 0; i < mMenuItems.size(); i++)
		{
			mMenuItems.get(i).SetTransparency(0);
		}
	}
	
	public BitmapFont GetFont()
	{
		return mFont;
	}
	
	public float GetScaleX()
	{
		return mScaleX;
	}
	
	public float GetScaleY()
	{
		return mScaleY;
	}
	
	private boolean FindSelected(InputHandler input)
	{
		for (int i = 0; i < mMenuItems.size(); i++)
		{
			Rectangle r = mMenuItems.get(i).GetBoundingRect(
					mGameState.GetGameWindow().Width() / 2, startingY + i * (mFont.StringHeight(mScaleY) + spacingY));
			
			if (r.ContainsPoint(input.GetMouseX(), input.GetMouseY()))
			{
				mSelected = i;
				return true;
			}
		}
		
		return false;
	}
	
	public void Update()
	{
		InputHandler input = mGameState.GetGameWindow().GetInputHandler();
		
		if (!FindSelected(input))
		{
			mSelected = -1;
		}
		
		if (input.IsMouseHit(0))
		{
			if (mSelected >= 0)
			{
				mGameState.OnMenuPressed(mMenuItems.get(mSelected).GetName());
			}
		}
		
		for (int i = 0; i < mMenuItems.size(); i++)
		{
			if (i == mSelected)
			{
				mMenuItems.get(i).HoveredOver();
			}
			else
			{
				mMenuItems.get(i).NotHoveredOver();
			}
		}
	}
	
	public void Draw()
	{
		for (int i = 0; i < mMenuItems.size(); i++)
		{
			MenuItem m = mMenuItems.get(i);
			
			float x = mGameState.GetGameWindow().Width() / 2;
			float y = startingY + i * (mFont.StringHeight(mScaleY) + spacingY);
			
			Rectangle r = m.GetBoundingRect(x, y);
			
			float width = r.width;
			float height = r.height;
			
			float x2 = r.x;
			float y2 = r.y;
			
			glColor3f(0, 0, 1);
			
			Texture.End();
			/*glBegin(GL_QUADS);
			{
				glVertex2f(x2, y2);
				glVertex2f(x2 + width, y2);
				glVertex2f(x2 + width, y2 + height);
				glVertex2f(x2, y2 + height);
			}
			glEnd();*/
			
			glColor4f(1, 0, 0, mMenuItems.get(i).GetTransparency());
			t.BindTexture();
			t.Draw(new Rectangle(0, 0, t.Width(), t.Height()),
					r,
					0,
					0,
					0);
			
			glColor3f(1, 1, 1);
			
			mMenuItems.get(i).Draw(x, y);
		}
	}
}
