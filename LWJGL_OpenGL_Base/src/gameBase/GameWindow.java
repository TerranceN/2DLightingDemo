package gameBase;

import input.*;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Handles the game loop and game-states. Entry point for the game.
 * @author Terry
 */
public class GameWindow
{
	private GameTime mGameTime;
	private boolean mIsRunning = true;
	
	private int mFPSCap = 60;
	private boolean mIsFPSCapped = true;
	
	boolean mIsFullscreen = false;
	
	InputHandler mInput;
	
	private int mWindowWidth = 800;
	private int mWindowHeight = 600;
	
	protected Stack<GameState> gameStates = new Stack<GameState>();
	
	public void ClearScreen()
	{
		glColor3f(0.3f, 0.3f, 0.5f);
		glBegin(GL_QUADS);
		{
			glVertex2f(0, 0);
			glVertex2f(mWindowWidth, 0);
			glVertex2f(mWindowWidth, mWindowHeight);
			glVertex2f(0, mWindowHeight);
		}
		glEnd();
	}
	
	/**
	 * Runs game loop. Handles the creation and deletion of new states to the stack.
	 */
	public void Run()
	{
		// make sure game started properly
		if (Initialize())
		{
			while(mIsRunning)
			{
				// handles window events
				Display.update(true);
				
				mGameTime.Update();
				
				while (Keyboard.next())
				{
					mInput.HandleKeyboardEvent();
					gameStates.peek().HandleKeyboardEvent();
				}
				
				while (Mouse.next())
				{
					mInput.HandleMouseEvent();
					gameStates.peek().HandleMouseEvent();
				}
				
				// if the top gamestate is alive, update, otherwise remove it
				if (gameStates.peek().IsAlive() != false)
				{
					gameStates.peek().Update(mGameTime);
				}
				
				if (gameStates.peek().IsAlive() == false)
				{
					gameStates.pop();
					
					// if there are no more game-states, end the game, otherwise update the new top
					if (!gameStates.empty())
					{
						gameStates.peek().Update(mGameTime);
					}
					else
					{
						mIsRunning = false;
						break;
					}
				}
				
				// clear screen and set modelview matrix
				glClear(GL_COLOR_BUFFER_BIT);
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				ClearScreen();
				
				// draw top gamestate
				gameStates.peek().Draw();
				
				if (mIsFPSCapped)
				{
					// delay for constant framerate
					Display.sync(mFPSCap);
				}
				
				// handle close message
				if (Display.isCloseRequested())
				{
					mIsRunning = false;
				}
				
				// handle adding a new state if current state requests a new state
				if (gameStates.peek().IsNextState())
				{
					gameStates.push(gameStates.peek().TakeNextState());
				}
			}
		}
		else
		{
			System.out.println("Failed To Initialize!");
		}
		
		UnLoad();
		
		// close display
		Display.destroy();
		
		System.exit(0);
	}
	
	/**
	 * Sets up game.
	 * @return Whether errors occurred.
	 */
	protected boolean Initialize()
	{
		// Scale game speed as if running at 60 fps
		mGameTime = new GameTime(60);
		
		mInput = new InputHandler(this);
		
		LoadGraphicsSettings();
		
		return UpdateWindow()
			&& InitOpenGL()
			&& Load();
	}
	
	private boolean InitOpenGL()
	{
		glClearColor(0, 0, 0, 1);
		
		glDisable(GL_DEPTH_TEST);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		return glGetError() == GL_NO_ERROR;
	}
	
	protected boolean Load()
	{
		return true;
	}
	
	protected void UnLoad()
	{
		
	}
	
	public boolean LoadGraphicsSettings()
	{
		try
		{
			Scanner s = new Scanner(new File("window_settings.txt"));
			
			int width = Integer.parseInt(s.nextLine());
			int height = Integer.parseInt(s.nextLine());
			boolean fullscreen = Boolean.parseBoolean(s.nextLine());
			int FPS = Integer.parseInt(s.nextLine());
			boolean isFPSCap = Boolean.parseBoolean(s.nextLine());
			
			mWindowWidth = width;
			mWindowHeight = height;
			mIsFullscreen = fullscreen;
			mFPSCap = FPS;
			mIsFPSCapped = isFPSCap;
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean SaveGraphicsSettings()
	{
		try
		{
			FileWriter fWriter = new FileWriter("window_settings.txt");
			PrintWriter pWriter = new PrintWriter(fWriter);
			
			pWriter.println(Integer.toString(mWindowWidth));
			pWriter.println(Integer.toString(mWindowHeight));
			pWriter.println(Boolean.toString(mIsFullscreen));
			pWriter.println(Integer.toString(mFPSCap));
			pWriter.println(Boolean.toString(mIsFPSCapped));
			
			pWriter.close();
			fWriter.close();
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean UpdateWindow()
	{
		try
		{
			DisplayMode dm = null;
			DisplayMode[] dms = Display.getAvailableDisplayModes();
			
			for (int i = 0; i < dms.length; i++)
			{				
				if (dms[i].getWidth() == mWindowWidth
					&& dms[i].getHeight() == mWindowHeight)
				{
					dm = dms[i];
					break;
				}
			}
			
			if (dm == null)
			{
				dm = new DisplayMode(mWindowWidth, mWindowHeight);
				Display.setDisplayMode(dm);
			}
			else
			{				
				Display.setDisplayMode(dm);
				Display.setFullscreen(mIsFullscreen);
				Display.setVSyncEnabled(mIsFullscreen);
			}
			
			if (!Display.isCreated())
			{
				Display.create();
			}
			
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, mWindowWidth, mWindowHeight, 0, -1, 1);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public InputHandler GetInputHandler()
	{
		return mInput;
	}
	
	public int Width()
	{
		return mWindowWidth;
	}
	
	public int Height()
	{
		return mWindowHeight;
	}
}
