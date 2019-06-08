package WebScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import WebScript.Checking.Checking;
import WebScript.Do.Do;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLParser
{
	
	private String path;
	
	private File file;
	
	private DocumentBuilderFactory dbFactory;
	
	private DocumentBuilder dBuilder;
	
	private Document doc;
	
	private ArrayList<WebScript> wsArray;
	
	public XMLParser(String path) throws Exception
	{
		this.dbFactory = DocumentBuilderFactory.newInstance();
		this.path = new String(path);
		this.file = new File(this.path);
		this.dBuilder = this.dbFactory.newDocumentBuilder();
		this.wsArray = new ArrayList<>();
		
		if (!this.file.exists())
		{
			throw new FileNotFoundException();
		}
	}
	
	public ArrayList<WebScript> getWebScripts()
	{
		return this.wsArray;
	}
	
	public void parse() throws Exception
	{
		this.doc = this.dBuilder.parse(this.file);
		
		this.doc.getDocumentElement().normalize();
		
		NodeList ws = doc.getElementsByTagName("WebScript");
		
		for (int i = 0; i < ws.getLength(); i++)
		{
			Node item = ws.item(i);
			
			if (item.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			NodeList wsItems = item.getChildNodes();
			WebScript webScript = null;
			String url = null;
			Boolean skipWebScript = false;
			
			for (int j = 0; j < wsItems.getLength(); j++)
			{
				Node wsItem = wsItems.item(j);
				Action action = null;
				
				if (wsItem.getNodeType() != Node.ELEMENT_NODE)
				{
					continue;
				}
				
				if (wsItem.getNodeName().equals("url"))
				{
					url = new String(wsItem.getTextContent());
					
					try
					{
						webScript = new WebScript(url);
					}
					catch (Exception e)
					{
						System.err.println("Skipping WebScript with URL = \"" + url + "\".");
						
						skipWebScript = true;
						
						break;
					}
				}
				else if (wsItem.getNodeName().equals("action"))
				{
					if (url == null)
					{
						System.err.println("The \"url\" node has to be the first node when creating a WebScript.");
						
						throw new Exception();
					}
					
					action = this.parseAction(wsItem);
					
					//System.out.println("Action: " + action.toString());
					
					webScript.addAction(action);
				}
			}
			
			if (skipWebScript)
			{
				continue;
			}
			
			if (url == null || url.isEmpty())
			{
				System.err.println("WebScript has to have, at least, the node \"url\" set.");
				
				throw new Exception();
			}
			
			System.out.println("URL = " + url);
			
			this.wsArray.add(webScript);
			
			//Node nUrl = wsItems.item(0);
			
			//System.out.println("URL node name: " + nUrl.getNodeName());
			
		}
	}
	
	private Action parseAction(Node aNode) throws Exception
	{
		NodeList aItems = aNode.getChildNodes();
		Checking checking = new Checking();
		Do _do = new Do();
		Boolean checkingFound = false, doFound = false;
		
		for (int i = 0; i < aItems.getLength(); i++)
		{
			Node aItem = aItems.item(i);
			
			if (aItem.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			if (aItem.getNodeName().equals("checking"))
			{
				checking = checking.parse(aItem);
				
				checking.parse(aItem);
				
				System.out.println("Checking: " + checking.toString());
				
				checkingFound = true;
			}
			else if (aItem.getNodeName().equals("do"))
			{
				_do = _do.parse(aItem);
				
				_do.parse(aItem);
				
				System.out.println("Do: " + _do.toString());
				
				doFound = true;
			}
		}
		
		if (!checkingFound)
		{
			checking = null;
		}
		if (!doFound)
		{
			_do = null;
		}
		
		Action action = new Action(checking, _do);
		
		return action;
	}
	
}
