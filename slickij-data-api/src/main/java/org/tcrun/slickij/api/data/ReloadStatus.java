package org.tcrun.slickij.api.data;

/**
 * User: jcorbett
 * Date: 12/4/12
 * Time: 9:49 AM
 */
public class ReloadStatus
{
    private String systemName;
    private Long reloadTime;
    private String systemStatus;

    public ReloadStatus()
    {
    }

    public ReloadStatus(String systemName, Long reloadTime, String systemStatus)
    {
        this.systemName = systemName;
        this.reloadTime = reloadTime;
        this.systemStatus = systemStatus;
    }

    public String getSystemName()
    {
        return systemName;
    }

    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public Long getReloadTime()
    {
        return reloadTime;
    }

    public void setReloadTime(Long reloadTime)
    {
        this.reloadTime = reloadTime;
    }

    public String getSystemStatus()
    {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus)
    {
        this.systemStatus = systemStatus;
    }
}
