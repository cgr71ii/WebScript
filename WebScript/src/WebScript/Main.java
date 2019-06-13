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
		System.out.println("Syntax: java -jar WebScript.jar configuration.xml");
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
			System.out.println("File " + args[0] + " not found...");
			
			System.exit(1);
		}
		catch(Exception e)
		{
			System.out.println("Something went wrong when creating Parsing object...");
			
			System.exit(1);
		}
		
		int wsCount = 1;
		
		try
		{
			parser.parse();
		}
		catch(Exception e)
		{
			System.out.println("Parsing failed: XML file (" + args[0] + ") contains errors.");
			
			System.exit(1);
		}
		
		Integer verbose = parser.getVerbose();
		
		if (verbose > 0)
		System.out.println("XML document parsed!\n");
		
		ArrayList<WebScript> webScripts = parser.getWebScripts();
		
		if (webScripts.size() == 0)
		{
			System.out.println("XML File (" + args[0] + ") has not any web script!");
			
			System.exit(1);
		}
		
		for (WebScript ws : webScripts)
		{
			int actionCount = 1;
			ArrayList<Action> actions = ws.getActions();
			
			if (verbose > 0)
			System.out.println("WebScript #" + wsCount++ + " (" + ws.getURL() + ")");
			
			if (actions.size() == 0)
			{
				System.out.printf("  - This web script %shas not actions%s... skipping.\n", AnsiColors.RED, AnsiColors.RESET);
				
				continue;
			}
			
			Boolean error = false;
			
			for (Action a : actions)
			{
				if (verbose > 1)
				System.out.println("  Action #" + actionCount++);
				
				try
				{
					if (!a.perform())
					{
						System.out.printf("    - The checking was %snot successfully%s at action #%d... skipping.\n", AnsiColors.RED, AnsiColors.RESET, actionCount - 1);
						
						error = true;
						
						break;
					}
				}
				catch (Exception e)
				{
					System.out.printf("    - %sAborting%s current WebScript.\n", AnsiColors.RED, AnsiColors.RESET);
					
					error = true;
					
					break;
				}
			}
			
			if (!error)
			{
				if (verbose > 0)
				System.out.printf("  The current web script finished %ssuccessfully%s!\n", AnsiColors.GREEN, AnsiColors.RESET);
			}
		}
		
		System.exit(0);
	}
	
}
