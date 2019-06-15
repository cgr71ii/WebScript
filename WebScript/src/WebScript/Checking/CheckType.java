package WebScript.Checking;

public enum CheckType
{
	TITLE,
	TEXT,
	IF;
	
	public static final Checking getCheckingClass(CheckType type)
	{
		switch (type)
		{
			case TITLE:
				return new CheckingTitle();
			case TEXT:
				return new CheckingText();
			case IF:
				return new CheckingIf();
		}
		
		return new Checking();
	}
}