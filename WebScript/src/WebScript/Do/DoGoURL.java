package WebScript.Do;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;

public class DoGoURL extends Do
{

	public DoGoURL()
	{
		super();
		
		this.type = DoType.GOURL;
	}

	@Override
	public void perform() throws Exception
	{
		super.perform();

		this.driver.get(this.value);
	}
	
	@Override
	public Do parse(Node dNode) throws Exception
	{
		String[] nodes = {"value"};
		ArrayList<Node> dwNodes = Util.getChildNodes(dNode, nodes, true);
		
		for (Node n : dwNodes)
		{
			switch (n.getNodeName().toLowerCase())
			{
				case "value":
					this.value = n.getTextContent();
					break;
				default:
					System.out.println("Something went wrong: DoGoURL.parse(Node).");
					throw new Exception();
			}
		}
		
		return new DoPrint();
	}
	
	@Override
	public String toString()
	{
		return "[Action GO URL]{Value: " + this.value + "}";
	}
	
}
