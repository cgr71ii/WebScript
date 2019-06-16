package WebScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import WebScript.Checking.Checking;
import WebScript.Do.Do;

import org.w3c.dom.Node;

public class XMLParser
{
	
	private String path;
	
	private File file;
	
	private DocumentBuilderFactory dbFactory;
	
	private DocumentBuilder dBuilder;
	
	private Document doc;
	
	private ArrayList<WebScript> wsArray;
	
	private Integer verbose;
	
	private Boolean showOnlyNecessaryErrors;
	
	public XMLParser(String path) throws Exception
	{
		this.dbFactory = DocumentBuilderFactory.newInstance();
		this.path = new String(path);
		this.file = new File(this.path);
		this.dBuilder = this.dbFactory.newDocumentBuilder();
		this.wsArray = new ArrayList<>();
		this.verbose = DefaultValues.VERBOSE;
		this.showOnlyNecessaryErrors = DefaultValues.SHOW_ONLY_NECESSARY_ERRORS;
		
		if (!this.file.exists())
		{
			throw new FileNotFoundException();
		}
	}
	
	public Integer getVerbose()
	{
		return this.verbose;
	}
	
	public Boolean getShowOnlyNecessaryErrors()
	{
		return this.showOnlyNecessaryErrors;
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
		NodeList v = doc.getElementsByTagName("verbose");
		NodeList sone = doc.getElementsByTagName("showOnlyNecessaryErrors");
		
		if (v.getLength() > 1)
		{
			System.out.println("The \"verbose\" node can only appear once.");
			
			throw new Exception();
		}
		if (sone.getLength() > 1)
		{
			System.out.println("The \"showOnlyNecessaryErrors\" node can only appear once.");
			
			throw new Exception();
		}
		
		if (v.getLength() == 1)
		{
			try
			{
				this.verbose = Integer.parseInt(v.item(0).getTextContent());
				
				if (this.verbose < DefaultValues.VERBOSE_MIN || this.verbose > DefaultValues.VERBOSE_MAX)
				{
					this.verbose = DefaultValues.VERBOSE;
				}
			}
			catch (Exception e)
			{
				this.verbose = DefaultValues.VERBOSE;
			}
		}
		if (sone.getLength() == 1)
		{
			try
			{
				this.showOnlyNecessaryErrors = Boolean.parseBoolean(sone.item(0).getTextContent());
			}
			catch (Exception e)
			{
				this.showOnlyNecessaryErrors = DefaultValues.SHOW_ONLY_NECESSARY_ERRORS;
			}
		}
		
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
						System.out.println("Skipping WebScript with URL = \"" + url + "\".");
						
						skipWebScript = true;
						
						break;
					}
				}
				else if (wsItem.getNodeName().equals("action"))
				{
					if (url == null)
					{
						System.out.println("The \"url\" node has to be the first node when creating a WebScript.");
						
						throw new Exception();
					}
					
					action = this.parseAction(wsItem);
					
					//System.out.println("Action: " + action.toString());
					
					webScript.addAction(action);
				}
				else
				{
					if (!this.showOnlyNecessaryErrors && this.verbose > 1)
					System.out.println("Node \"" + wsItem.getNodeName() + "\" unexpected, but still parsing...");
				}
			}
			
			if (skipWebScript)
			{
				continue;
			}
			
			if (url == null || url.isEmpty())
			{
				System.out.println("WebScript has to have, at least, the node \"url\" set.");
				
				throw new Exception();
			}
			
			//System.out.println("URL = " + url);
			
			webScript.setVerbose(this.verbose);
			webScript.setShowOnlyNecessaryErrors(this.showOnlyNecessaryErrors);
			
			this.wsArray.add(webScript);
			
			//Node nUrl = wsItems.item(0);
			
			//System.out.println("URL node name: " + nUrl.getNodeName());
			
		}
	}
	
	private Action parseAction(Node aNode) throws Exception
	{
		NodeList aItems = aNode.getChildNodes();
		ArrayList<Checking> alChecking = new ArrayList<>();
		ArrayList<Do> alDo = new ArrayList<>();
		Boolean checkingFound = false, doFound = false;
		Boolean doRunAny = DefaultValues.DO_RUN_ANY;
		Boolean continueIfFailsMethod = DefaultValues.CONTINUE_IF_FAILS_METHOD;
		Boolean continueIfFailsChecking = DefaultValues.CONTINUE_IF_FAILS_CHECKING;
		
		for (int i = 0; i < aItems.getLength(); i++)
		{
			Checking checking = new Checking();
			Do _do = new Do();
			Node aItem = aItems.item(i);
			
			if (aItem.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			if (aItem.getNodeName().equals("checking"))
			{
				checking = checking.parse(aItem);
				
				checking.parse(aItem);
				
				alChecking.add(checking);
				
				checkingFound = true;
			}
			else if (aItem.getNodeName().equals("do"))
			{
				_do = _do.parse(aItem);
				
				_do.parse(aItem);
				
				alDo.add(_do);
				
				doFound = true;
			}
			else if (aItem.getNodeName().equals("doRun"))
			{
				String doRunAnyAux = aItem.getTextContent();
				
				switch (doRunAnyAux.toLowerCase())
				{
					case "all":
						doRunAny = false;
						break;
					case "any":
						doRunAny = true;
						break;
					default:
						System.out.println("Node \"doRun\" can only be:");
						System.out.println("  - any");
						System.out.println("  - all");
						
						throw new Exception();
				}
			}
			else if (aItem.getNodeName().equals("continueIfFailsMethod"))
			{
				String continueIfFailsMethodAux = aItem.getTextContent();
				
				switch (continueIfFailsMethodAux.toLowerCase())
				{
					case "true":
						continueIfFailsMethod = true;
						break;
					case "false":
						continueIfFailsMethod = false;
						break;
					default:
						System.out.println("Node \"continueIfFailsMethod\" can only be:");
						System.out.println("  - true");
						System.out.println("  - false");
						
						throw new Exception();
				}
			}
			else if (aItem.getNodeName().equals("continueIfFailsChecking"))
			{
				String continueIfFailsCheckingAux = aItem.getTextContent();
				
				switch (continueIfFailsCheckingAux.toLowerCase())
				{
					case "true":
						continueIfFailsChecking = true;
						break;
					case "false":
						continueIfFailsChecking = false;
						break;
					default:
						System.out.println("Node \"continueIfFailsChecking\" can only be:");
						System.out.println("  - true");
						System.out.println("  - false");
						
						throw new Exception();
				}
			}
			else
			{
				if (!this.showOnlyNecessaryErrors && this.verbose > 1)
				System.out.println("Node \"" + aItem.getNodeName() + "\" unexpected, but still parsing...");
			}
		}
		
		if (!checkingFound)
		{
			alChecking = null;
		}
		if (!doFound)
		{
			alDo = null;
		}
		
		Action action = new Action(alChecking, alDo);
		
		action.setDoRunAny(doRunAny);
		action.setContinueIfFailsMethod(continueIfFailsMethod);
		action.setContinueIfFailsChecking(continueIfFailsChecking);
		
		return action;
	}
	
}
