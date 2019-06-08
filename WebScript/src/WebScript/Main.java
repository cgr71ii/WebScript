package WebScript;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Main
{
	
	public static void printErrorMessageAndExit(int status)
	{
		System.err.println("Syntax: java -jar WebScript.jar configuration.xml");
		System.exit(status);
	}
	
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			printErrorMessageAndExit(1); 
		}
		
		XMLParser parser = null;
		
		try
		{
			parser = new XMLParser(args[0]);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File " + args[0] + " not found...");
			
			System.exit(1);
		}
		catch(Exception e)
		{
			System.err.println("Something went wrong when creating Parsing object...");
			
			System.exit(1);
		}
		
		int wsCount = 1;
		
		try
		{
			parser.parse();
		}
		catch(Exception e)
		{
			System.err.println("Parsing failed: XML file (" + args[0] + ") contains errors.");
			
			System.exit(1);
		}
		
		System.out.println("XML document parsed!");
		
		ArrayList<WebScript> webScripts = parser.getWebScripts();
		
		if (webScripts.size() == 0)
		{
			System.err.println("XML File (" + args[0] + ") has not any web script!");
			
			System.exit(1);
		}
		
		for (WebScript ws : webScripts)
		{
			int actionCount = 1;
			ArrayList<Action> actions = ws.getActions();
			
			System.out.println("WebScript #" + wsCount++);
			
			if (actions.size() == 0)
			{
				System.err.println("This web script has not actions... skipping.");
				
				continue;
			}
			
			for (Action a : actions)
			{
				System.out.println("Action #" + actionCount++);
				
				if (!a.perform())
				{
					System.out.println("The checking was not successfully.");
				}
			}
		}
		
		System.exit(0);
	}
	
}
