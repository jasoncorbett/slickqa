package org.tcrun.slickij.api.events;

import org.tcrun.slickij.api.data.Testrun;
/**
 * User: jcorbett
 * Date: 1/8/13
 * Time: 11:14 AM
 */
public class CreateTestrunEvent extends SlickCreateEvent<Testrun>
{
    public CreateTestrunEvent(Testrun run)
    {
        super(run);
    }
}
