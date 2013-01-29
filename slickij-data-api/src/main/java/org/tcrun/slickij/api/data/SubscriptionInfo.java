package org.tcrun.slickij.api.data;

import com.google.code.morphia.annotations.Property;

/**
* Created with IntelliJ IDEA.
* User: jcorbett
* Date: 1/29/13
* Time: 12:07 PM
* To change this template use File | Settings | File Templates.
*/
public class SubscriptionInfo
{
    @Property
    private String subscriptionType;

    @Property
    private String subscriptionValue;

    @Property
    private Boolean onStart;

    public String getSubscriptionType()
    {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType)
    {
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionValue()
    {
        return subscriptionValue;
    }

    public void setSubscriptionValue(String subscriptionValue)
    {
        this.subscriptionValue = subscriptionValue;
    }

    public Boolean getOnStart()
    {
        return onStart;
    }

    public void setOnStart(Boolean onStart)
    {
        this.onStart = onStart;
    }
}
