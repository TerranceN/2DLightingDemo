package menus;

import textures.*;
import util.Rectangle;
import bitmapFonts.*;

public abstract class MenuItem
{
	protected final float SPACING = 100;
	
	protected Menu mOwner;
	protected String mName;
	protected String mValue;
	protected float mTransparency;
	
	public MenuItem (Menu newOwner)
	{
		mOwner = newOwner;
	}
	
	public String GetName()
	{
		return mName;
	}
	
	public String GetValue()
	{
		return mValue;
	}
	
	public float Width()
	{
		BitmapFont f = mOwner.GetFont();
		
		return f.StringWidth(mName, mOwner.GetScaleX())
			+ SPACING
			+ f.StringWidth(mValue, mOwner.GetScaleX());
	}
	
	public float Height()
	{
		BitmapFont f = mOwner.GetFont();
		
		return f.StringHeight(mOwner.GetScaleY());
	}
	
	public Rectangle GetBoundingRect(float x, float y)
	{
		float width = Width();
		
		float height = Height();
		
		return new Rectangle(x - width / 2, y - height / 2, width, height);
	}
	
	public void OnSelection()
	{
	}
	
	public void SetTransparency(float t)
	{
		mTransparency = t;
	}
	
	public void HoveredOver()
	{
		mTransparency += 0.04;
		
		if (mTransparency > 1)
		{
			mTransparency = 1;
		}
	}
	
	public void NotHoveredOver()
	{
		mTransparency -= 0.04;
		
		if (mTransparency < 0)
		{
			mTransparency = 0;
		}
	}
	
	public float GetTransparency()
	{
		return mTransparency;
	}
	
	public void Draw(float x, float y)
	{
		BitmapFont f = mOwner.GetFont();
		
		float width = Width();
		
		f.DrawString(mName,
				x - width / 2,
				y,
				mOwner.GetScaleX(),
				mOwner.GetScaleY(),
				BitmapFont.XAllignment.Left,
				BitmapFont.YAllignment.Middle);
		
		f.DrawString(mValue,
				x - width / 2 + f.StringWidth(mName, mOwner.GetScaleX()) + SPACING,
				y,
				mOwner.GetScaleX(),
				mOwner.GetScaleY(),
				BitmapFont.XAllignment.Left,
				BitmapFont.YAllignment.Middle);
	}
}
