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
			throw new FileNotFoundException("File " + path + " not found");
		}
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
			
			for (int j = 0; j < wsItems.getLength(); j++)
			{
				Node wsItem = wsItems.item(j);
				Action action = null;
				
				if (wsItem.getNodeType() != Node.ELEMENT_NODE)
				{
					continue;
				}
				
				if (wsItem.getNodeName() == "url")
				{
					url = new String(wsItem.getTextContent());
					
					webScript = new WebScript(url);
				}
				else if (wsItem.getNodeName() == "action")
				{
					if (url == null)
					{
						System.err.println("The \"url\" node has to be the first node when creating a WebScript.");
						
						throw new Exception();
					}
					
					action = this.parseAction(wsItem);
					webScript.addAction(action);
				}
			}
			
			if (url == null || url.isEmpty())
			{
				System.err.println("WebScript has to have, at least, the node \"url\" set.");
				
				throw new Exception();
			}
			
			System.out.println("URL = " + url);
			
			//Node nUrl = wsItems.item(0);
			
			//System.out.println("URL node name: " + nUrl.getNodeName());
			
		}
	}
	
	private Action parseAction(Node aNode) throws Exception
	{
		NodeList aItems = aNode.getChildNodes();
		Checking checking = new Checking();
		
		for (int i = 0; i < aItems.getLength(); i++)
		{
			Node aItem = aItems.item(i);
			
			if (aItem.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			if (aItem.getNodeName() == "checking")
			{
				checking = checking.parse(aItem);
				
				checking.parse(aItem);
				
				System.out.println("Checking: " + checking.toString());
			}
			else if (aItem.getNodeName() == "do")
			{
				
			}
		}
		
		//Action action = new Action();
		Action action = null;	// TODO erase me
		
		return action;
	}
	
}
