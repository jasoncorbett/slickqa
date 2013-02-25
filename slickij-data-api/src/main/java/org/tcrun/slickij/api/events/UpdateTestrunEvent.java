package org.tcrun.slickij.api.events;

import org.tcrun.slickij.api.data.Testrun;

/**
 * User: jcorbett
 * Date: 1/8/13
 * Time: 11:21 AM
 */
public class UpdateTestrunEvent extends SlickUpdateEvent<Testrun>
{
    public UpdateTestrunEvent(Testrun before, Testrun after)
    {
        super(before, after);
    }
}
