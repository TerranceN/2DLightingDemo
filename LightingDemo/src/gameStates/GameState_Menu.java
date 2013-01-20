package gameStates;

import gameBase.*;
import menus.*;

public class GameState_Menu extends GameState
{
	protected Menu mMenu;
	
	public GameState_Menu(GameWindow window)
	{
		super(window);
	}
	
	public void Load()
	{
		mMenu = new Menu(this);
	}
	
	public void OnMenuPressed(String menuOptionName)
	{
		
	}
	
	public void Update(GameTime gameTime)
	{
		mMenu.Update();
	}
	
	public void Draw()
	{
		mMenu.Draw();
	}
}
