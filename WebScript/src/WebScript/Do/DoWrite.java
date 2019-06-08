package WebScript.Do;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;

public class DoWrite extends Do
{

	public DoWrite()
	{
		this.type = DoType.WRITE;
	}

	@Override
	public void perform() throws Exception
	{
		
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
					System.err.println("Something went wrong: DoWrite.parse(Node).");
					throw new Exception();
			}
		}
		
		return new DoWrite();
	}
	
	@Override
	public String toString()
	{
		return "[Action WRITE]{Position: " + this.position.toString() + "; Value: " + this.value + "}";
	}
	
}
