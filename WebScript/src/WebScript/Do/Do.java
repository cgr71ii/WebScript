package WebScript.Do;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Locator.Position;

public class Do
{

	protected DoType type;
	
	// Fields for checking
	protected Position position;
	protected String value;
	
	public Do()
	{
		this.position = new Position();
		this.value = new String();
	}

	public void perform() throws Exception
	{
		System.err.println("This method has to be overridden.");
		
		throw new Exception();
	}
	
	public Do parse(Node dNode) throws Exception
	{
		NamedNodeMap dNNM = dNode.getAttributes();
		
		if (dNNM.getLength() != 1)
		{
			System.err.println("\"Do\" node has to have at least / only the \"method\" attribute.");
			
			throw new Exception();
		}
		
		Node typeNode = dNNM.getNamedItem("method"); 
		
		if (typeNode == null)
		{
			System.err.println("\"Do\" node has to have the \"method\" attribute, not other.");
			
			throw new Exception();
		}
		
		try
		{
			DoType type = DoType.valueOf(typeNode.getNodeValue().toUpperCase());
			
			this.type = type;
		}
		catch (Exception e)
		{
			System.err.println("The \"do\" node type can only be: ");
			
			for (DoType t : DoType.values())
			{
				System.err.println(" - " + t.toString());
			}
			
			throw e;
		}
		
		return DoType.getDoClass(this.type);
	}
	
}
