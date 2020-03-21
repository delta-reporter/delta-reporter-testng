package com.deltareporter;

import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest2 
{
    /**
     * Rigorous Test :-)
     */
    @Test(enabled=false)
    public void shouldAnswerWithNotTrue()
    {
        assertTrue( true );
    }

    @Test
	public void testCaseConditionalSkipException(){
        Boolean DataAvailable = false;
		System.out.println("Im in Conditional Skip");
		if(!DataAvailable)
		    throw new SkipException("Skipping this exception");
		System.out.println("Executed Successfully");
	}
}
