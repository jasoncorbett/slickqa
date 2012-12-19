package org.tcrun.slickij.api.events;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 2:10 PM
 */
public class SimpleQueueDefinition implements QueueDefinition
{
    private String queueName;
    private boolean queuePersistent;
    private String topicMatcher;

    public SimpleQueueDefinition(String queueName, String topicMatcher)
    {
        this(queueName, false, topicMatcher);
    }

    public SimpleQueueDefinition(String queueName, boolean queuePersistent, String topicMatcher)
    {
        this.queueName = queueName;
        this.queuePersistent = queuePersistent;
        this.topicMatcher = topicMatcher;
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    @Override
    public boolean isQueuePersistent() {
        return queuePersistent;
    }

    @Override
    public String getTopicMatcher() {
        return topicMatcher;
    }

    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }

    public void setQueuePersistent(boolean queuePersistent)
    {
        this.queuePersistent = queuePersistent;
    }

    public void setTopicMatcher(String topicMatcher)
    {
        this.topicMatcher = topicMatcher;
    }
}
