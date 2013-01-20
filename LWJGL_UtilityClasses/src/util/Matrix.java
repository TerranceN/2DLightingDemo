package util;

import org.lwjgl.BufferUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.*;

public class Matrix
{
	private float[][] mElements;
	
	public static Matrix Identity()
	{
		Matrix mat = new Matrix();
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				if (m == n)
					mat.Set(m, n, 1);
			}
		}
		
		return mat;
	}
	
	public static Matrix CreateTranslation3(float x, float y, float z)
	{
		Matrix mat = Matrix.Identity();
		
		mat.Set(0, 3, x);
		mat.Set(1, 3, y);
		mat.Set(2, 3, z);
		
		return mat;
	}
	
	public static Matrix CreateTranslation(float x, float y)
	{
		Matrix mat = Matrix.Identity();
		
		mat.Set(0, 3, x);
		mat.Set(1, 3, y);
		
		return mat;
	}
	
	public static Matrix CreateScale(float scaleX, float scaleY)
	{
		Matrix mat = Matrix.Identity();
		
		mat.Set(0, 0, scaleX);
		mat.Set(1, 1, scaleX);
		
		return mat;
	}
	
	public static Matrix CreateRotationX(float angle)
	{
		Matrix mat = Matrix.Identity();
		
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		
		mat.Set(1, 1, cos);
		mat.Set(2, 1, sin);
		mat.Set(1, 2, -sin);
		mat.Set(2, 2, cos);
		
		return mat;
	}
	
	public static Matrix CreateRotationY(float angle)
	{
		Matrix mat = Matrix.Identity();
		
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		
		mat.Set(0, 0, cos);
		mat.Set(0, 2, sin);
		mat.Set(2, 0, -sin);
		mat.Set(2, 2, cos);
		
		return mat;
	}
	
	public static Matrix CreateRotationZ(float angle)
	{
		Matrix mat = Matrix.Identity();
		
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		
		mat.Set(0, 0, cos);
		mat.Set(1, 0, sin);
		mat.Set(0, 1, -sin);
		mat.Set(1, 1, cos);
		
		return mat;
	}
	
	public static Matrix FromArray(float[] array)
	{
		Matrix mat = new Matrix();
		
		for (int n = 0; n < 4; n++)
		{
			for (int m = 0; m < 4; m++)
			{
				mat.Set(m, n, array[n * 4 + m]);
			}
		}
		
		return mat;
	}
	
	public Matrix()
	{
		InitElements();
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				mElements[m][n] = 0;
			}
		}
	}
	
	public Matrix Copy()
	{
		Matrix mat = new Matrix();
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				mat.Set(m, n, mElements[m][n]);
			}
		}
		
		return mat;
	}
	
	public Vector2 Forwards()
	{
		return new Vector2(-Get(1, 0), -Get(0, 0));
	}
	
	public Vector2 Backwards()
	{
		return new Vector2(Get(1, 0), Get(0, 0));
	}
	
	public Vector2 Left()
	{
		return new Vector2(-Get(1, 1), -Get(0, 1));
	}
	
	public Vector2 Right()
	{
		return new Vector2(Get(1, 1), Get(0, 1));
	}
	
	public Matrix Multiply(Matrix other)
	{
		Matrix mat = Copy();
		
		mat.MultiplyBy(other);
		
		return mat;
	}
	
	public void MultiplyBy(Matrix other)
	{
		Matrix mat = Matrix.Identity();
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				float total = 0;
				
				for (int i = 0; i < 4; i++)
				{
					total += Get(m, i) * other.Get(i, n);
				}
				
				mat.Set(m, n, total);
			}
		}
		
		SetEqual(mat);
	}
	
	public void SetEqual(Matrix other)
	{
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				Set(m, n, other.Get(m, n));
			}
		}
	}
	
	private void InitElements()
	{
		mElements = new float[4][4];
	}
	
	public float Get(int m, int n)
	{
		return mElements[m][n];
	}
	
	public void Set(int m, int n, float value)
	{
		mElements[m][n] = value;
	}
	
	public float[] ToArray()
	{
		float[] arr = new float[16];
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				arr[n * 4 + m] = mElements[m][n];
			}
		}
		
		return arr;
	}
	
	public FloatBuffer ToFloatBuffer()
	{
		FloatBuffer buf = BufferUtils.createFloatBuffer(16);
		
		for (int m = 0; m < 4; m++)
		{
			for (int n = 0; n < 4; n++)
			{
				buf.put(mElements[n][m]);
			}
		}
		
		buf.flip();
		
		return buf;
	}
	
	public void Transpose()
	{
		for (int n = 0; n < 4; n++)
		{
			for (int m = 0; m < n; m++)
			{
				float temp = Get(m, n);
				Set(m, n, Get(n, m));
				Set(n, m, temp);
			}
		}
	}
	
	public Matrix GetTransposed()
	{
		Matrix mat = Copy();
		mat.Transpose();
		return mat;
	}
	
	public Vector2 Transform(Vector2 src)
	{
		Vector2 vector = new Vector2();
		
		vector.X = Get(0, 0) * src.X + Get(0, 1) * src.Y + Get(0, 3);
		vector.Y = Get(1, 0) * src.X + Get(1, 1) * src.Y + Get(1, 3);
		
		return vector;
	}
	
	public void Print(PrintStream s)
	{
		for (int m = 0; m < 4; m++)
		{
			String temp = "";
			
			for (int n = 0; n < 4; n++)
			{
				temp += Float.toString(Get(m, n));
				
				if (n!=3)
				{
					temp += ", ";
				}
			}
			
			s.println(temp);
		}
		
		s.println();
		s.println();
	}
	
	public Matrix Inverse()
	{
		float[] mFinished = new float[16];
		float[] mEntry = ToArray();
		
		float a0 = mEntry[ 0]*mEntry[ 5] - mEntry[ 1]*mEntry[ 4];
		float a1 = mEntry[ 0]*mEntry[ 6] - mEntry[ 2]*mEntry[ 4];
		float a2 = mEntry[ 0]*mEntry[ 7] - mEntry[ 3]*mEntry[ 4];
		float a3 = mEntry[ 1]*mEntry[ 6] - mEntry[ 2]*mEntry[ 5];
		float a4 = mEntry[ 1]*mEntry[ 7] - mEntry[ 3]*mEntry[ 5];
	    float a5 = mEntry[ 2]*mEntry[ 7] - mEntry[ 3]*mEntry[ 6];
	    float b0 = mEntry[ 8]*mEntry[13] - mEntry[ 9]*mEntry[12];
	    float b1 = mEntry[ 8]*mEntry[14] - mEntry[10]*mEntry[12];
	    float b2 = mEntry[ 8]*mEntry[15] - mEntry[11]*mEntry[12];
	    float b3 = mEntry[ 9]*mEntry[14] - mEntry[10]*mEntry[13];
	    float b4 = mEntry[ 9]*mEntry[15] - mEntry[11]*mEntry[13];
	    float b5 = mEntry[10]*mEntry[15] - mEntry[11]*mEntry[14];

	    float det = a0*b5 - a1*b4 + a2*b3 + a3*b2 - a4*b1 + a5*b0;
	    
	    if (Math.abs(det) > 0.0001f)
	    {
	    	mFinished[ 0] = + mEntry[ 5]*b5 - mEntry[ 6]*b4 + mEntry[ 7]*b3;
	        mFinished[ 4] = - mEntry[ 4]*b5 + mEntry[ 6]*b2 - mEntry[ 7]*b1;
	        mFinished[ 8] = + mEntry[ 4]*b4 - mEntry[ 5]*b2 + mEntry[ 7]*b0;
	        mFinished[12] = - mEntry[ 4]*b3 + mEntry[ 5]*b1 - mEntry[ 6]*b0;
	        mFinished[ 1] = - mEntry[ 1]*b5 + mEntry[ 2]*b4 - mEntry[ 3]*b3;
	        mFinished[ 5] = + mEntry[ 0]*b5 - mEntry[ 2]*b2 + mEntry[ 3]*b1;
	        mFinished[ 9] = - mEntry[ 0]*b4 + mEntry[ 1]*b2 - mEntry[ 3]*b0;
	        mFinished[13] = + mEntry[ 0]*b3 - mEntry[ 1]*b1 + mEntry[ 2]*b0;
	        mFinished[ 2] = + mEntry[13]*a5 - mEntry[14]*a4 + mEntry[15]*a3;
	        mFinished[ 6] = - mEntry[12]*a5 + mEntry[14]*a2 - mEntry[15]*a1;
	        mFinished[10] = + mEntry[12]*a4 - mEntry[13]*a2 + mEntry[15]*a0;
	        mFinished[14] = - mEntry[12]*a3 + mEntry[13]*a1 - mEntry[14]*a0;
	        mFinished[ 3] = - mEntry[ 9]*a5 + mEntry[10]*a4 - mEntry[11]*a3;
	        mFinished[ 7] = + mEntry[ 8]*a5 - mEntry[10]*a2 + mEntry[11]*a1;
	        mFinished[11] = - mEntry[ 8]*a4 + mEntry[ 9]*a2 - mEntry[11]*a0;
	        mFinished[15] = + mEntry[ 8]*a3 - mEntry[ 9]*a1 + mEntry[10]*a0;

	        float invDet = ((float)1)/det;
	        mFinished[ 0] *= invDet;
	        mFinished[ 1] *= invDet;
	        mFinished[ 2] *= invDet;
	        mFinished[ 3] *= invDet;
	        mFinished[ 4] *= invDet;
	        mFinished[ 5] *= invDet;
	        mFinished[ 6] *= invDet;
	        mFinished[ 7] *= invDet;
	        mFinished[ 8] *= invDet;
	        mFinished[ 9] *= invDet;
	        mFinished[10] *= invDet;
	        mFinished[11] *= invDet;
	        mFinished[12] *= invDet;
	        mFinished[13] *= invDet;
	        mFinished[14] *= invDet;
	        mFinished[15] *= invDet;
	        
	        return Matrix.FromArray(mFinished);
	    }
	    
	    System.out.println("Inverse Failed!");
	    
	    return null;
	}
}
