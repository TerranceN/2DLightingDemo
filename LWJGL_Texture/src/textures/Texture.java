package textures;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import util.Rectangle;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.*;
import java.awt.image.*;
import java.io.*;
import java.nio.*;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Texture
{
	private IntBuffer mTexture;
	private int mWidth;
	private int mHeight;
	
	private int mOriginalWidth;
	private int mOriginalHeight;
	
	private float mOriginalPercentX;
	private float mOriginalPercentY;
	
	public static void Begin()
	{
		glEnable(GL_TEXTURE_2D);
	}
	
	public static void End()
	{
		glDisable(GL_TEXTURE_2D);
	}
	
	public Texture(int width, int height)
	{
		mOriginalWidth = width;
		mOriginalHeight = height;
		
		mTexture = BufferUtils.createIntBuffer(1);
		glGenTextures(mTexture);
		
		ByteBuffer bBuffer = NewImage();
		
		glBindTexture(GL_TEXTURE_2D, mTexture.get(0));
		
		glTexImage2D(GL_TEXTURE_2D,
				0,
				GL_RGBA,
				mWidth,
				mHeight,
				0,
				GL_RGBA,
				GL_UNSIGNED_BYTE,
				bBuffer);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);	// Linear Filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);	// Linear Filtering
		glTexEnvf(GL_TEXTURE_2D, GL_TEXTURE_ENV_MODE, GL_ADD);
	}
	
	public Texture(String filePath)
	{
		try
		{
			HandleBufferedImage(ImageIO.read(new File(filePath)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Texture(BufferedImage newImg)
	{
		HandleBufferedImage(newImg);
	}
	
	private void HandleBufferedImage(BufferedImage newImg)
	{
		if (!glIsEnabled(GL_TEXTURE_2D))
			glEnable(GL_TEXTURE_2D);
		
		mOriginalWidth = newImg.getWidth();
		mOriginalHeight = newImg.getHeight();
		
		mTexture = BufferUtils.createIntBuffer(1);
		glGenTextures(mTexture);
		
		ByteBuffer bBuffer = ConvertImage(newImg);

		glBindTexture(GL_TEXTURE_2D, mTexture.get(0));
		
		int imageType = newImg.getColorModel().hasAlpha() ? GL_RGBA : GL_RGB;
		
		glTexImage2D(GL_TEXTURE_2D,
				0,
				imageType,
				mWidth,
				mHeight,
				0,
				imageType,
				GL_UNSIGNED_BYTE,
				bBuffer);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);	// Linear Filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);	// Linear Filtering
		glTexEnvf(GL_TEXTURE_2D, GL_TEXTURE_ENV_MODE, GL_ADD);
		
		mOriginalPercentX = (float)mOriginalWidth / mWidth;
		mOriginalPercentY = (float)mOriginalHeight / mHeight;
	}
	
	private ByteBuffer NewImage()
	{
		mWidth = NextPowerOf2(mOriginalWidth);
		mHeight = NextPowerOf2(mOriginalHeight);
		
		ByteBuffer imageBuffer = null;
        
        byte[] data = new byte[4 * mWidth * mHeight];
        
        imageBuffer = ByteBuffer.allocateDirect(4 * mWidth * mHeight);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();
        
        return imageBuffer;
	}
	
	private ByteBuffer ConvertImage(BufferedImage img)
	{
		mWidth = NextPowerOf2(mOriginalWidth);
		mHeight = NextPowerOf2(mOriginalHeight);
		
		ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);
                
		ColorModel glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
		
		ByteBuffer imageBuffer = null;
		WritableRaster raster;
		BufferedImage texImage;
		
		if (img.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,mWidth,mHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable<String, Object>());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,mWidth,mHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable<String, Object>());
        }
		
		Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,mWidth,mHeight);
        g.drawImage(img,0,0,null);
        
        // build a byte buffer from the temporary image

        // that be used by OpenGL to produce a texture.

        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
		
		return imageBuffer;
	}
	
	private int NextPowerOf2(int num)
	{
		int start = 2;
		
		while (start < num)
		{
			start *= 2;
		}
		
		return start;
	}
	
	public boolean IsLoaded()
	{
		return glIsTexture(mTexture.get(0));
	}
	
	public void Delete()
	{
		glDeleteTextures(mTexture);
	}
	
	public void BindTexture()
	{
		if (!glIsEnabled(GL_TEXTURE_2D))
			glEnable(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, mTexture.get(0));
	}
	
	public void Draw(float x, float y)
	{
		Draw(x, y, 1, 1, 0, 0, 0);
	}
	
	public void Draw(float x, float y, float sx, float sy)
	{
		Draw(x, y, sx, sy, 0, 0, 0);
	}
	
	public void Draw(float x, float y, float sx, float sy, float angle)
	{
		Draw(x, y, sx, sy, angle, 0, 0);
	}
	
	public void Draw(float x, float y, float scaleX, float scaleY, float angle, float pivotX, float pivotY)
	{
		/*glPushMatrix();
		glLoadIdentity();
		glTranslatef(x, y, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(scaleX * (Width() / 2 - pivotX), scaleY * (Height() / 2 - pivotY), 0);
		glScalef(scaleX * Width() / 2, scaleY * Height() / 2, 1);
		
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0.0f, mOriginalPercentY); glVertex2f(-1, 1);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(mOriginalPercentX, mOriginalPercentY); glVertex2f(1, 1);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(mOriginalPercentX, 0.0f); glVertex2f(1, -1);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex2f(-1, -1);	// Top Left Of The Texture and Quad
		}
		glEnd();
		
		glPopMatrix();*/
		
		Draw(new Rectangle(0, 0, Width(), Height()), new Rectangle(x, y, Width() * scaleX, Height() * scaleY), angle, pivotX, pivotY);
	}
	
	public void Draw(Rectangle srcRect, Rectangle dstRect, float angle, float pivotX, float pivotY)
	{
		Rectangle src = srcRect;
		Rectangle dst = dstRect.Copy();
		
		if (src == null)
		{
			src = new Rectangle(0, 0, Width(), Height());
		}
		
		glPushMatrix();
		{
			float flipX = 1;
			float flipY = 1;
			
			if (dst.width < 0)
			{
				flipX = -1;
				dst.width = -dst.width;
			}
			
			if (dst.height < 0)
			{
				flipY = -1;
				dst.height = -dst.height;
			}
			
			float scaleX = dst.width / Width();
			float scaleY = dst.height / Height();
			
			glTranslatef(dst.x, dst.y, 0);
			glRotatef(angle, 0, 0, 1);
			glTranslatef(dst.width / 2 - scaleX * pivotX, dst.height / 2 - scaleY * pivotY, 0);
			glScalef(dst.width / 2, dst.height / 2, 1);
			
			glBegin(GL_QUADS);
			{
				float minX = src.x / (float)mWidth;
				float maxX = (src.x + src.width) / (float)mWidth;
				float minY = src.y / (float)mHeight;
				float maxY = (src.y + src.height) / (float)mHeight;
				
				glTexCoord2f(minX, maxY);
				glVertex2f(-1 * flipX, 1 * flipY);	// Bottom Left Of The Texture and Quad
				
				glTexCoord2f(maxX, maxY);
				glVertex2f(1 * flipX, 1 * flipY);	// Bottom Right Of The Texture and Quad
				
				glTexCoord2f(maxX, minY);
				glVertex2f(1 * flipX, -1 * flipY);	// Top Right Of The Texture and Quad
				
				glTexCoord2f(minX, minY);
				glVertex2f(-1 * flipX, -1 * flipY);	// Top Left Of The Texture and Quad
			}
			glEnd();
		}
		glPopMatrix();
	}
	
	public int Width()
	{
		return mOriginalWidth;
	}
	
	public int Height()
	{
		return mOriginalHeight;
	}
	
	public int FullWidth()
	{
		return mWidth;
	}
	
	public int FullHeight()
	{
		return mHeight;
	}
	
	public int ID()
	{
		return mTexture.get(0);
	}
}
