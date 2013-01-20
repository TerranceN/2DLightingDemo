package menus;

import bitmapFonts.BitmapFont;

public class MenuItem_Basic extends MenuItem
{
	public MenuItem_Basic(Menu newOwner, String newName)
	{
		super(newOwner);
		
		mName = newName;
	}
	
	public float Width()
	{
		BitmapFont f = mOwner.GetFont();
		
		return f.StringWidth(mName, mOwner.GetScaleX());
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
	}
}
