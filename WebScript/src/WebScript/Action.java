package WebScript;

import org.openqa.selenium.WebDriver;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Do.Do;
import WebScript.Do.DoType;

public class Action
{
	
	private Checking checking;
	
	private Do _do;
	
	private WebDriver driver;

	public Action(Checking checking, Do _do)
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
	}
	
	private void performMethod() throws Exception
	{
		if (this._do == null)
		{
			// Skipping concrete action
			
			return;
		}
		
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
