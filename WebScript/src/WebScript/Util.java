package WebScript;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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
	
	public static int checkIfViuIsInstalled()
	{
		return Util.executeCommand("viu --help", false);
		/*
		Runtime rt = Runtime.getRuntime();
		Process proc;
		int exit;
		
		try
		{
			proc = rt.exec("viu --help");
			
			exit = proc.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			exit = 1;
		}
		
		return exit;
		*/
	}
	
	public static Boolean downloadImage(String src, String dst)
	{
		URL url;
		
		try
		{
			url = new URL(src);
			//String fileName = url.getFile();
			
			//dst += fileName.substring(fileName.lastIndexOf("/")).lastIndexOf(".");
			
			//System.out.println(dst);
		 
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(dst);
			
			byte[] b = new byte[2048];
			int length;
		 
			while ((length = is.read(b)) != -1)
			{
				os.write(b, 0, length);
			}
		 
			is.close();
			os.close();
		}
		catch (IOException e)
		{
			System.out.println("  Something went wrong while downloading the image.");
			
			return false;
		}
		
		return true;
	}
	
	public static int executeCommand(String cmd, Boolean showContent)
	{
		return Util.executeCommand(cmd, showContent, null, null, false);
	}
	
	public static int executeCommand(String cmd, Boolean showContent, Integer maxLines, Integer milliseconds, Boolean forceSuccessful)
	{
		Runtime rt = Runtime.getRuntime();
		Process proc;
		int exit;
		
		try
		{
			proc = rt.exec(cmd);
			
			if (showContent)
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line;
				
				if (maxLines == null || maxLines < 0)
				{
					while ((line = reader.readLine()) != null)
					{
						System.out.println("    " + line);
					}
				}
				else
				{
					for (int i = 0; i < maxLines; i++)
					{
						line = reader.readLine();
						
						if (line == null)
						{
							break;
						}
						
						System.out.println("    " + line);
					}
				}
			}
			
			if (milliseconds == null || milliseconds < 0)
			{
				exit = proc.waitFor();
			}
			else
			{
				if (proc.waitFor(milliseconds, TimeUnit.MILLISECONDS))
				{
					exit = 0;
				}
				else
				{
					if (!forceSuccessful)
					System.out.println("  Something went wrong while running the command (ran out of time).");
					
					exit = 1;
					
					if (forceSuccessful)
					exit = 0;
				}
			}
		}
		catch (IOException | InterruptedException e)
		{
			System.out.println("  Something went wrong while running the command.");
			
			exit = 1;
		}
		
		return exit;
	}
	
}
