package org.tcrun.slickij.api.events;

import org.tcrun.slickij.api.data.Project;
import org.tcrun.slickij.api.data.Release;

/**
 * User: jcorbett
 * Date: 12/4/12
 * Time: 10:09 AM
 */
public class CreateReleaseEvent extends SlickCreateEvent<Release>
{
    public CreateReleaseEvent(Project project, Release release)
    {
        super(release);
        setHeader("projectid", project.getId());
    }

    public CreateReleaseEvent(RawMessage message) throws SlickEventException
    {
        super(message, Release.class);
    }

    public String getProjectId()
    {
        return getHeader("projectid");
    }
}
