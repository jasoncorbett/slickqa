package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Reference;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jcorbett
 */
@Entity("hoststatus")
public class HostStatus implements Serializable, Copyable<HostStatus>
{
	@Id
	private String hostname;

	@Property
	private Date lastCheckin;

	@Reference
	private Result currentWork;

	public Result getCurrentWork()
	{
        if(currentWork != null && currentWork.getRecorded() != null)
        {
            Date now = new Date();
            int seconds = (int)((now.getTime() - currentWork.getRecorded().getTime())/1000);
            currentWork.setRunlength(seconds);
        }

		return currentWork;
	}

	public void setCurrentWork(Result currentWork)
	{
		this.currentWork = currentWork;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public Date getLastCheckin()
	{
		return lastCheckin;
	}

	public void setLastCheckin(Date lastCheckin)
	{
		this.lastCheckin = lastCheckin;
	}
    
    public int getSecondsSinceLastCheckin()
    {
        return (int)(((new Date()).getTime() - lastCheckin.getTime())/1000);
    }

    @Override
    public HostStatus createCopy()
    {
        HostStatus copy = new HostStatus();

        copy.setHostname(hostname);
        copy.setLastCheckin(new Date(lastCheckin.getTime()));
        copy.setCurrentWork(currentWork.createCopy());

        return copy;
    }
}
