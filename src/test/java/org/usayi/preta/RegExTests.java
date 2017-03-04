package org.usayi.preta;

import org.junit.Test;

public class RegExTests
{

	@Test
	public void regExTest()
	{
		try
		{
//			String pattern = "\\+?(?:229)?(?:97|96|61|62|66|67)\\d{6}";
			String pattern = "\\+?(?:229)?(?:95|94)\\d{6}";
			String phoneNumber = "96799779";
			if ( phoneNumber.matches( pattern))
				assert( true);
			else
				assert( false);
			
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}
}
