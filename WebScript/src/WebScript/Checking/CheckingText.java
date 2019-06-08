package WebScript.Checking;

import java.util.ArrayList;

import org.w3c.dom.Node;

import WebScript.Util;

public class CheckingText extends Checking
{
	
	public CheckingText()
	{
		this.type = CheckType.TEXT;
	}

	@Override
	public Boolean check() throws Exception
	{
		return true;
	}
	
	@Override
	public Checking parse(Node cNode) throws Exception
	{
		String[] nodes = {"position", "value"};
		ArrayList<Node> ctNodes = Util.getChildNodes(cNode, nodes, true);
		
		for (Node n : ctNodes)
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
					System.err.println("Something went wrong: CheckingText.parse(Node).");
					throw new Exception();
			}
		}
		
		return new CheckingText();
	}
	
	@Override
	public String toString()
	{
		return "[Checking TEXT]{Position: " + this.position.toString() + "; Value: " + this.value + "}";
	}
	
}
