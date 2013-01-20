package levels;

import textures.*;
import util.Rectangle;

import java.util.*;

class BackgroundTexture
{
	private Texture mTexture;
	private ArrayList<Rectangle> mDrawRectangles = new ArrayList<Rectangle>();
	
	public BackgroundTexture(Texture texture)
	{
		mTexture = texture;
	}
	
	public BackgroundTexture(String textureName)
	{
		mTexture = new Texture(textureName);
	}
	
	public Texture GetTexture()
	{
		return mTexture;
	}
	
	public void AddRect(Rectangle newRect)
	{
		mDrawRectangles.add(newRect);
	}
	
	public void Delete()
	{
		mTexture.Delete();
		mDrawRectangles.clear();
	}
	
	public void Draw()
	{
		if (mTexture != null)
		{
			Texture.Begin();
			{
				mTexture.BindTexture();
			
				for (int i = 0; i < mDrawRectangles.size(); i++)
				{
					mTexture.Draw(null, mDrawRectangles.get(i), 0, 0, 0);
				}
			}
			Texture.End();
		}
	}
}
