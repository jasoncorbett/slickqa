package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;
import java.io.Serializable;

/**
 * Class representing a testcase step in the database.
 *
 * @author slambson
 */
public class Step implements Serializable
{

    @Property
    private String name;

    @Property
    private String expectedResult;

    public String getExpectedResult()
    {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult)
    {
        this.expectedResult = expectedResult;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
