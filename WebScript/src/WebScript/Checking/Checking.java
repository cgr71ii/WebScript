package WebScript.Checking;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import WebScript.Checking.CheckType;
import WebScript.Locator.Position;

public class Checking
{
	protected CheckType type;
	
	// Fields for checking
	protected Position position;
	protected String value;
	
	public Checking()
	{
		this.position = new Position();
		this.value = new String();
	}
	
	public Boolean check() throws Exception
	{
		System.err.println("This method has to be overridden.");
		
		throw new Exception();
	}
	
	public Checking parse(Node cNode) throws Exception
	{
		NamedNodeMap cNNM = cNode.getAttributes();
		
		if (cNNM.getLength() != 1)
		{
			System.err.println("Checking has to have at least / only the \"type\" attribute.");
			
			throw new Exception();
		}
		
		Node typeNode = cNNM.getNamedItem("check"); 
		
		if (typeNode == null)
		{
			System.err.println("Checking has to have the \"check\" attribute, not other.");
			
			throw new Exception();
		}
		
		try
		{
			CheckType type = CheckType.valueOf(typeNode.getNodeValue().toUpperCase());
			
			this.type = type;
		}
		catch (Exception e)
		{
			System.err.println("The checking type can only be: ");
			
			for (CheckType t : CheckType.values())
			{
				System.err.println(" - " + t.toString());
			}
			
			throw e;
		}
		
		return CheckType.getCheckingClass(this.type);
		
		//String type = typeNode.getNodeValue();
	}
}
