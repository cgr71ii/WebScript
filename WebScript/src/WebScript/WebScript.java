package WebScript;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class WebScript
{

	private WebDriver driver;
	
	private ArrayList<Action> actions;
	
	private Integer actionIndex;
	
	private String url;
	
	public WebScript(String url) throws Exception
	{
		this.url = new String(url);
		this.driver = new HtmlUnitDriver();
		this.actions = new ArrayList<>();
		this.actionIndex = 0;
		
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
}
