package WebScript.Checking;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import WebScript.Util;

public class CheckingText extends Checking
{
	
	public CheckingText()
	{
		super();
		
		this.type = CheckType.TEXT;
	}

	@Override
	public CheckingReturn check() throws Exception
	{
		super.check();
		
		WebElement element = Util.getWebElement(this.driver, this.position);
		String text = element.getText();
		
		if (text.equals(this.value))
		{
			return CheckingReturn.TRUE;
		}
		
		return CheckingReturn.FALSE;
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
					this.value = Util.getValue(n);
					break;
				default:
					System.out.println("Something went wrong: CheckingText.parse(Node).");
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
