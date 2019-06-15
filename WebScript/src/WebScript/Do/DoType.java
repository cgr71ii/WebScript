package WebScript.Do;

public enum DoType
{
	CLICK,
	WRITE,
	PRINT,
	PRINTIMAGE,
	GOURL;
	
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
			case PRINTIMAGE:
				return new DoPrintImage();
			case GOURL:
				return new DoGoURL();
		}
		
		return new Do();
	}
}
