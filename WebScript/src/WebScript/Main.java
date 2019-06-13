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
		//Boolean showOnlyNecessaryErrors = parser.getShowOnlyNecessaryErrors();
		
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
			if (verbose > 0)
			System.out.println("WebScript #" + wsCount++ + " (" + ws.getURL() + ")");
			
			ws.run();
		}
		
		System.exit(0);
	}
	
}
