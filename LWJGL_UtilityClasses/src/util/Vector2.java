package util;

/**
 * A two component vector.
 * @author Terry
 */
public class Vector2
{
	/**
	 * The X component of this vector.
	 */
	public float X;
	
	/**
	 * The Y component of this vector.
	 */
	public float Y;
	
	/**
	 * Constructor.
	 * Sets both components to zero.
	 */
	public Vector2()
	{
		X = 0;
		Y = 0;
	}
	
	/**
	 * Constructor.
	 * Sets both components to parameter.
	 * @param nX New value for both X and Y components.
	 */
	public Vector2(float newX)
	{
		X = newX;
		Y = newX;
	}
	
	/**
	 * Constructor.
	 * Sets both components to corresponding parameters.
	 * @param nX New value for X component.
	 * @param nY New value for Y component.
	 */
	public Vector2(float newX, float newY)
	{
		X = newX;
		Y = newY;
	}
	
	/**
	 * Creates a unit vector from the angle parameter.
	 * @param angle Angle to create unit vector for.
	 * @return Unit vector that is along the angle parameter.
	 */
	public static Vector2 FromAngle(float angle)
	{
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	public String toString()
	{
		return X + ", " + Y;
	}
	
	/**
	 * Sets the current vector's components to be equal to the parameter's components.
	 * @param otherVector The vector whose components this vector's components should be set to.
	 */
	public void SetEqual(Vector2 otherVector)
	{
		X = otherVector.X;
		Y = otherVector.Y;
	}
	
	public boolean Equals(Vector2 otherVector)
	{
		return X == otherVector.X && Y == otherVector.Y;
	}
	
	/**
	 * Creates a copy of the current vector.
	 * @return A copy of the current vector.
	 */
	public Vector2 Copy()
	{
		return new Vector2(X, Y);
	}
	
	/**
	 * Add two vectors.
	 * @param otherVector The vector this vector should be added to.
	 * @return The sum of the two vectors.
	 */
	public Vector2 Plus(Vector2 otherVector)
	{
		return new Vector2(X + otherVector.X, Y + otherVector.Y);
	}

	/**
	 * Adds a scalar to each component of a vector.
	 * @param plus The scalar that should be added to each component.
	 * @return The sum of the vector and scalar.
	 */
	public Vector2 Plus(float plus)
	{
		return new Vector2(X + plus, Y + plus);
	}
	
	/**
	 * Subtracts two vectors.
	 * @param otherVector The vector that should be subtracted from this vector.
	 * @return The difference of the two vectors.
	 */
	public Vector2 Minus(Vector2 otherVector)
	{
		return new Vector2(X - otherVector.X, Y - otherVector.Y);
	}

	/**
	 * Subtracts a scalar from each component of a vector.
	 * @param minus The scalar that should be subtracted from this vector.
	 * @return The difference of the vector and scalar.
	 */
	public Vector2 Minus(float minus)
	{
		return new Vector2(X - minus, Y - minus);
	}
	
	/**
	 * Multiplies (component by component) two vectors.
	 * @param otherVector The vector that should be multiplied to this vector.
	 * @return The multiplication of the two vectors.
	 */
	public Vector2 Times(Vector2 otherVector)
	{
		return new Vector2(X * otherVector.X, Y * otherVector.Y);
	}

	/**
	 * Multiplies a vector by a scalar.
	 * @param multiplier The scalar that should be multiplied by this vector.
	 * @return The multiplication of the vector and scalar.
	 */
	public Vector2 Times(float multiplier)
	{
		return new Vector2(X * multiplier, Y * multiplier);
	}
	
	/**
	 * Divides two vectors.
	 * @param otherVector The vector that this vector should be divided by.
	 * @return The quotient of the two vectors.
	 */
	public Vector2 DividedBy(Vector2 otherVector)
	{
		return new Vector2(X / otherVector.X, Y / otherVector.Y);
	}

	/**
	 * Divides a vector by a scalar.
	 * @param divisor The scalar that this vector should be divided by.
	 * @return The quotient of the vector divided by the scalar.
	 */
	public Vector2 DividedBy(float divisor)
	{
		return new Vector2(X / divisor, Y / divisor);
	}
	
	/**
	 * Increments this vector by another vector.
	 * @param otherVector The vector that this vector should be incremented by.
	 */
	public void PlusEquals(Vector2 otherVector)
	{
		X += otherVector.X;
		Y += otherVector.Y;
	}
	
	/**
	 * Increments this vector by a scalar.
	 * @param plus The scalar that this vector should be incremented by.
	 */
	public void PlusEquals(float plus)
	{
		X += plus;
		Y += plus;
	}
	
	/**
	 * Decrements this vector by another vector.
	 * @param otherVector The vector that this vector should be decremented by.
	 */
	public void MinusEquals(Vector2 otherVector)
	{
		X -= otherVector.X;
		Y -= otherVector.Y;
	}
	
	/**
	 * Decrements this vector by a scalar.
	 * @param minus The scalar that this vector should be decremented by.
	 */
	public void MinusEquals(float minus)
	{
		X -= minus;
		Y -= minus;
	}
	
	/**
	 * Multiplies this vector by another vector.
	 * @param otherVector The vector that this vector should be multiplied by.
	 */
	public void TimesEquals(Vector2 otherVector)
	{
		X *= otherVector.X;
		Y *= otherVector.Y;
	}

	/**
	 * Multiplies this vector by a scalar.
	 * @param multiplier The scalar that this vector should be multiplied by.
	 */
	public void TimesEquals(float multiplier)
	{
		X *= multiplier;
		Y *= multiplier;
	}
	
	/**
	 * Divides this vector by another vector.
	 * @param otherVector The vector that this vector should be divided by.
	 */
	public void DividedByEquals(Vector2 otherVector)
	{
		X /= otherVector.X;
		Y /= otherVector.Y;
	}

	/**
	 * Divides this vector by a scalar.
	 * @param divisor The scalar that this vector should be divided by.
	 */
	public void DividedByEquals(float divisor)
	{
		X /= divisor;
		Y /= divisor;
	}
	
	/**
	 * Gets the length of the vector.
	 * @return The length of the vector.
	 */
	public float Length()
	{
		return (float)Math.sqrt(LengthSquared());
	}
	
	/**
	 * Gets the length squared of the vector.
	 * @return The length of the vector.
	 */
	public float LengthSquared()
	{
		return (float)(X*X + Y*Y);
	}
	
	/**
	 * Transforms this vector into a unit vector.
	 */
	public void Normalize()
	{
		float length = Length();
		
		if (length > 0)
		{
			X /= length;
			Y /= length;
		}
	}
	
	/**
	 * Gets the unit vector of this vector.
	 * @return This unit vector of this vector.
	 */
	public Vector2 GetNormalized()
	{
		float length = Length();
		Vector2 newVector = new Vector2(X, Y);
		
		if (length > 0)
		{
			newVector.X /= length;
			newVector.Y /= length;
		}
		
		return newVector;
	}
	
	public float Dot(Vector2 other)
	{
		return (X * other.X) + (Y * other.Y);
	}
}
