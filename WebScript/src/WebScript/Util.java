package WebScript;

import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import WebScript.Locator.Position;

public class Util
{
	
	public static ArrayList<Node> getChildNodes(Node node) throws Exception
	{
		ArrayList<Node> res = new ArrayList<>();
		NodeList list = node.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++)
		{
			Node item = list.item(i);
			
			if (item.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			res.add(item);
		}
		
		return res;
	}
	
	public static ArrayList<Node> getChildNodes(Node node, String[] nodes, Boolean onlyAndOnly) throws Exception
	{
		ArrayList<Node> res = new ArrayList<>();
		NodeList list = node.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++)
		{
			Node item = list.item(i);
			
			if (item.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			if (!Arrays.asList(nodes).contains(item.getNodeName()))
			{
				if (onlyAndOnly)
				{
					System.out.printf("  Node \"%s\" was not expected for node \"%s\".\n", item.getNodeName(), node.getNodeName());
					
					throw new Exception();
				}
				
				continue;
			}
			
			res.add(item);
		}
		
		if (res.size() != nodes.length)
		{
			System.out.println("  Not all nodes expected for \"" + node.getNodeName() + "\" found! Expected:");
			
			for (String s : nodes)
			{
				System.out.println("   - " + s);
			}
			
			throw new Exception();
		}
		
		return res;
	}
	
	public static WebElement getWebElement(WebDriver driver, Position position) throws Exception
	{
		return Util.getWebElement(driver, position, false);
	}
	
	public static WebElement getWebElement(WebDriver driver, Position position, Boolean showOnlyNecessaryErrors) throws Exception
	{
		try
		{
			WebElement element = driver.findElement(position.getBy());
			
			return element;
		}
		catch (NoSuchElementException e)
		{
			if (!showOnlyNecessaryErrors)
			System.out.printf("  Element with locator = %s not found.\n", position.toString());
			
			throw new Exception();
		}
		catch (Exception e)
		{
			if (!showOnlyNecessaryErrors)
			System.out.println("  Something went wrong while trying to get a WebElement.");
			
			throw new Exception();
		}
	}
	
	public static void turnOffWarnings()
	{
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	}
	
}
