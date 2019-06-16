package WebScript.Do;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import WebScript.AnsiColors;
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
		
		Boolean blankMessage = this.position.isEmpty();
		WebElement element = null;
		String message = "";
		
		if (!blankMessage)
		{
			element = Util.getWebElement(this.driver, this.position, this.showOnlyNecessaryErrors);
			message = element.getText();
		}
		
		String ownValue = new String(this.value);
		
		ownValue = ownValue.replaceAll("[{][t][}]", this.driver.getTitle());
		ownValue = ownValue.replaceAll("[{][}]", message);
		ownValue = ownValue.replaceAll("[\\\\][n]", "\n    ");	// Own new lines
		ownValue = ownValue.replaceAll("[\n]", "\n    ");		// Web new lines
		
		ownValue = ownValue.replaceAll("[{][R][}]", AnsiColors.RED);
		ownValue = ownValue.replaceAll("[{][G][}]", AnsiColors.GREEN);
		ownValue = ownValue.replaceAll("[{][B][}]", AnsiColors.BLUE);
		ownValue = ownValue.replaceAll("[{][Y][}]", AnsiColors.YELLOW);
		
		ownValue = ownValue.replaceAll("[{][b][}]", AnsiColors.BOLD);
		ownValue = ownValue.replaceAll("[{][u][}]", AnsiColors.UNDERLINE);
		
		ownValue = ownValue.replaceAll("[{][/][ubRGBY][}]", AnsiColors.RESET);
		
		System.out.println("    " + ownValue);
	}
	
	@Override
	public Do parse(Node dNode) throws Exception
	{
		String[] nodes = {"position", "value"};
		
		//ArrayList<Node> dwNodes = Util.getChildNodes(dNode, nodes, true);
		ArrayList<Node> dwNodes = Util.getChildNodes(dNode, nodes, false);
		
		for (Node n : dwNodes)
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
					System.out.println("Something went wrong: DoPrint.parse(Node).");
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
