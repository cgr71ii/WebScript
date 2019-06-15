package WebScript.Do;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;

import WebScript.AnsiColors;
import WebScript.Util;

public class DoPrintImage extends Do
{

	public DoPrintImage()
	{
		super();
		
		this.type = DoType.PRINTIMAGE;
	}

	@Override
	public void perform() throws Exception
	{
		super.perform();
		
		if (Util.checkIfViuIsInstalled() != 0)
		{
			if (!this.showOnlyNecessaryErrors)
			{
				System.out.printf("    %sCan't show images%s if viu is not installed (has not been detected).\n", AnsiColors.RED, AnsiColors.RESET);
			}
			
			throw new Exception();
		}
		
		WebElement element = Util.getWebElement(this.driver, this.position, this.showOnlyNecessaryErrors);
		String src = element.getAttribute("src");
		
		if (src == null || src.isEmpty())
		{
			if (!this.showOnlyNecessaryErrors)
			{
				System.out.printf("    %sCould not%s find the image.\n", AnsiColors.RED, AnsiColors.RESET);
			}
			
			throw new Exception();
		}
		
		Boolean imageSaved = false;
		String path = "/tmp/img";
		
		imageSaved = Util.downloadImage(src, path);
		
		if (!imageSaved)
		{
			if (!this.showOnlyNecessaryErrors)
			{
				System.out.printf("    %sCould not%s download the image.\n", AnsiColors.RED, AnsiColors.RESET);
			}
			
			throw new Exception();
		}
		
		if (Util.executeCommand("viu -h 10 " + path, true, 10, 100, true) != 0)
		{
			if (!this.showOnlyNecessaryErrors)
			{
				System.out.printf("    %sCould not%s show the image.\n", AnsiColors.RED, AnsiColors.RESET);
			}
			
			throw new Exception();
		}
	}
	
	@Override
	public Do parse(Node dNode) throws Exception
	{
		String[] nodes = {"position"};
		ArrayList<Node> dwNodes = Util.getChildNodes(dNode, nodes, true);
		
		for (Node n : dwNodes)
		{
			switch (n.getNodeName().toLowerCase())
			{
				case "position":
					this.position.parse(n);
					break;
				default:
					System.out.println("Something went wrong: DoPrintImage.parse(Node).");
					throw new Exception();
			}
		}
		
		return new DoPrint();
	}
	
	@Override
	public String toString()
	{
		return "[Action PRINT IMAGE]{Position: " + this.position + "}";
	}
	
}
