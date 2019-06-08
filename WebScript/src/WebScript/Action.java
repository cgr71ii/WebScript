package WebScript;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Do.Do;
import WebScript.Do.DoType;

public class Action
{
	
	private Checking checking;
	
	private Do _do;

	public Action(Checking checking, Do _do)
	{
		this.checking = checking;
		this._do = _do;
	}
	
	public Boolean perform()
	{
		Boolean checking = this.performChecking();
		
		if (!checking)
		{
			return checking;
		}
		
		this.performMethod();
		
		return checking;
	}
	
	private Boolean performChecking()
	{
		if (this.checking == null)
		{
			// Skipping checking
			
			return true;
		}
		
		try
		{
			return this.checking.check();
		}
		catch (Exception e)
		{
			System.err.println("Something went wrong while performing the checking.");
			
			return false;
		}
	}
	
	private void performMethod()
	{
		if (this._do == null)
		{
			// Skipping concrete action
			
			return;
		}
		
		try
		{
			this._do.perform();
		}
		catch (Exception e)
		{
			System.err.println("Something went wrong while performing the task (\"do\" node).");
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
