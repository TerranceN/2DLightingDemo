package camera;

import gameBase.*;
import util.*;

import static org.lwjgl.opengl.GL11.*;

public class Camera
{
	GameWindow mGameWindow;
	Matrix mViewMatrix = Matrix.Identity();
	float mMasterSpeed = 5.0f;

	Matrix mTranslationMatrx = Matrix.Identity();
	Vector2 mTranslation = new Vector2();
	Vector2 mTranslateTo = new Vector2();
	float mTranslationSpeed = 1.0f;

	Matrix mScaleMatrix = Matrix.Identity();
	float mScale = 1;
	float mScaleTo = 1;
	float mScaleSpeed = 1.0f;

	Matrix mRotationMatrix = Matrix.Identity();
	float mRotation = 0;
	float mRotateTo = 0;
	float mRotationSpeed = 1.0f;

	public Camera(GameWindow newGame)
	{
		mGameWindow = newGame;
	}

	public Vector2 ScreenToWorld(Vector2 screenPoint)
	{
		return mViewMatrix.Inverse().Transform(screenPoint);
	}

	public Vector2 WorldToScreen(Vector2 worldPoint)
	{
		return mViewMatrix.Transform(worldPoint);
	}

	public void SetTranslationFocus(Vector2 focus)
	{
		mTranslateTo.SetEqual(focus);
	}

	public void SetScaleFocus(float focus)
	{
		mScaleTo = focus;
	}

	public void SetRotationFocus(float focus)
	{
		mRotateTo = (float)(focus % 2 * Math.PI);
	}

	public void SetScaleSpeed(float speed)
	{
		mScaleSpeed = speed;
	}

	public void ForceTranslationFocus(Vector2 focus)
	{
		SetTranslationFocus(focus);
		UpdateTranslation(1);
	}

	public void ForceScaleFocus(float focus)
	{
		SetScaleFocus(focus);
		UpdateScale(1);
	}

	public void ForceRotationFocus(float focus)
	{
		SetRotationFocus(focus);
		UpdateRotation(1);
	}

	public void UpdateTranslation(float speed)
	{
		Vector2 middleOfScreen = new Vector2(mGameWindow.Width(), mGameWindow.Height()).DividedBy(2);

		Vector2 middleOfScreenInWorld = ScreenToWorld(middleOfScreen);

		mTranslation.PlusEquals(mTranslateTo.Minus(middleOfScreenInWorld).Times(speed));

		mTranslationMatrx = Matrix.CreateTranslation(-mTranslation.X, -mTranslation.Y);
	}

	public void UpdateScale(float speed)
	{
		float difference = mScaleTo - mScale;

		mScale += difference * speed;

		if (mScale < 0.1f)
		{
			mScale = 0.1f;
		}

		if (mScale > 10)
		{
			mScale = 10;
		}

		mScaleMatrix = Matrix.CreateScale(mScale, mScale);
	}

	public void UpdateRotation(float speed)
	{
		/*float difference1 = (float)((mRotateTo - mRotation) % (2*Math.PI));
		  float difference2 = (float)((mRotation - mRotateTo) % (2*Math.PI));

		  if (difference1 < difference2)
		  {
		  mRotation += difference1 * mRotationSpeed * mMasterSpeed * gameTime.GetAverageUpdateTime() / 1000;
		  }
		  else 
		  {
		  mRotation -= difference2 * mRotationSpeed * mMasterSpeed * gameTime.GetAverageUpdateTime() / 1000;
		  }

		//mRotation = mRotateTo;*/

		float difference = mRotateTo - mRotation;

		float difference2;

		if (mRotateTo < mRotation)
		{
			difference2 = (float)(mRotateTo + Math.PI * 2) - mRotation;
		}
		else
		{
			difference2 = mRotateTo - (float)(mRotation + Math.PI * 2);
		}

		if (Math.abs(difference) < Math.abs(difference2))
		{
			mRotation += difference * speed;
		}
		else
		{
			mRotation += difference2 * speed;
		}

		mRotation = (float)(mRotation % (2*Math.PI));

		mRotationMatrix = Matrix.CreateRotationZ(mRotation);
	}

	public void UpdateViewMatrix()
	{
		// scale * rotation * translation
		Vector2 middleOfScreen = new Vector2(mGameWindow.Width(), mGameWindow.Height()).DividedBy(2);

		mViewMatrix = Matrix.CreateTranslation(middleOfScreen.X, middleOfScreen.Y)
			.Multiply(Matrix.CreateScale(mScale, mScale))
			.Multiply(Matrix.CreateRotationZ(mRotation))
			.Multiply(Matrix.CreateTranslation(-mTranslation.X, -mTranslation.Y));
	}

	public void Update(GameTime gameTime)
	{
		float baseSpeed = mMasterSpeed * gameTime.GetAverageUpdateTime() / 1000;

		UpdateTranslation(mTranslationSpeed * baseSpeed);
		UpdateScale(mScaleSpeed * baseSpeed);
		UpdateRotation(mRotationSpeed * baseSpeed);

		UpdateViewMatrix();
	}

	public Matrix GetViewMatrix()
	{
		return mViewMatrix;
	}

	public Vector2 GetTranslation()
	{
		return mTranslation;
	}

	public float GetRotation()
	{
		return mRotation;
	}

	public float GetScale()
	{
		return mScale;
	}

	public void Set()
	{
		glLoadMatrix(mViewMatrix.ToFloatBuffer());
	}
}
