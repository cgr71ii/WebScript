package WebScript.Checking;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;

public class CheckingTitle extends Checking
{

	public CheckingTitle()
	{
		super();
		
		this.type = CheckType.TITLE;
	}

	@Override
	public CheckingReturn check() throws Exception
	{
		super.check();
		
		String title = this.driver.getTitle();
		
		if (title.equals(this.value))
		{
			return CheckingReturn.TRUE;
		}
		
		return CheckingReturn.FALSE;
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
					System.out.println("Something went wrong: CheckingTitle.parse(Node).");
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
