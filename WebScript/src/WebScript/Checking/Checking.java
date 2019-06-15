package WebScript.Checking;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import WebScript.Checking.CheckType;
import WebScript.Locator.Position;

public class Checking
{
	protected CheckType type;
	
	protected WebDriver driver;
	
	// Fields for checking
	protected Position position;
	protected String value;
	
	public Checking()
	{
		this.position = new Position();
		this.value = new String();
		this.driver = null;
	}
	
	public final void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public CheckingReturn check() throws Exception
	{
		if (this.driver == null)
		{
			System.out.println("Driver is not set.");
			
			throw new Exception();
		}
		
		return CheckingReturn.FALSE;
	}
	
	public Checking parse(Node cNode) throws Exception
	{
		NamedNodeMap cNNM = cNode.getAttributes();
		
		if (cNNM.getLength() != 1)
		{
			System.out.println("\"Checking\" node has to have at least / only the \"check\" attribute.");
			
			throw new Exception();
		}
		
		Node typeNode = cNNM.getNamedItem("check"); 
		
		if (typeNode == null)
		{
			System.out.println("\"Checking\" node has to have the \"check\" attribute, not other.");
			
			throw new Exception();
		}
		
		try
		{
			CheckType type = CheckType.valueOf(typeNode.getNodeValue().toUpperCase());
			
			this.type = type;
		}
		catch (Exception e)
		{
			System.out.println("The \"checking\" node type can only be: ");
			
			for (CheckType t : CheckType.values())
			{
				System.out.println(" - " + t.toString());
			}
			
			throw e;
		}
		
		return CheckType.getCheckingClass(this.type);
	}
}
