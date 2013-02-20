package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;

/**
 * An email off switch allows someone to turn off email for a Project, a Testplan, a Release, or an Environment.
 * User: jcorbett
 * Date: 2/14/13
 * Time: 2:39 PM
 */
public class EmailOffSwitch extends AbstractSystemConfiguration implements SystemConfiguration
{
    public static final String CONFIGURATION_TYPE_NAME = "email-off-switch";

    @Property
    private String turnOffEmailsForType;

    @Property
    private String turnOffEmailsForId;

    public EmailOffSwitch()
    {
        configurationType = CONFIGURATION_TYPE_NAME;
    }

    public String getTurnOffEmailsForType()
    {
        return turnOffEmailsForType;
    }

    public void setTurnOffEmailsForType(String turnOffEmailsForType)
    {
        this.turnOffEmailsForType = turnOffEmailsForType;
    }

    public String getTurnOffEmailsForId()
    {
        return turnOffEmailsForId;
    }

    public void setTurnOffEmailsForId(String turnOffEmailsForId)
    {
        this.turnOffEmailsForId = turnOffEmailsForId;
    }

    @Override
    public void validate() throws InvalidDataError
    {
        if(turnOffEmailsForType == null || turnOffEmailsForType.equals(""))
            throw new InvalidDataError(EmailOffSwitch.class.getName(), "turnOffEmailsForType", "Cannot be null or empty.");
    }

    @Override
    public void update(SystemConfiguration update) throws InvalidDataError
    {
        if(update instanceof EmailOffSwitch)
        {
            EmailOffSwitch realUpdate = EmailOffSwitch.class.cast(update);
            if(realUpdate.turnOffEmailsForType != null)
                turnOffEmailsForType = realUpdate.turnOffEmailsForType;
            if(realUpdate.turnOffEmailsForId != null)
                turnOffEmailsForId = realUpdate.turnOffEmailsForId;
            if(realUpdate.name != null)
                name = realUpdate.name;
        }
    }
}
