package org.tcrun.slickij.api.data;


import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a user's email subscriptions.  The "name" property will be the email
 * address.
 * User: jcorbett
 * Date: 1/24/13
 * Time: 11:14 AM
 */
public class EmailSubscription extends AbstractSystemConfiguration
{
    public static final String CONFIGURATION_TYPE_NAME = "email-subscription";

    @Embedded
    private List<SubscriptionInfo> subscriptions;

    @Property
    private Boolean enabled;

    public EmailSubscription()
    {
        configurationType = EmailSubscription.CONFIGURATION_TYPE_NAME;
    }

    public List<SubscriptionInfo> getSubscriptions()
    {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionInfo> subscriptions)
    {
        this.subscriptions = subscriptions;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    @Override
    public void validate() throws InvalidDataError
    {
        if(name == null || name.equals(""))
            throw new InvalidDataError("EmailSubscription", "name", "The name of the email subscription should be a valid email address.");
        if(subscriptions == null)
            subscriptions = new ArrayList<SubscriptionInfo>();
    }

    @Override
    public void update(SystemConfiguration update) throws InvalidDataError
    {
        if(update instanceof EmailSubscription)
        {
            EmailSubscription realUpdate = EmailSubscription.class.cast(update);
            if(realUpdate.name != null)
                name = realUpdate.name;
            if(realUpdate.enabled != null)
                enabled = realUpdate.enabled;
            if(realUpdate.subscriptions != null)
                subscriptions = realUpdate.subscriptions;
        }
    }

}
