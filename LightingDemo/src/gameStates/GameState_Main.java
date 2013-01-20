package gameStates;

import gameBase.*;
import input.*;
import textures.*;
import util.Rectangle;
import bitmapFonts.*;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

public class GameState_Main extends GameState
{
	Texture t;
	BitmapFont f;
	float x = 1;
	
	public GameState_Main(GameWindow window)
	{
		super(window);
	}
	
	public void Load()
	{
		t = new Texture("TextureTest.png");
		f = new BitmapFont("Calibri.fnt");
	}

	public void Update(GameTime gameTime)
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		if (input.IsKeyHit(Keyboard.KEY_ESCAPE))
		{
			UnLoad();
		}
		
		x++;
	}
	
	public void Draw()
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		/*glColor3f(0, 0, 0.5f);
		
		glBegin(GL_QUADS);
		{
			float x = 400;
			float y = 100;
			float width = f.StringWidth("Hello, World!", 0.5f);
			float height = f.StringHeight(0.5f);
			
			glVertex2f(x, y);
			glVertex2f(x + width, y);
			glVertex2f(x + width, y + height);
			glVertex2f(x, y + height);
		}
		glEnd();*/
		
		glColor3f(1, 1, 1);
		Texture.Begin();
		{
			t.BindTexture();
			//t.Draw(50, 50, 0.5f, 0.5f, x, 0, 0);
			//t.Draw(0, 0, 1, 1, 0, 0, 0);
			//t.Draw(50, 50, 0.5f, 0.5f, x, t.Width() / 2, t.Height() / 2);
			t.Draw(new Rectangle(t.Width() / 4, t.Height() / 4, t.Width() / 2, t.Height() / 2), new Rectangle(64, 64, 256, 256), x, 256, 256);
			//t.Draw(new Rectangle(0, 0, t.Width(), t.Height()), new Rectangle(0, 0, t.Width(), t.Height()), 0, 0, 0);
			/*f.DrawString("Hello, World!",
					400,
					100,
					0.5f,
					0.5f);*/
		}
		Texture.End();
	}
}
