package WebScript.Do;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import WebScript.Util;

public class DoClick extends Do
{

	public DoClick()
	{
		super();
		
		this.type = DoType.CLICK;
	}

	@Override
	public void perform() throws Exception
	{
		super.perform();
		
		WebElement element = Util.getWebElement(this.driver, this.position, this.showOnlyNecessaryErrors);
		
		element.click();
	}
	
	@Override
	public Do parse(Node dNode) throws Exception
	{
		String[] nodes = {"position"};
		ArrayList<Node> dcNodes = Util.getChildNodes(dNode, nodes, true);
		
		for (Node n : dcNodes)
		{
			switch (n.getNodeName().toLowerCase())
			{
				case "position":
					this.position.parse(n);
					break;
				default:
					System.out.println("Something went wrong: DoClick.parse(Node).");
					throw new Exception();
			}
		}
		
		return new DoClick();
	}
	
	@Override
	public String toString()
	{
		return "[Action CLICK]{Position: " + this.position + "}";
	}
	
}
