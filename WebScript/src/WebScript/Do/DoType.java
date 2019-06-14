package WebScript.Do;

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
