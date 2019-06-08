package WebScript.Do;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;
import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Checking.CheckingTitle;

public class DoClick extends Do
{

	public DoClick()
	{
		this.type = DoType.CLICK;
	}

	@Override
	public void perform() throws Exception
	{
		
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
					System.err.println("Something went wrong: DoClick.parse(Node).");
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
