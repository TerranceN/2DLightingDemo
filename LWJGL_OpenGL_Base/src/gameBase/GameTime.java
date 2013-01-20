package gameBase;

import java.util.*;

public class GameTime
{
	public final int NUM_UPDATE_TIMES = 20;
	
	private long lastUpdateTimer = -1;
	private int calibratedFPS = 60;
	private float actualFPS = 0;
	private float fpsDelay = 16.6f;
	private float speedFactor;
	private float averageUpdateTime = 0;
	private boolean paused = false;
	private ArrayList<Float> updateTimes = new ArrayList<Float>();
	private double runTime = 0;
	
	public GameTime(int newFps)
	{
		if (newFps > 0)
		{
			calibratedFPS = newFps;
			fpsDelay = 1000.0f / calibratedFPS;
		}
		
		for (int i = 0; i < NUM_UPDATE_TIMES; i++)
		{
			updateTimes.add(fpsDelay);
		}
	}
	
	private void AddNewUpdateTime(float newTime)
	{
		float addTime = newTime;
		
		if (addTime > 50)
		{
			addTime = 50;
		}
		
		if (updateTimes.size() >= NUM_UPDATE_TIMES)
		{
			updateTimes.remove(0);
		}
		
		updateTimes.add(newTime);
		
		runTime += newTime;
	}
	
	private float CalculateAverageUpdateTime()
	{
		float total = 0;
		
		for (int i = 0; i < updateTimes.size(); i++)
		{
			total += updateTimes.get(i);
		}
		
		return total / updateTimes.size();
	}
	
	public float GetAverageUpdateTime()
	{
		return averageUpdateTime;
	}
	
	public long GetRuntime()
	{
		return (long)runTime;
	}
	
	public float GetLastUpdateTime()
	{
		return updateTimes.get(updateTimes.size() - 1);
	}
	
	private float CalculateFPS()
	{
		return calibratedFPS / speedFactor;
	}
	
	public void Pause()
	{
		paused = true;
	}
	
	public void UnPause()
	{
		paused = false;
		lastUpdateTimer = -1;
	}
	
	public void Update()
	{
		if (!paused)
		{
			if (lastUpdateTimer == -1)
			{
				lastUpdateTimer = System.currentTimeMillis();
			}
			
			AddNewUpdateTime((System.currentTimeMillis() - lastUpdateTimer));
			averageUpdateTime = CalculateAverageUpdateTime();
			speedFactor = averageUpdateTime / fpsDelay;
			actualFPS = CalculateFPS();
			lastUpdateTimer = System.currentTimeMillis();
		}
	}
	
	public float GetSpeedFactor()
	{
		return speedFactor;
	}
	
	public float GetActualFPS()
	{
		return actualFPS;
	}
}
