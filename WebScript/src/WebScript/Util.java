package WebScript;

import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Util
{
	
	public static ArrayList<Node> getChildNodes(Node node) throws Exception
	{
		ArrayList<Node> res = new ArrayList<>();
		NodeList list = node.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++)
		{
			Node item = list.item(i);
			
			if (item.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			res.add(item);
		}
		
		return res;
	}
	
	public static ArrayList<Node> getChildNodes(Node node, String[] nodes, Boolean onlyAndOnly) throws Exception
	{
		ArrayList<Node> res = new ArrayList<>();
		NodeList list = node.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++)
		{
			Node item = list.item(i);
			
			if (item.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			
			if (!Arrays.asList(nodes).contains(item.getNodeName()))
			{
				if (onlyAndOnly)
				{
					System.err.printf("Node \"%s\" was not expected for node \"%s\".\n", item.getNodeName(), node.getNodeName());
					
					throw new Exception();
				}
				
				continue;
			}
			
			res.add(item);
		}
		
		if (res.size() != nodes.length)
		{
			System.err.println("Not all nodes expected for \"" + node.getNodeName() + "\" found! Expected:");
			
			for (String s : nodes)
			{
				System.err.println(" - " + s);
			}
			
			throw new Exception();
		}
		
		return res;
	}
	
}
