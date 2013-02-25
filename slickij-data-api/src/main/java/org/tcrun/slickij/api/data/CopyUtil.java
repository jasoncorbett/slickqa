package org.tcrun.slickij.api.data;

import java.util.Date;

/**
 * Contains utility functions for doing things with Copyable.
 * User: jcorbett
 * Date: 12/18/12
 * Time: 6:15 PM
 */
public class CopyUtil
{
    public static <T extends Copyable<T>> T copyIfNotNull(T original)
    {
        if(original == null)
            return null;
        else
            return original.createCopy();
    }

    public static Date copyDateIfNotNull(Date original)
    {
        if(original == null)
            return null;
        else
            return new Date(original.getTime());
    }
}
