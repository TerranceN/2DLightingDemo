package input;

import gameBase.*;
import org.lwjgl.input.*;

public class InputHandler
{
	private static final int NUM_KEYS = 256;
	private static final int NUM_MOUSE_BUTTONS = 10;
	
	private GameWindow mGame;
	
	private boolean keys[];
	private boolean keysOnce[];
	
	private boolean mouseButtons[];
	private boolean mouseButtonsOnce[];
	private int mouseX = 0;
	private int mouseY = 0;
	private int mouseWheel = 0;
	private int dWheel = 0;
	
	private boolean mRecordingKeys = false;
	
	private String inputString = "";
	
	public InputHandler(GameWindow newGame)
	{
		mGame = newGame;
		
		keys = new boolean[NUM_KEYS];
		keysOnce = new boolean[NUM_KEYS];
		mouseButtons = new boolean[NUM_MOUSE_BUTTONS];
		mouseButtonsOnce = new boolean[NUM_MOUSE_BUTTONS];
	}
	
	public void ClearMouse()
	{
		for (int i = 0; i < NUM_MOUSE_BUTTONS; i++)
		{
			mouseButtons[i] = false;
			mouseButtonsOnce[i] = false;
		}
	}
	
	public void ClearKeys()
	{
		for (int i = 0; i < NUM_KEYS; i++)
		{
			keys[i] = false;
			keysOnce[i] = false;
		}
	}
	
	public void Clear()
	{
		ClearKeys();
		ClearMouse();
		mouseWheel = 0;
		dWheel = 0;
	}
	
	public void BeginRecordingKeys()
	{
		mRecordingKeys = true;
	}
	
	public void EndRecordingKeys()
	{
		mRecordingKeys = false;
		inputString = "";
	}
	
	private boolean IsInKeyRange(int key)
	{
		return (key >= 0 && key < NUM_KEYS);
	}
	
	private boolean IsInMouseButtonRange(int button)
	{
		return (button >= 0 && button < NUM_MOUSE_BUTTONS);
	}
	
	public boolean IsKeyDown(int key)
	{
		if (IsInKeyRange(key))
		{
			return keys[key];
		}
		
		return false;
	}
	
	public boolean IsKeyHit(int key)
	{
		if (IsInKeyRange(key))
		{
			boolean returnValue = keysOnce[key];
			keysOnce[key] = false;
			return returnValue;
		}
		
		return false;
	}
	
	public boolean IsMouseDown(int button)
	{
		if (IsInMouseButtonRange(button))
		{
			return mouseButtons[button];
		}
		
		return false;
	}
	
	public boolean IsMouseHit(int button)
	{
		if (IsInMouseButtonRange(button))
		{
			boolean returnValue = mouseButtonsOnce[button];
			mouseButtonsOnce[button] = false;
			return returnValue;
		}
		
		return false;
	}
	
	public int GetMouseX()
	{
		return mouseX;
	}
	
	public int GetMouseY()
	{
		return mouseY;
	}
	
	public int GetMouseWheel()
	{
		return mouseWheel;
	}
	
	public int GetMouseDeltaWheel()
	{
		int wheel = dWheel;
		dWheel = 0;
		return wheel;
	}
	
	public void KeyDown(int key)
	{
		if (IsInKeyRange(key))
		{
			keys[key] = true;
			keysOnce[key] = true;
		}
	}
	
	public void KeyUp(int key)
	{
		if (IsInKeyRange(key))
		{
			keys[key] = false;
			keysOnce[key] = false;
		}
	}
	
	public void HandleKeyboardEvent()
	{
		int key = Keyboard.getEventKey();
		
		if (IsInKeyRange(key))
		{
			if (Keyboard.getEventKeyState())
			{
				if (mRecordingKeys)
				{
					if (key == 14)
					{
						if (inputString.length() > 0)
						{
							inputString = inputString.substring(0, inputString.length() - 1);
						}
					}
					else
					{
						inputString += KeyToString(key);
					}
				}
				
				KeyDown(key);
			}
			else
			{
				KeyUp(key);
			}
		}
	}
	
	public void HandleMouseEvent()
	{
		mouseX = Mouse.getEventX();
		mouseY = mGame.Height() - Mouse.getEventY();
		
		int mouseButton = Mouse.getEventButton();
		
		int mouseDeltaWheel = Mouse.getDWheel() / 120;
		
		mouseWheel += mouseDeltaWheel;
		dWheel += mouseDeltaWheel;
		
		if (IsInMouseButtonRange(mouseButton))
		{
			if (Mouse.getEventButtonState())
			{
				mouseButtons[mouseButton] = true;
				mouseButtonsOnce[mouseButton] = true;
			}
			else
			{
				mouseButtons[mouseButton] = false;
				mouseButtonsOnce[mouseButton] = false;
			}
		}
	}
	
	public String GetInputString()
	{
		return inputString;
	}
	
	public void SetInputString(String s)
	{
		inputString = s;
	}
	
	public String TakeInputString()
	{
		String temp = inputString;
		inputString = "";
		return temp;
	}
	
	public String KeyToString(int key)
	{
		String returnString = "";
		
		boolean shiftPressed = IsKeyDown(Keyboard.KEY_RSHIFT) || IsKeyDown(Keyboard.KEY_LSHIFT);
		
		switch(key)
		{
			case 2:
			{
				if (shiftPressed)
				{
					returnString = "!";
				}
				else
				{
					returnString = "1";
				}
			}
			break;
			case 3:
			{
				if (shiftPressed)
				{
					returnString = "@";
				}
				else
				{
					returnString = "2";
				}
			}
			break;
			case 4:
			{
				if (shiftPressed)
				{
					returnString = "#";
				}
				else
				{
					returnString = "3";
				}
			}
			break;
			case 5:
			{
				if (shiftPressed)
				{
					returnString = "$";
				}
				else
				{
					returnString = "4";
				}
			}
			break;
			case 6:
			{
				if (shiftPressed)
				{
					returnString = "%";
				}
				else
				{
					returnString = "5";
				}
			}
			break;
			case 7:
			{
				if (shiftPressed)
				{
					returnString = "^";
				}
				else
				{
					returnString = "6";
				}
			}
			break;
			case 8:
			{
				if (shiftPressed)
				{
					returnString = "&";
				}
				else
				{
					returnString = "7";
				}
			}
			break;
			case 9:
			{
				if (shiftPressed)
				{
					returnString = "*";
				}
				else
				{
					returnString = "8";
				}
			}
			break;
			case 10:
			{
				if (shiftPressed)
				{
					returnString = "(";
				}
				else
				{
					returnString = "9";
				}
			}
			break;
			case 11:
			{
				if (shiftPressed)
				{
					returnString = ")";
				}
				else
				{
					returnString = "0";
				}
			}
			break;
			case 12:
			{
				if (shiftPressed)
				{
					returnString = "_";
				}
				else
				{
					returnString = "-";
				}
			}
			break;
			case 13:
			{
				if (shiftPressed)
				{
					returnString = "+";
				}
				else
				{
					returnString = "=";
				}
			}
			break;
			case 16:
			{
				returnString = "q";
			}
			break;
			case 17:
			{
				returnString = "w";
			}
			break;
			case 18:
			{
				returnString = "e";
			}
			break;
			case 19:
			{
				returnString = "r";
			}
			break;
			case 20:
			{
				returnString = "t";
			}
			break;
			case 21:
			{
				returnString = "y";
			}
			break;
			case 22:
			{
				returnString = "u";
			}
			break;
			case 23:
			{
				returnString = "i";
			}
			break;
			case 24:
			{
				returnString = "o";
			}
			break;
			case 25:
			{
				returnString = "p";
			}
			break;
			case 30:
			{
				returnString = "a";
			}
			break;
			case 31:
			{
				returnString = "s";
			}
			break;
			case 32:
			{
				returnString = "d";
			}
			break;
			case 33:
			{
				returnString = "f";
			}
			break;
			case 34:
			{
				returnString = "g";
			}
			break;
			case 35:
			{
				returnString = "h";
			}
			break;
			case 36:
			{
				returnString = "j";
			}
			break;
			case 37:
			{
				returnString = "k";
			}
			break;
			case 38:
			{
				returnString = "l";
			}
			break;
			case 44:
			{
				returnString = "z";
			}
			break;
			case 45:
			{
				returnString = "x";
			}
			break;
			case 46:
			{
				returnString = "c";
			}
			break;
			case 47:
			{
				returnString = "v";
			}
			break;
			case 48:
			{
				returnString = "b";
			}
			break;
			case 49:
			{
				returnString = "n";
			}
			break;
			case 50:
			{
				returnString = "m";
			}
			break;
			case 51:
			{
				if (shiftPressed)
				{
					returnString = "<";
				}
				else
				{
					returnString = ",";
				}
			}
			break;
			case 52:
			{
				if (shiftPressed)
				{
					returnString = ">";
				}
				else
				{
					returnString = ".";
				}
			}
			break;
			case 53:
			{
				if (shiftPressed)
				{
					returnString = "?";
				}
				else
				{
					returnString = "/";
				}
			}
			break;
		}
		
		if (shiftPressed)
		{
			returnString = returnString.toUpperCase();
		}
		
		return returnString;
	}
}
