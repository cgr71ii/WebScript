package WebScript.Checking;

public enum CheckType
{
	TITLE,
	TEXT;
	
	public static final Checking getCheckingClass(CheckType type)
	{
		switch (type)
		{
			case TITLE:
				return new CheckingTitle();
			case TEXT:
				return new CheckingText();
		}
		
		return new Checking();
	}
}