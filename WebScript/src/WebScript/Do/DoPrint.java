package WebScript.Do;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import WebScript.Util;

public class DoPrint extends Do
{

	public DoPrint()
	{
		super();
		
		this.type = DoType.CLICK;
	}

	@Override
	public void perform() throws Exception
	{
		super.perform();
		
		WebElement element = Util.getWebElement(this.driver, this.position);
		String message = element.getText();
		
		String ownValue = new String(this.value);
		
		ownValue = ownValue.replaceAll("[{][}]", message);
		
		System.out.println("    " + ownValue);
	}
	
	@Override
	public Do parse(Node dNode) throws Exception
	{
		String[] nodes = {"position", "value"};
		ArrayList<Node> dwNodes = Util.getChildNodes(dNode, nodes, true);
		
		for (Node n : dwNodes)
		{
			switch (n.getNodeName().toLowerCase())
			{
				case "position":
					this.position.parse(n);
					break;
				case "value":
					this.value = n.getTextContent();
					break;
				default:
					System.out.println("Something went wrong: DoWrite.parse(Node).");
					throw new Exception();
			}
		}
		
		return new DoPrint();
	}
	
	@Override
	public String toString()
	{
		return "[Action PRINT]{Position: " + this.position + "; Value: " + this.value + "}";
	}
	
}
