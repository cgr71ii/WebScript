package WebScript.Do;

import WebScript.Checking.CheckType;
import WebScript.Checking.Checking;
import WebScript.Checking.CheckingText;
import WebScript.Checking.CheckingTitle;

public enum DoType
{
	CLICK,
	WRITE,
	PRINT;
	
	public static final Do getDoClass(DoType type)
	{
		switch (type)
		{
			case CLICK:
				return new DoClick();
			case WRITE:
				return new DoWrite();
			case PRINT:
				return new DoPrint();
		}
		
		return new Do();
	}
}
