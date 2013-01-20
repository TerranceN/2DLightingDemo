package util;

import static org.lwjgl.opengl.GL11.*;

/**
 * A line segment with collision detection methods.
 * @author Terry
 */
public class CollisionLine
{
	Vector2 mPoint1 = new Vector2();
	Vector2 mPoint2 = new Vector2();
	Line line = new Line(mPoint1, mPoint2);
	
	/**
	 * Constructor.
	 * Creates a new CollisionLine from two points.
	 * @param newPoint1 First point.
	 * @param newPoint2 Second point.
	 */
	public CollisionLine(Vector2 point1, Vector2 point2)
	{
		Set(point1, point2);
	}
	
	/**
	 * Sets vales of collision line from two points.
	 * @param point1 First point.
	 * @param point2 Second point.
	 */
	public void Set(Vector2 point1, Vector2 point2)
	{
		mPoint1.SetEqual(point1);
		mPoint2.SetEqual(point2);
		line.SetLine(mPoint1, mPoint2);
	}
	
	/**
	 * Gets the line joining the two points of the CollisionLine.
	 * @return The line joining the two points of the CollisionLine.
	 */
	public Line GetLine()
	{
		return line;
	}
	
	/**
	 * Checks if a point is within the rectangle defined by the two points
	 * of the CollisionLine
	 * @param point The point to check.
	 * @return Whether the point is in the rectangle or not.
	 */
	public boolean PointInLineRect(Vector2 point)
	{		
		return (((point.X >= mPoint1.X && point.X <= mPoint2.X)
				|| (point.X <= mPoint1.X && point.X >= mPoint2.X))
				&& ((point.Y >= mPoint1.Y && point.Y <= mPoint2.Y)
				|| (point.Y <= mPoint1.Y && point.Y >= mPoint2.Y)));
	}
	
	/**
	 * Finds the point of intersection (if there is one) between this CollisionLine
	 * and another.
	 * @param otherLine The other CollisionLine.
	 * @return The intersection point if one exists, otherwise null.
	 */
	public Vector2 GetIntersectionWithLine(CollisionLine otherLine)
	{
		Vector2 intersection = line.GetIntersectionPoint(otherLine.GetLine());
		
		if (intersection != null)
		{		
			if (PointInLineRect(intersection)
				&& otherLine.PointInLineRect(intersection))
			{
				return intersection;
			}
		}
		
		return null;
	}
	
	public void Draw()
	{
		glBegin(GL_LINES);
		{
			glVertex2f(mPoint1.X, mPoint1.Y);
			glVertex2f(mPoint2.X, mPoint2.Y);
		}
		glEnd();
	}
}
