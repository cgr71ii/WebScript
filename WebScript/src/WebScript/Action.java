package WebScript;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Do.Do;
import WebScript.Do.DoMethod;

public class Action
{
	
	private Checking checking;
	
	private Do _do;

	public Action(Checking checking, Do _do)
	{
		this.checking = checking;
		this._do = _do;
	}
	
	public Boolean perform()
	{
		Boolean checking = this.performChecking();
		
		if (!checking)
		{
			return checking;
		}
		
		return checking;
	}
	
	private Boolean performChecking()
	{
		
		return false;
	}
	
	private void performMethod()
	{
		
	}
}
