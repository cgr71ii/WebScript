package WebScript.Locator;

import org.openqa.selenium.By;

public enum PositionType
{
	NONE,
	XPATH,
	CSS;
	
	public static By getBy(PositionType type, String position)
	{
		By by = null;
		
		switch (type)
		{
			case NONE:
				break;
			case XPATH:
				by = By.xpath(position);
				break;
			case CSS:
				by = By.cssSelector(position);
				break;
		}
		
		return by;
	}
}
