package game;

import gameBase.*;
import gameStates.*;

public class Game extends GameWindow
{
	public static void main(String[] args)
	{
		new Game().Run();
	}
	
	protected boolean Initialize()
	{
		boolean returnValue = super.Initialize();
		
		gameStates.add(new GameState_MainMenu(this));
		
		return returnValue;
	}
}
