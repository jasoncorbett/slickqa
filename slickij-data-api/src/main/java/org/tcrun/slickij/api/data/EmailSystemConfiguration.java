package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;

/**
 * System configuration data for email alerts.
 * User: jcorbett
 * Date: 1/24/13
 * Time: 9:14 AM
 */
public class EmailSystemConfiguration extends AbstractSystemConfiguration
{
    public static final String CONFIGURATION_TYPE_NAME = "email-system-configuration";

    @Property
    private String smtpHostname;

    @Property
    private Integer smtpPort;

    @Property
    private String smtpUsername;

    @Property
    private String smtpPassword;

    @Property
    private Boolean ssl;

    @Property
    private Boolean enabled;

    @Property
    private String sender;

    public EmailSystemConfiguration()
    {
        configurationType = EmailSystemConfiguration.CONFIGURATION_TYPE_NAME;
        name = "Global Email System Configuration";
    }

    public String getSmtpHostname()
    {
        return smtpHostname;
    }

    public void setSmtpHostname(String smtpHostname)
    {
        this.smtpHostname = smtpHostname;
    }

    public Integer getSmtpPort()
    {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort)
    {
        this.smtpPort = smtpPort;
    }

    public String getSmtpUsername()
    {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername)
    {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpPassword()
    {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword)
    {
        this.smtpPassword = smtpPassword;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public Boolean getSsl()
    {
        return ssl;
    }

    public void setSsl(Boolean ssl)
    {
        this.ssl = ssl;
    }

    @Override
    public void validate() throws InvalidDataError
    {
        if(smtpPort == null)
            smtpPort = 25;
        if(enabled == null)
            enabled = false;
        if(enabled)
        {
            if(smtpHostname == null || smtpHostname.equals(""))
                throw new InvalidDataError("EmailSystemConfiguration", "smtpHostname", "When email is enabled, smtpHostname cannot be empty or null.");
            if(sender == null)
                throw new InvalidDataError("EmailSystemConfiguration", "sender", "When email is enabled, sender cannot be empty or null.");
        }
    }

    @Override
    public void update(SystemConfiguration update) throws InvalidDataError
    {
        if(update instanceof EmailSystemConfiguration)
        {
            EmailSystemConfiguration realUpdate = EmailSystemConfiguration.class.cast(update);
            if(realUpdate.smtpHostname != null)
                smtpHostname = realUpdate.smtpHostname;
            if(realUpdate.smtpPort != null)
                smtpPort = realUpdate.smtpPort;
            if(realUpdate.smtpUsername != null)
                smtpUsername = realUpdate.smtpUsername;
            if(realUpdate.smtpPassword != null)
                smtpPassword = realUpdate.smtpPassword;
            if(realUpdate.enabled != null)
                enabled = realUpdate.enabled;
            if(realUpdate.ssl != null)
                ssl = realUpdate.ssl;
            if(realUpdate.sender != null)
                sender = realUpdate.sender;
        }
    }
}
