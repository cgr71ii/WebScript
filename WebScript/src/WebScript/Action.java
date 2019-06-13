package WebScript;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Do.Do;
import WebScript.Do.DoType;

public class Action
{
	
	private ArrayList<Checking> checking;
	
	private ArrayList<Do> _do;
	
	private WebDriver driver;

	public Action(Checking checking, Do _do)
	{
		this.checking = new ArrayList<>();
		this._do = new ArrayList<>();
		
		this.checking.add(checking);
		this._do.add(_do);
		
		this.driver = null;
	}
	
	public Action(ArrayList<Checking> checking, ArrayList<Do> _do)
	{
		this.checking = checking;
		this._do = _do;
		
		this.driver = null;
	}
	
	public final void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public Boolean perform() throws Exception
	{
		Boolean checking = this.performChecking();
		
		if (!checking)
		{
			return checking;
		}
		
		this.performMethod();
		
		return checking;
	}
	
	private Boolean performChecking() throws Exception
	{
		if (this.checking == null)
		{
			// Skipping checking
			
			return true;
		}
		
		for (int i = 0; i < this.checking.size(); i++)
		{
			this.checking.get(i).setDriver(this.driver);
			
			try
			{
				if (!this.checking.get(i).check())
				{
					throw new Exception();
				}
			}
			catch (Exception e)
			{
				System.out.println("Something went wrong while performing the checking (Checking #" + (i + 1) + ").");
				
				throw new Exception();
			}
		}
		
		return true;
		
		/*
		this.checking.setDriver(this.driver);
		
		try
		{
			return this.checking.check();
		}
		catch (Exception e)
		{
			System.out.println("Something went wrong while performing the checking.");
			
			throw new Exception();
		}
		*/
	}
	
	private void performMethod() throws Exception
	{
		if (this._do == null)
		{
			// Skipping concrete action
			
			return;
		}
		
		Boolean done = false;
		
		for (int i = 0; i < this._do.size(); i++)
		{
			this._do.get(i).setDriver(this.driver);
			
			try
			{
				this._do.get(i).perform();
				
				done = true;
				
				break;
			}
			catch (Exception e)
			{
				System.out.println("Something went wrong while performing the task (\"do\" node #" + (i + 1) + ").");
				
				//throw new Exception();
			}
		}
		
		if (!done)
		{
			throw new Exception();
		}
		
		/*
		this._do.setDriver(this.driver);
		
		try
		{
			this._do.perform();
		}
		catch (Exception e)
		{
			System.out.println("Something went wrong while performing the task (\"do\" node).");
			
			throw new Exception();
		}
		*/
	}
	
	@Override
	public String toString()
	{
		if (this.checking == null && this._do == null)
		{
			return "[ACTION]{}";
		}
		else if (this.checking == null)
		{
			return "[ACTION]{Action: " + this._do.toString() + "}";
		}
		else if (this._do == null)
		{
			return "[ACTION]{Checking: " + this.checking.toString() + "}";
		}
		else
		{
			return "[ACTION]{Checking: " + this.checking.toString() + "; Action: " + this._do.toString() + "}";			
		}
	}
	
}
