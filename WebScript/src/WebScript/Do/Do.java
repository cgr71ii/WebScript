package WebScript.Do;

import org.openqa.selenium.WebDriver;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import WebScript.DefaultValues;
import WebScript.Locator.Position;

public class Do
{

	protected DoType type;
	
	protected WebDriver driver;

	protected Boolean showOnlyNecessaryErrors;
	
	// Fields for checking
	protected Position position;
	protected String value;
	
	public Do()
	{
		this.position = new Position();
		this.value = new String();
		this.driver = null;
		this.showOnlyNecessaryErrors = DefaultValues.SHOW_ONLY_NECESSARY_ERRORS;
	}
	
	public final void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public final void setShowOnlyNecessaryErrors(Boolean showOnlyNecessaryErrors)
	{
		this.showOnlyNecessaryErrors = showOnlyNecessaryErrors;
	}

	public void perform() throws Exception
	{
		if (this.driver == null)
		{
			System.out.println("Driver is not set.");
			
			throw new Exception();
		}
	}
	
	public Do parse(Node dNode) throws Exception
	{
		NamedNodeMap dNNM = dNode.getAttributes();
		
		if (dNNM.getLength() != 1 && dNNM.getLength() != 2)
		{
			System.out.println("\"Do\" node has to have at least / only the \"method\" attribute.");
			
			throw new Exception();
		}
		
		Node typeNode = dNNM.getNamedItem("method");
		
		if (typeNode == null)
		{
			System.out.println("\"Do\" node has to have the \"method\" attribute, not other.");
			
			throw new Exception();
		}
		
		try
		{
			DoType type = DoType.valueOf(typeNode.getNodeValue().toUpperCase());
			
			this.type = type;
		}
		catch (Exception e)
		{
			System.out.println("The \"do\" node type can only be: ");
			
			for (DoType t : DoType.values())
			{
				System.out.println(" - " + t.toString());
			}
			
			throw e;
		}
		
		return DoType.getDoClass(this.type);
	}
	
}
