package util;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

/**
 * A Color object with methods for rgb multiplication,
 * and setting the color in openGL.
 * @author Terry
 */
public class GLColor
{
	private float mRed = 0;
	private float mGreen = 0;
	private float mBlue = 0;
	
	/**
	 * Constructor.
	 * Creates a new GLColor set to black.
	 */
	public GLColor(){}
	
	/**
	 * Constructor.
	 * Creates a new GLColor set to the passed RGB values.
	 * @param red The red value for the new GLColor.
	 * @param green The green value for the new GLColor.
	 * @param blue The blue value for the new GLColor.
	 */
	public GLColor(float red, float green, float blue)
	{
		SetRed(red);
		SetGreen(green);
		SetBlue(blue);
	}
	
	/**
	 * Constructor.
	 * Creates a new GLColor set to the values of the passed Color object.
	 * @param c The Color this GLColor's RGB values should be set to.
	 */
	public GLColor(Color c)
	{
		SetRed(c.getRed() / 255.0f);
		SetGreen(c.getGreen() / 255.0f);
		SetBlue(c.getBlue() / 255.0f);
	}
	
	/**
	 * Multiplies the current RGB values my the passed multiplier.
	 * @param multiplier The value to multiply the RGB values by.
	 * @return The resultant GLColor.
	 */
	public GLColor Times(float multiplier)
	{
		return new GLColor(
			GetRed() * multiplier,
			GetGreen() * multiplier,
			GetBlue() * multiplier);
	}
	
	/**
	 * Divides the current RGB values by the passed divisor.
	 * @param divisor The value to divide the RGB values by.
	 * @return The resultant GLColor.
	 */
	public GLColor DividedBy(float divisor)
	{
		return new GLColor(
			GetRed() / divisor,
			GetGreen() / divisor,
			GetBlue() / divisor);
	}
	
	public void TimesEquals(float multiplier)
	{
		SetRed(GetRed() * multiplier);
		SetGreen(GetGreen() * multiplier);
		SetBlue(GetBlue() * multiplier);
	}
	
	public void DividedByEquals(float divisor)
	{
		SetRed(GetRed() / divisor);
		SetGreen(GetGreen() / divisor);
		SetBlue(GetBlue() / divisor);
	}
	
	/**
	 * Sets the values of this GLColor to the values of the passed GLColor.
	 * @param other The GLColor whose values this GLColor's values should be set to.
	 */
	public void SetEqual(GLColor other)
	{
		SetRed(other.GetRed());
		SetGreen(other.GetGreen());
		SetBlue(other.GetBlue());
	}
	
	/**
	 * Sets the current openGL color to be the values of this GLColor.
	 */
	public void SetColor()
	{
		glColor3f(GetRed(), GetGreen(), GetBlue());
	}
	
	/**
	 * Sets the red value of this GLColor.
	 * @param red The red value that should be set.
	 */
	public void SetRed(float red)
	{
		if (red > 1)
		{
			mRed = 1;
		}
		else
		{
			mRed = red;
		}
	}
	
	/**
	 * Sets the green value of this GLColor.
	 * @param green The green value that should be set.
	 */
	public void SetGreen(float green)
	{
		if (green > 1)
		{
			mGreen = 1;
		}
		else
		{
			mGreen = green;
		}
	}
	
	/**
	 * Sets the blue value of this GLColor.
	 * @param blue The blue value that should be set.
	 */
	public void SetBlue(float blue)
	{
		if (blue > 1)
		{
			mBlue = 1;
		}
		else
		{
			mBlue = blue;
		}
	}
	
	/**
	 * Gets the red value of this GLColor.
	 * @return The red value of this GLColor.
	 */
	public float GetRed()
	{
		return mRed;
	}
	
	/**
	 * Gets the green value of this GLColor.
	 * @return The green value of this GLColor.
	 */
	public float GetGreen()
	{
		return mGreen;
	}
	
	/**
	 * Gets the blue value of this GLColor.
	 * @return The blue value of this GLColor.
	 */
	public float GetBlue()
	{
		return mBlue;
	}
}
