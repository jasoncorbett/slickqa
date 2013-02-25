package org.tcrun.slickij.data;

import com.google.inject.Inject;
import org.tcrun.slickij.api.ReloadResource;
import org.tcrun.slickij.api.data.ReloadStatus;
import org.tcrun.slickij.api.events.EventManager;

import java.util.Date;

/**
 * User: jcorbett
 * Date: 12/4/12
 * Time: 9:59 AM
 */
public class ReloadResourceImpl implements ReloadResource
{
    private EventManager eventSubsystem;

    @Inject
    public ReloadResourceImpl(EventManager eventSubsystem)
    {
        this.eventSubsystem = eventSubsystem;
    }

    @Override
    public ReloadStatus reloadEventSubSystem()
    {
        long start = System.currentTimeMillis();
        eventSubsystem.reload();
        long end = System.currentTimeMillis();

        return new ReloadStatus("event", end - start, eventSubsystem.getSystemStatus().toString());
    }
}
