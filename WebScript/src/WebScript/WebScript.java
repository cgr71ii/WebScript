package WebScript;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import WebScript.Checking.CheckingReturn;

public class WebScript
{

	private WebDriver driver;
	
	private ArrayList<Action> actions;
	
	private String url;
	
	private Integer verbose;
	
	private Boolean showOnlyNecessaryErrors;
	
	public WebScript(String url) throws Exception
	{
		this.url = new String(url);
		this.driver = new HtmlUnitDriver();
		this.actions = new ArrayList<>();
		this.verbose = DefaultValues.VERBOSE;
		this.showOnlyNecessaryErrors = DefaultValues.SHOW_ONLY_NECESSARY_ERRORS;
		
		Util.turnOffWarnings();
		
		// Loads the page
		try
		{
			this.driver.get(this.url);
		}
		catch(Exception e)
		{
			System.out.println("Could not load the page \"" + this.url + "\".");
			
			throw new Exception();
		}
	}
	
	public void setVerbose(Integer verbose)
	{
		this.verbose = verbose;
	}
	
	public void setShowOnlyNecessaryErrors(Boolean showOnlyNecessaryErrors)
	{
		this.showOnlyNecessaryErrors = showOnlyNecessaryErrors;
	}
	
	public String getURL()
	{
		return this.url;
	}
	
	public ArrayList<Action> getActions()
	{
		return this.actions;
	}
	
	public void addAction(Action action)
	{
		action.setDriver(this.driver);
		
		this.actions.add(action);
	}
	
	public Boolean run()
	{
		if (this.actions.size() == 0)
		{
			System.out.printf("  - This web script %shas not actions%s... skipping.\n", AnsiColors.RED, AnsiColors.RESET);
			
			return false;
		}
		
		Boolean error = false;
		Integer actionCount = 1;
		
		for (Action a : this.actions)
		{
			a.setVerbose(this.verbose);
			a.setShowOnlyNecessaryErrors(this.showOnlyNecessaryErrors);
			
			if (this.verbose > 1)
			System.out.println("  Action #" + actionCount++);
			
			try
			{
				CheckingReturn checking = a.performChecking();
				
				if (checking == CheckingReturn.FALSE)
				{
					System.out.printf("    - The checking was %snot successfully%s at action #%d... skipping.\n", AnsiColors.RED, AnsiColors.RESET, actionCount - 1);
					
					error = true;
					
					// Stop the current web script -> checking failed
					break;
				}
				else if (checking == CheckingReturn.SKIP)
				{
					if (verbose > 1)
					System.out.println("    Skipping current action due to checking.");
					
					continue;
				}
			}
			catch (Exception e)
			{
				System.out.printf("    - %sAborting%s current WebScript.\n", AnsiColors.RED, AnsiColors.RESET);
				
				error = true;
				
				break;
			}
			
			try
			{
				a.performMethod();
			}
			catch (Exception e2)
			{
				if (!a.getContinueIfFailsMethod())
				{
					System.out.printf("    - %sAborting%s current WebScript.\n", AnsiColors.RED, AnsiColors.RESET);
					
					error = true;
					
					break;
				}
				else
				{
					if (verbose > 1)
					System.out.println("    Skipping current action.");
					
					continue;
				}
			}
		}
		
		if (!error)
		{
			if (this.verbose > 0)
			System.out.printf("  The current web script %sfinished successfully%s!\n", AnsiColors.GREEN, AnsiColors.RESET);
			
			return true;
		}
		
		System.out.printf("  The current web script %snot finished successfully%s...\n", AnsiColors.RED, AnsiColors.RESET);
		
		return false;
	}
	
	public void quitDriver()
	{
		if (this.driver != null)
		{
			this.driver.close();
			this.driver.quit();
		}
	}
	
}
