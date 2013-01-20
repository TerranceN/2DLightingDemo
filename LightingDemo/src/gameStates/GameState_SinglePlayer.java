package gameStates;

import input.*;
import levels.*;
import textures.*;
import util.*;
import entities.*;
import gameBase.*;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;

public class GameState_SinglePlayer extends GameState_Game
{
	private Player p;
	
	float x = 0;
	
	public GameState_SinglePlayer(GameWindow window)
	{
		super(window);
	}

	public void Load()
	{
		super.Load();
		
		Player.Load();
		
		mLevel = new Level();
		
		ComplexPolygon cPoly = new ComplexPolygon(this, new Vector2(475, 600));
		SimplePolygon poly;
		
		poly = new SimplePolygon(this, new Vector2(300, 300));
		poly.AddVertex(new Vector2(-25, -25));
		poly.AddVertex(new Vector2(0, -25));
		poly.AddVertex(new Vector2(25, 0));
		poly.AddVertex(new Vector2(25, 25));
		poly.AddVertex(new Vector2(-5, 5));
		mLevel.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(200, 250));
		poly.AddVertex(new Vector2(-25, -25));
		poly.AddVertex(new Vector2(-25, 25));
		poly.AddVertex(new Vector2(25, 25));
		poly.AddVertex(new Vector2(25, -25));
		mLevel.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(475, 325));
		poly.AddVertex(new Vector2(-25, -250));
		poly.AddVertex(new Vector2(25, -250));
		poly.AddVertex(new Vector2(25, 300));
		poly.AddVertex(new Vector2(-25, 250));
		cPoly.AddPolygon(poly);
		
		poly = new SimplePolygon(this, new Vector2(200, 600));
		poly.AddVertex(new Vector2(-250, -25));
		poly.AddVertex(new Vector2(250, -25));
		poly.AddVertex(new Vector2(300, 25));
		poly.AddVertex(new Vector2(-250, 25));
		cPoly.AddPolygon(poly);
		cPoly.AddConnectedEdge(new Vector2(250, -25), new Vector2(300, 25));
		
		mLevel.AddPolygon(cPoly);
		
		Texture board = new Texture("Textures/Background.png");
		mLevel.AddBackground(board, new Rectangle(0, 0, 1000, 1000));
		mLevel.AddBackground(board, new Rectangle(-1000, 0, 1000, 1000));
		mLevel.AddBackground(board, new Rectangle(0, -1000, 1000, 1000));
		mLevel.AddBackground(board, new Rectangle(-1000, -1000, 1000, 1000));
		
		p = new Player(this, new Vector2(400, 300));
		
		mAmbientLighting = 0.1f;
	}
	
	public void UnLoad()
	{
		super.UnLoad();
		
		mLevel.Delete();
		Player.UnLoad();
	}
	
	public void Update(GameTime gameTime)
	{
		InputHandler input = mGameWindow.GetInputHandler();
		
		if (input.IsKeyDown(Keyboard.KEY_ESCAPE))
		{
			UnLoad();
		}
		
		if (input.IsKeyDown(Keyboard.KEY_Q))
		{
			x += 0.01f;
		}
		
		if (input.IsKeyDown(Keyboard.KEY_E))
		{
			x -= 0.01f;
		}
		
		p.Update(gameTime);
		
		mCamera.SetTranslationFocus(p.GetPosition());
		mCamera.SetRotationFocus(x);
		
		super.Update(gameTime);
	}
	
	public void DrawScene()
	{
		glColor3f(1, 1, 1);
		mLevel.Draw();
		p.Draw();
	}
}
