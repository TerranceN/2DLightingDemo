package util;

/**
 * A two dimensional line.
 * @author Terry
 */
public class Line
{
	public static enum LineType
	{
		NORMAL,
		HORIZONTAL,
		VERTICAL
	}
	
	private LineType mType = LineType.NORMAL;
	private float mSlope, mIntercept = 0;
	
	/**
	 * Constructor.
	 * Creates a line that lies along two points.
	 * @param point1 The first point on the line.
	 * @param point2 The second point on the line.
	 */
	public Line(Vector2 point1, Vector2 point2)
	{
		SetLine(point1, point2);
	}
	
	/**
	 * Constructor.
	 * Creates a normal line.
	 * @param slope The slope of the new line.
	 * @param intercept The intercept of the new line.
	 */
	public Line(float slope, float intercept)
	{
		SetNormalLine(slope, intercept);
	}
	
	/**
	 * Constructor.
	 * Creates a non-normal line (vertical or horizontal).
	 * @param lineType The type for the new line.
	 * @param intercept The intercept of the new line.
	 */
	public Line(LineType lineType, float intercept)
	{
		SetOtherLine(lineType, intercept);
	}
	
	/**
	 * Sets values for a line that lies along two points.
	 * @param point1 First point.
	 * @param point2 Second point.
	 */
	public void SetLine(Vector2 point1, Vector2 point2)
	{
		if (point1.Y == point2.Y)
		{
			SetOtherLine(LineType.HORIZONTAL, point1.Y);
		}
		else if(point1.X == point2.X)
		{
			SetOtherLine(LineType.VERTICAL, point1.X);
		}
		else
		{
			float newSlope = (point2.Y - point1.Y) / (point2.X - point1.X);
			float newIntercept = point1.Y - newSlope * point1.X;
			SetNormalLine(newSlope, newIntercept);
		}
	}
	
	/**
	 * Sets values for a normal line.
	 * @param slope The slope that should be set.
	 * @param intercept The intercept that should be set.
	 */
	public void SetNormalLine(float slope, float intercept)
	{
		mType = LineType.NORMAL;
		mSlope = slope;
		mIntercept = intercept;
	}
	
	/**
	 * Sets values for a non-normal line (vertical or horizontal)
	 * @param lineType The type that should be set.
	 * @param intercept The intercept that should be set.
	 */
	public void SetOtherLine(LineType lineType, float intercept)
	{
		mType = lineType;
		mIntercept = intercept;
	}
	
	/**
	 * Gets the slope of the line.
	 * @return The slope of the line.
	 */
	public float GetSlope()
	{
		return mSlope;
	}
	
	/**
	 * Gets the intercept of the line.
	 * @return The slope of the line.
	 */
	public float GetIntercept()
	{
		return mIntercept;
	}
	
	/**
	 * Gets the type of the line.
	 * @return The type of the line.
	 */
	public LineType GetType()
	{
		return mType;
	}
	
	/**
	 * Find the intersection point between this line and another line.
	 * @param otherLine The line to find an intersection with.
	 * @return The intersection point if one exists, otherwise null.
	 */
	public Vector2 GetIntersectionPoint(Line otherLine)
	{
		Vector2 intersection = new Vector2();
		
		switch(mType)
		{
			case NORMAL:
			{
				switch(otherLine.GetType())
				{
					case NORMAL:
					{
						intersection.X = (otherLine.GetIntercept() - GetIntercept())
							/ (GetSlope() - otherLine.GetSlope());
						intersection.Y = GetSlope() * intersection.X + GetIntercept();
					}
					break;
					case HORIZONTAL:
					{
						intersection.Y = otherLine.GetIntercept();
						intersection.X = (intersection.Y - GetIntercept()) / GetSlope();
					}
					break;
					case VERTICAL:
					{
						intersection.X = otherLine.GetIntercept();
						intersection.Y = GetSlope() * intersection.X + GetIntercept();
					}
					break;
				}
			}
			break;
			case HORIZONTAL:
			{
				switch(otherLine.GetType())
				{
					case NORMAL:
					{
						intersection = otherLine.GetIntersectionPoint(this);
					}
					break;
					case HORIZONTAL:
					{
						intersection = null;
					}
					break;
					case VERTICAL:
					{
						intersection.X = otherLine.GetIntercept();
						intersection.Y = GetIntercept();
					}
					break;
				}
			}
			break;
			case VERTICAL:
			{
				switch(otherLine.GetType())
				{
					case NORMAL:
					{
						intersection = otherLine.GetIntersectionPoint(this);
					}
					break;
					case HORIZONTAL:
					{
						intersection = otherLine.GetIntersectionPoint(this);
					}
					break;
					case VERTICAL:
					{
						intersection = null;
					}
					break;
				}
			}
			break;
		}
		
		return intersection;
	}
	
	private int sign(float x)
	{
		if (x > 0)
		{
			return 1;
		}
		else if (x < 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	
	public int WhatSide(Vector2 point)
	{
		if (mType == LineType.VERTICAL)
		{
			return sign(mIntercept - point.X);
		}
		else if (mType == LineType.HORIZONTAL)
		{
			return sign(mIntercept - point.Y);
		}
		
		return sign(mSlope * point.X - point.Y + mIntercept);
	}
}
