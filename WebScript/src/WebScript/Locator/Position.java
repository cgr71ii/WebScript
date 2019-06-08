package WebScript.Locator;

import org.openqa.selenium.By;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import WebScript.Checking.CheckType;

public class Position
{
	private PositionType type;
	
	private String value;
	
	public Position()
	{
		this.type = PositionType.NONE;
		this.value = "";
	}
	
	public Position(PositionType type)
	{
		this.type = type;
		this.value = "";
	}
	
	public By getBy()
	{
		if (this.value.isEmpty())
		{
			return null;
		}
		
		return PositionType.getBy(this.type, this.value);
	}
	
	public void parse(Node pNode) throws Exception
	{
		NamedNodeMap pNNM = pNode.getAttributes();
		
		if (pNNM.getLength() != 1)
		{
			System.out.println("Position has to have at least / only the \"type\" attribute.");
			
			throw new Exception();
		}
		
		Node typeNode = pNNM.getNamedItem("type"); 
		
		if (typeNode == null)
		{
			System.out.println("Position has to have the \"type\" attribute, not other.");
			
			throw new Exception();
		}
		
		try
		{
			PositionType type = PositionType.valueOf(typeNode.getNodeValue().toUpperCase());
			
			this.type = type;
		}
		catch (Exception e)
		{
			System.out.println("The position type can only be: ");
			
			for (PositionType t : PositionType.values())
			{
				System.out.println(" - " + t.name());
			}
			
			throw e;
		}
		
		this.value = pNode.getTextContent();
		
		if (this.value == null)
		{
			System.out.println("Something went wrong with the value of the position.");
			
			throw new Exception();
		}
	}
	
	@Override
	public String toString()
	{
		return "{Type: " + this.type.toString() + "; Value: " + this.value + "}";
	}
	
}
