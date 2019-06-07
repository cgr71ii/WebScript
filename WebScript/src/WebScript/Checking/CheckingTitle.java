package WebScript.Checking;

import org.w3c.dom.Node;

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
		return new CheckingTitle();
	}
	
	@Override
	public String toString()
	{
		// TODO implement
		return "NOT IMPLEMENTED";
	}
	
}
