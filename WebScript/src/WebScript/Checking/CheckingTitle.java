package WebScript.Checking;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;

public class CheckingTitle extends Checking
{

	public CheckingTitle()
	{
		this.type = CheckType.TITLE;
	}

	@Override
	public Boolean check() throws Exception
	{
		return true;
	}
	
	@Override
	public Checking parse(Node cNode) throws Exception
	{
		String[] nodes = {"value"};
		ArrayList<Node> ctNodes = Util.getChildNodes(cNode, nodes, true);
		
		for (Node n : ctNodes)
		{
			switch (n.getNodeName().toLowerCase())
			{
				case "value":
					this.value = n.getTextContent();
					break;
				default:
					System.err.println("Something went wrong: CheckingTitle.parse(Node).");
					throw new Exception();
			}
		}
		
		return new CheckingTitle();
	}
	
	@Override
	public String toString()
	{
		return "[Checking TITLE]{Value: " + this.value + "}";
	}
	
}
