package org.tcrun.slickij.api.data;

import java.util.UUID;

/**
 * An update is an activity that needs to get applied.
 * User: jcorbett
 * Date: 6/24/12
 * Time: 5:17 PM
 */
public interface SlickUpdate
{
    public String getUpdateId();
    public String getDescription();
    public String getName();
    public void apply();
    public boolean needsApplying();
}
