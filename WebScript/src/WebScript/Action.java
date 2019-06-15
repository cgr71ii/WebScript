package WebScript;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

import WebScript.Checking.Checking;
import WebScript.Checking.CheckingReturn;
import WebScript.Do.Do;

public class Action
{
	
	private ArrayList<Checking> checking;
	
	private ArrayList<Do> _do;
	
	private WebDriver driver;
	
	private Boolean showOnlyNecessaryErrors;
	
	private Integer verbose;
	
	private Action()
	{
		this.driver = null;
		this.showOnlyNecessaryErrors = DefaultValues.SHOW_ONLY_NECESSARY_ERRORS;
		this.verbose = DefaultValues.VERBOSE;
	}

	public Action(Checking checking, Do _do)
	{
		this();
		
		this.checking = new ArrayList<>();
		this._do = new ArrayList<>();
		
		this.checking.add(checking);
		this._do.add(_do);
	}
	
	public Action(ArrayList<Checking> checking, ArrayList<Do> _do)
	{
		this();
		
		this.checking = checking;
		this._do = _do;
	}
	
	public final void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public final void setVerbose(Integer verbose)
	{
		this.verbose = verbose;
	}
	
	public final void setShowOnlyNecessaryErrors(Boolean showOnlyNecessaryErrors)
	{
		this.showOnlyNecessaryErrors = showOnlyNecessaryErrors;
	}
	
	public CheckingReturn perform() throws Exception
	{
		CheckingReturn checking = this.performChecking();
		
		if (checking == CheckingReturn.FALSE ||
			checking == CheckingReturn.SKIP)
		{
			return checking;
		}
		
		this.performMethod();
		
		return checking;
	}
	
	public CheckingReturn performChecking() throws Exception
	{
		if (this.checking == null)
		{
			// Skipping checking
			
			return CheckingReturn.TRUE;
		}
		
		Boolean skip = false;
		
		for (int i = 0; i < this.checking.size(); i++)
		{
			this.checking.get(i).setDriver(this.driver);
			
			try
			{
				CheckingReturn r = this.checking.get(i).check();
				
				if (r == CheckingReturn.FALSE ||
					r == CheckingReturn.EXCEPTION)
				{
					throw new Exception();
				}
				else if (r == CheckingReturn.SKIP)
				{
					skip = true;
				}
			}
			catch (Exception e)
			{
				System.out.printf("  %sSomething went wrong%s while performing the checking (Checking #%d).\n", AnsiColors.RED, AnsiColors.RESET, i + 1);
				
				throw new Exception();
			}
		}
		
		if (skip)
		{
			return CheckingReturn.SKIP;
		}
		
		return CheckingReturn.TRUE;
	}
	
	public void performMethod() throws Exception
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
			this._do.get(i).setShowOnlyNecessaryErrors(this.showOnlyNecessaryErrors);
			
			try
			{
				this._do.get(i).perform();
				
				done = true;
				
				if (this.verbose > 1)
				System.out.printf("    Concrete action #%d was executed %ssuccessfully%s!\n", i + 1, AnsiColors.GREEN, AnsiColors.RESET);
				
				break;
			}
			catch (Exception e)
			{
				if (!this.showOnlyNecessaryErrors && this.verbose > 1)
				System.out.printf("  %sSomething went wrong%s while performing the task (\"do\" node #%d).\n", AnsiColors.RED, AnsiColors.RESET, i + 1);
				
				//throw new Exception();
			}
		}
		
		if (!done)
		{
			if (!this.showOnlyNecessaryErrors && verbose > 0)
			System.out.println("  None concrete action could be executed...");
			
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
