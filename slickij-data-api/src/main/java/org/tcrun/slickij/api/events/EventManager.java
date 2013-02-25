package org.tcrun.slickij.api.events;

import javax.mail.event.MessageCountListener;

/**
 * User: jcorbett
 * Date: 11/30/12
 * Time: 2:03 PM
 */
public interface EventManager
{
    public void publishMessage(RawMessage message);
    public void publishEvent(SlickEvent event);
    public void addMessageListener(QueueDefinition queue, MessageListener listener) throws SlickEventException;
    public void reload();
    public enum SystemStatus {
        CONFIGURED,
        NOT_CONFIGURED,
        ERROR
    }

    public SystemStatus getSystemStatus();
}
