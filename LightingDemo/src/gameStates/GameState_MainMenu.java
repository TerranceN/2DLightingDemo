package gameStates;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex2f;
import textures.Texture;
import gameBase.*;
import menus.*;

public class GameState_MainMenu extends GameState_Menu
{
	public GameState_MainMenu(GameWindow window)
	{
		super(window);
	}

	public void Load()
	{
		super.Load();
		
		mMenu.AddItem(new MenuItem_Basic(mMenu, "Play"));
		mMenu.AddItem(new MenuItem_Basic(mMenu, "Polygon Editor"));
		mMenu.AddItem(new MenuItem_Basic(mMenu, "Quit"));
	}
	
	public void OnMenuPressed(String name)
	{
		if (name.equals("Play"))
		{
			mMenu.ResetTransparency();
			mNextState = new GameState_SinglePlayer(mGameWindow);
		}
		else if (name.equals("Polygon Editor"))
		{
			mMenu.ResetTransparency();
			mNextState = new GameState_PolygonEditor(mGameWindow);
		}
		else if (name.equals("Quit"))
		{
			UnLoad();
		}
	}
	
	public void Draw()
	{
		glPushMatrix();
		{
			glLoadIdentity();
			glColor3f(1, 1, 1);
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
		
		super.Draw();
	}
}
