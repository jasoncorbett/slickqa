package org.tcrun.slickij.api.events;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 2:06 PM
 */
public interface QueueDefinition
{
    public String getQueueName();
    public boolean isQueuePersistent();
    public String getTopicMatcher();
}
