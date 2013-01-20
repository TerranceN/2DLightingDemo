package bitmapFonts;

import textures.*;
import util.Rectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

public class BitmapFont
{
	private Texture mBitmap;
	private ArrayList<BitmapCharacter> mCharacters = new ArrayList<BitmapCharacter>();
	private int mLineHeight = 0;
	
	public static enum XAllignment
	{
		Left,
		Middle,
		Right
	}
	
	public static enum YAllignment
	{
		Top,
		Middle,
		Bottom
	}
	
	class Kerning
	{
		public int otherChar;
		public int amount;
	}
	
	class BitmapCharacter
	{
		public int id;
		public Rectangle imageRect = new Rectangle();
		public int xOffset;
		public int yOffset;
		public int xAdvance;
		
		public ArrayList<Kerning> kernings = new ArrayList<Kerning>();
		
		public int GetKerning(int otherChar)
		{
			for (int i = 0; i < kernings.size(); i++)
			{
				if (kernings.get(i).otherChar == otherChar)
				{
					return kernings.get(i).amount;
				}
			}
			
			return 0;
		}
	}
	
	public BitmapFont(String fontName)
	{
		try
		{
			LoadFont(fontName);
		}
		catch(Exception e)
		{
			System.out.println("Error loading BitmapFont");
			e.printStackTrace();
			System.out.println();
		}
	}
	
	public BitmapFont(String bitmapName, String fontName)
	{
		try
		{
			LoadBitmap(bitmapName);
			LoadFont(fontName);
		}
		catch(Exception e)
		{
			System.out.println("Error loading BitmapFont");
			e.printStackTrace();
			System.out.println();
		}
	}
	
	private boolean LoadBitmap(String bitmapName)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(bitmapName));
			
			mBitmap = new Texture(image);
			
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Error loading bitmap while loading BitmapFont");
			e.printStackTrace();
			System.out.println();
			
			return false;
		}
	}
	
	private BitmapCharacter GetCharacter(int id)
	{
		for (int i = 0; i < mCharacters.size(); i++)
		{
			if (mCharacters.get(i).id == id)
			{
				return mCharacters.get(i);
			}
		}
		
		return null;
	}
	
	private boolean LoadCharacter(String line)
	{
		try
		{
			Scanner s = new Scanner(line);
			BitmapCharacter newCharacter = new BitmapCharacter();
			
			while(s.hasNext())
			{
				StringTokenizer st = new StringTokenizer(s.next(), "=");
				String command = st.nextToken();
				
				if (command.equals("id"))
				{
					newCharacter.id = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("x"))
				{
					newCharacter.imageRect.x = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("y"))
				{
					newCharacter.imageRect.y = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("width"))
				{
					newCharacter.imageRect.width = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("height"))
				{
					newCharacter.imageRect.height = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("xoffset"))
				{
					newCharacter.xOffset = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("yoffset"))
				{
					newCharacter.yOffset = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("xadvance"))
				{
					newCharacter.xAdvance = Integer.parseInt(st.nextToken());
				}
			}
			
			mCharacters.add(newCharacter);
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	private boolean LoadKerning(String line)
	{
		try
		{
			Scanner s = new Scanner(line);
			Kerning newKerning = new Kerning();
			int id = 0;
			
			while(s.hasNext())
			{
				StringTokenizer st = new StringTokenizer(s.next(), "=");
				String command = st.nextToken();
				
				if (command.equals("first"))
				{
					id = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("second"))
				{
					newKerning.otherChar = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("amount"))
				{
					newKerning.amount = Integer.parseInt(st.nextToken());
				}
			}
			
			BitmapCharacter c = GetCharacter(id);
			
			if (c != null)
				c.kernings.add(newKerning);
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	private void LoadOther(String line)
	{
		try
		{
			Scanner s = new Scanner(line);
			
			while(s.hasNext())
			{
				StringTokenizer st = new StringTokenizer(s.next(), "=");
				String command = st.nextToken();
				
				if (command.equals("lineHeight"))
				{
					mLineHeight = Integer.parseInt(st.nextToken());
				}
				else if (command.equals("file"))
				{
					String fileName = st.nextToken();
					LoadBitmap(fileName.substring(1, fileName.length() - 1));
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	private boolean LoadFont(String fontName)
	{
		try
		{
			Scanner s = new Scanner(new File(fontName));
			
			while (s.hasNext())
			{
				String type = s.next();
				
				if (type.equals("char"))
				{
					if (!LoadCharacter(s.nextLine()))
						throw new Exception("Failed to load character data.");
				}
				else if (type.equals("kerning"))
				{
					if (!LoadKerning(s.nextLine()))
						throw new Exception("Failed to load character data.");
				}
				else
				{
					LoadOther(s.nextLine());
				}
			}
			
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Error loading font while loading BitmapFont");
			e.printStackTrace();
			System.out.println();
			
			return false;
		}
	}
	
	public float StringWidth(String s, float scaleX)
	{
		float length = 0;
		
		BitmapCharacter previousCharacter = null;
		BitmapCharacter c = null;
		
		for (int i = 0; i < s.length(); i++)
		{
			c = GetCharacter((int)s.charAt(i));
			
			if (c != null)
			{
				if (previousCharacter != null)
				{
					length += previousCharacter.GetKerning(c.id);
				}
				
				length += c.xAdvance;
				
				previousCharacter = c;
			}
		}
		
		return scaleX * length;
	}
	
	public float StringHeight(float scaleY)
	{
		return scaleY * mLineHeight;
	}
	
	public void DrawChar(char c, float x, float y, float scaleX, float scaleY)
	{
		BitmapCharacter b = GetCharacter((int)c);
		
		if (b != null)
		{
			DrawChar(b, x, y, scaleX, scaleY);
		}
	}
	
	public void DrawCharOffsetted(BitmapCharacter c, float x, float y, float scaleX, float scaleY)
	{
		DrawChar(c, x + scaleX * c.xOffset, y + scaleY * c.yOffset, scaleX, scaleY);
	}
	
	public void DrawChar(BitmapCharacter c, float x, float y, float scaleX, float scaleY)
	{
		mBitmap.BindTexture();
		mBitmap.Draw(c.imageRect, new Rectangle(x, y, scaleX * c.imageRect.width, scaleY * c.imageRect.height), 0, 0, 0);
	}
	
	public void DrawString(String s, float x, float y, float scaleX, float scaleY, XAllignment xAll, YAllignment yAll)
	{
		float startingX = x;
		float startingY = y;
		
		switch(xAll)
		{
			case Middle:
			{
				float width = StringWidth(s, scaleX);
				startingX -= width / 2;
			}
			break;
			case Right:
			{
				float width = StringWidth(s, scaleX);
				startingX -= width;
			}
			break;
		}
		
		switch(yAll)
		{
			case Middle:
			{
				startingY -= StringHeight(scaleY) / 2;
			}
			break;
			case Bottom:
			{
				startingY -= StringHeight(scaleY);
			}
			break;
		}
		
		DrawString(s, startingX, startingY, scaleX, scaleY);
	}
	
	public void DrawString(String s, float x, float y, float scaleX, float scaleY)
	{
		float length = 0;
		
		BitmapCharacter previousCharacter = null;
		
		for (int i = 0; i < s.length(); i++)
		{
			BitmapCharacter c = GetCharacter((int)s.charAt(i));
			
			if (c != null)
			{
				if (previousCharacter != null)
				{
					length += scaleX * (previousCharacter.xAdvance + previousCharacter.GetKerning(c.id));
				}
				
				DrawCharOffsetted(c, x + length, y, scaleX, scaleY);
				
				previousCharacter = c;
			}
		}
	}
}
